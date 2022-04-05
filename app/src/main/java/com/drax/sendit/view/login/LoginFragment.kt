package com.drax.sendit.view.login

import android.app.Activity
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.drax.sendit.BuildConfig
import com.drax.sendit.R
import com.drax.sendit.data.model.*
import com.drax.sendit.data.service.SenditFirebaseService
import com.drax.sendit.databinding.LoginFragmentBinding
import com.drax.sendit.domain.network.model.SignInRequest
import com.drax.sendit.domain.network.model.type.UserSex
import com.drax.sendit.view.base.BaseFragment
import com.drax.sendit.view.util.DeviceInfoHelper
import com.drax.sendit.view.util.modal
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Instant

class LoginFragment: BaseFragment<LoginFragmentBinding, LoginVM>(LoginFragmentBinding::inflate) {
    override val viewModel: LoginVM by viewModel()

    private val TAG = javaClass.canonicalName

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var signUpRequest: BeginSignInRequest
    private val signInActivityResult = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
        checkResult(it)
    }

    private val signupActivityResult = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
        checkResult(it)
    }

    private fun checkResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {

            val credential = Identity.getSignInClient(requireActivity()).getSignInCredentialFromIntent(result.data)
            val idToken = credential.googleIdToken

            when {
                idToken != null -> {
                    SenditFirebaseService.token({
                        viewModel.googleSignInFailed(R.string.signin_google_error)

                    }) {instanceId->

                        viewModel.login(
                            SignInRequest(
                                firstName = credential.givenName ?: "",
                                lastName = credential.familyName ?: "",
                                fullName = credential.displayName ?: "",
                                email = credential.id,
                                sex = UserSex.UserSex_NONE,
                                birthDate = Instant.now(),
                                avatarUrl = credential.profilePictureUri.toString(),
                                deviceId = DeviceInfoHelper.getId(requireContext()),
                                instanceId = instanceId,
                                tokenId = idToken
                            )
                        )
                    }

                    Log.d("TAG", "Got password.")
                }
                else -> {
                    // Shouldn't happen.
                    Log.d("TAG", "No ID token or password!")
                    viewModel.googleSignInFailed(R.string.signin_google_error)
                }
            }
        }else{
            viewModel.googleSignInFailed(R.string.signin_google_error)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInGoogle.setOnClickListener {
            launchOneTapSignIn()
        }

        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect {uiState->
                when(uiState){
                    is LoginUiState.LoginFailed ->
                        modal(ModalMessage.FromNetError(R.string.error_internal))
                    LoginUiState.Neutral -> Unit
                    LoginUiState.LoginSucceed -> Unit
                    is LoginUiState.GoogleSignInFailed -> modal(ModalMessage.FromNetError(uiState.message))
                    LoginUiState.Loading -> Unit
                }
            }
        }
    }

    private fun launchOneTapSignIn(){
        oneTapClient = Identity.getSignInClient(requireActivity())
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(BuildConfig.ONETAP_CLIENT_ID)
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()

        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(requireActivity()) { result ->
                try {
                    signInActivityResult.launch(
                        IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    )

                } catch (e: IntentSender.SendIntentException) {
                    viewModel.googleSignInFailed(R.string.signin_google_error)
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(requireActivity()) { e ->
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                Log.d(TAG, "Errorrrrr "+e.localizedMessage)
                showSignupFlow()
            }
    }

    private fun showSignupFlow() {
        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(BuildConfig.ONETAP_CLIENT_ID)
                    // Show all accounts on the device.
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .build()

        oneTapClient.beginSignIn(signUpRequest)
            .addOnSuccessListener(requireActivity()) { result ->
                try {
                    signupActivityResult.launch(
                        IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    )

                } catch (e: IntentSender.SendIntentException) {
                    viewModel.googleSignInFailed(R.string.signin_google_error)
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(requireActivity()) { e ->
                // No Google Accounts found. Just continue presenting the signed-out UI.
                viewModel.googleSignInFailed(R.string.signin_google_error_no_account)
                Log.d(TAG, e.localizedMessage)
            }
    }

    override fun onDetach() {
        super.onDetach()
        signInActivityResult.unregister()
        signupActivityResult.unregister()
    }
}