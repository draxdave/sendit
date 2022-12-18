package com.drax.sendit.view.login

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import app.siamak.sendit.BuildConfig
import app.siamak.sendit.R
import com.drax.sendit.data.service.Analytics
import com.drax.sendit.data.service.Event
import com.drax.sendit.data.service.SenditFirebaseService
import com.drax.sendit.domain.network.model.SignInRequest
import com.drax.sendit.domain.network.model.type.UserSex
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
import java.time.Instant

class SsoHandler(
    private val analytics: Analytics,
    private val deviceInfoHelper: DeviceInfoHelper,
    private val onEvent: (SsoEvent) -> Unit,
) {

    private val tag = javaClass.canonicalName
    private lateinit var oneTapClient: SignInClient

    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var signUpRequest: BeginSignInRequest
    private lateinit var signInActivityResult: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var signupActivityResult: ActivityResultLauncher<IntentSenderRequest>

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var googleAuthCallback: ActivityResultLauncher<Intent>

    fun launchOneTapSignIn(activity: Activity) {
        oneTapClient = Identity.getSignInClient(activity)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
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
            .addOnSuccessListener(activity) { result ->
                analytics.set(Event.SignIn.SsoDone)
                try {
                    signInActivityResult.launch(
                        IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    )

                } catch (e: IntentSender.SendIntentException) {
                    onEvent(SsoEvent.SignInFailed(R.string.signin_google_error))
                    Log.e(tag, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(activity) { e ->
                analytics.set(Event.SignIn.SignInFlowFailed)
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                Log.d(tag, "Errorrrrr " + e.localizedMessage)
                showSignupFlow(activity)
            }
    }

    private fun showSignupFlow(activity: Activity) {
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
            .addOnSuccessListener(activity) { result ->
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
            .addOnFailureListener(activity) { e ->
                analytics.set(Event.SignIn.SignInFlowFailed)
                // No Google Accounts found. Just continue presenting the signed-out UI.
                Log.d(tag, e.localizedMessage)
                googleAuth()
            }
    }


    fun googleAuthInit(activity: FragmentActivity?) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestId()
            .requestIdToken(BuildConfig.ONETAP_CLIENT_ID)
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(activity ?: return, gso)
    }

    private fun googleAuth() {
        googleAuthCallback.launch(mGoogleSignInClient.signInIntent)
    }

    private fun checkResult(activity: Activity?, result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK && activity != null) {

            val credential = Identity.getSignInClient(activity)
                .getSignInCredentialFromIntent(result.data)
            val idToken = credential.googleIdToken

            when {
                idToken != null -> {
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
                else -> {
                    // Shouldn't happen.
                    Log.d("TAG", "No ID token or password!")
                    analytics.set(Event.SignIn.Failed("No ID token or password!"))
                    onEvent(SsoEvent.SignInFailed(R.string.signin_google_error))
                }
            }
        } else {
            analytics.set(Event.SignIn.Failed("Negative Result"))
            onEvent(SsoEvent.SignInFailed(R.string.signin_google_error))
        }
    }

    fun unregister() {
        signInActivityResult.unregister()
        signupActivityResult.unregister()
        googleAuthCallback.unregister()
    }

    fun register(fragment: Fragment) {
        signInActivityResult =
            fragment.registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
                checkResult(fragment.activity, it)
            }
        signupActivityResult =
            fragment.registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
                checkResult(fragment.activity, it)
            }
        googleAuthCallback =
            fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result: ActivityResult ->
                if (result.resultCode == AppCompatActivity.RESULT_OK && result.data != null) {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    handleSignInResult(task)
                }
            }
    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
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

    private fun GoogleSignInAccount.toSignInRequest(): SignInRequest? =
        email.ifNotNull(id, idToken) {
            SignInRequest(
                firstName = givenName ?: displayName ?: "User Name",
                lastName = familyName ?: displayName ?: "N/A",
                fullName = displayName ?: "N/A",
                email = email!!,
                sex = UserSex.UserSex_NONE,
                birthDate = Instant.now(),
                avatarUrl = photoUrl.toString(),
                deviceId = deviceInfoHelper.getId(),
                instanceId = id!!,
                tokenId = idToken!!
            )
        }

    private fun SignInCredential.toSignInRequest(
        instanceId: String,
        idToken: String
    ): SignInRequest = SignInRequest(
        firstName = givenName ?: displayName ?: "User Name",
        lastName = familyName ?: displayName ?: "N/A",
        fullName = displayName ?: "N/A",
        email = id,
        sex = UserSex.UserSex_NONE,
        birthDate = Instant.now(),
        avatarUrl = profilePictureUri.toString(),
        deviceId = deviceInfoHelper.getId(),
        instanceId = instanceId,
        tokenId = idToken
    )
}

sealed class SsoEvent {
    data class SignInFailed(val stringId: Int) : SsoEvent()
    data class SignSucceed(val request: SignInRequest?) : SsoEvent()
}