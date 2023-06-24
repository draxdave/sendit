package com.drax.sendit.view.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import app.siamak.sendit.BuildConfig
import app.siamak.sendit.R
import com.drax.sendit.data.service.Analytics
import com.drax.sendit.data.service.Event
import com.drax.sendit.data.service.SenditFirebaseService
import com.drax.sendit.domain.network.model.auth.sso.SignInSsoRequest
import com.drax.sendit.view.util.DeviceInfoHelper
import com.drax.sendit.view.util.ifNotNull
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json

@Singleton
class SsoHandler @Inject constructor(
    private val analytics: Analytics,
    private val deviceInfoHelper: DeviceInfoHelper,
    @ApplicationContext private val appContext: Context,
) {

    private val tag = javaClass.canonicalName
    private val oneTapClient: SignInClient by lazy {
        Identity.getSignInClient(appContext)
    }

    private lateinit var signUpRequest: BeginSignInRequest
    private lateinit var signupActivityResult: ActivityResultLauncher<IntentSenderRequest>

    private val mGoogleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestId()
            .requestIdToken(BuildConfig.ONETAP_CLIENT_ID)
            .build()
        GoogleSignIn.getClient(appContext, gso)
    }
    private lateinit var googleAuthCallback: ActivityResultLauncher<Intent>

    suspend fun launchOneTapSignIn(
        launcher: ActivityResultLauncher<IntentSenderRequest>,
        onEvent: (SsoEvent) -> Unit
    ) {
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(BuildConfig.ONETAP_CLIENT_ID)
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()

        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                analytics.set(Event.SignIn.SsoDone)
                try {
                    launcher.launch(
                        IntentSenderRequest.Builder(result.pendingIntent).build()
                    )

                } catch (e: IntentSender.SendIntentException) {
                    onEvent(SsoEvent.SignInFailed(R.string.signin_google_error))
                    Log.e(tag, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener { e ->
                analytics.set(Event.SignIn.SignInFlowFailed)
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                Log.d(tag, "Errorrrrr " + e.localizedMessage)
                showSignupFlow(onEvent)
            }.await()
    }

    private fun showSignupFlow(onEvent: (SsoEvent) -> Unit) {
        analytics.set(Event.SignIn.SignUpFlowStarted)
        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(BuildConfig.ONETAP_CLIENT_ID)
                    // Show all accounts on the device.
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()

        oneTapClient.beginSignIn(signUpRequest)
            .addOnSuccessListener { result ->
                analytics.set(Event.SignIn.SsoDone)
                try {
                    signupActivityResult.launch(
                        IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    )

                } catch (e: IntentSender.SendIntentException) {
                    onEvent(SsoEvent.SignInFailed(R.string.signin_google_error))
                    Log.e(tag, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener { e ->
                analytics.set(Event.SignIn.SignInFlowFailed)
                // No Google Accounts found. Just continue presenting the signed-out UI.
                Log.d(tag, e.localizedMessage)
                googleAuth()
            }
    }

    private fun googleAuth() {
        googleAuthCallback.launch(mGoogleSignInClient.signInIntent)
    }

    private fun checkResult(result: ActivityResult, onEvent: (SsoEvent) -> Unit) {
        if (result.resultCode == Activity.RESULT_OK) {

            val credential = Identity.getSignInClient(appContext)
                .getSignInCredentialFromIntent(result.data)
            val idToken = credential.googleIdToken

            if (idToken != null) {
                SenditFirebaseService.token(
                    onError = {
                        analytics.set(Event.SignIn.Failed("Token is missing"))
                        onEvent(SsoEvent.SignInFailed(R.string.signin_google_error))

                    }) { instanceId ->
                    onEvent(
                        SsoEvent.SignSucceed(
                            credential.toSignInRequest(
                                instanceId,
                                idToken
                            )
                        )
                    )
                }
                Log.d("TAG", "Got password.")
            }
            else {
                // Shouldn't happen.
                Log.d("TAG", "No ID token or password!")
                analytics.set(Event.SignIn.Failed("No ID token or password!"))
                onEvent(SsoEvent.SignInFailed(R.string.signin_google_error))
            }
        } else {
            analytics.set(Event.SignIn.Failed("Negative Result"))
            onEvent(SsoEvent.SignInFailed(R.string.signin_google_error))
        }
    }

    fun unregister() {
        signupActivityResult.unregister()
        googleAuthCallback.unregister()
    }


    fun register(fragment: Fragment, onEvent: (SsoEvent) -> Unit) {


        googleAuthCallback =
            fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result: ActivityResult ->
                if (result.resultCode == AppCompatActivity.RESULT_OK && result.data != null) {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    handleSignInResult(task, onEvent)
                } else {
                    val list = result.data?.extras?.keySet()
                        ?.map { it to (result.data?.extras?.get(it) ?: "null") }

                    Log.d("list", list.toString())

                    result.data?.let {
                        Log.e(
                            tag,
                            "Google sign in failed: ${it.getStringExtra("com.google.android.gms.auth.api.signin.EXTRA_SIGN_IN_FAILURE")}"
                        )
                        onEvent(SsoEvent.SignInFailed(R.string.signin_google_error))
                    }
                }
            }
    }

    private fun handleSignInResult(
        completedTask: Task<GoogleSignInAccount>,
        onEvent: (SsoEvent) -> Unit
    ) {
        try {
            val request = completedTask.getResult(ApiException::class.java).toSignInRequest()
            onEvent(SsoEvent.SignSucceed(request))

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(tag, "signInResult:failed code=" + e.statusCode)
            onEvent(SsoEvent.SignInFailed(R.string.signin_google_error_no_account))
        }
    }

    private fun GoogleSignInAccount.toSignInRequest(): SignInSsoRequest? =
        email.ifNotNull(id, idToken) {
            SignInSsoRequest(
                email = email!!,
                deviceId = deviceInfoHelper.getId(),
                instanceId = id!!,
                tokenId = idToken!!
            )
        }

    private fun SignInCredential.toSignInRequest(
        instanceId: String,
        idToken: String
    ): SignInSsoRequest = SignInSsoRequest(
        email = id,
        deviceId = deviceInfoHelper.getId(),
        instanceId = instanceId,
        tokenId = idToken
    )
}

sealed class SsoEvent {
    data class SignInFailed(val stringId: Int) : SsoEvent()
    data class SignSucceed(val request: SignInSsoRequest?) : SsoEvent()
}