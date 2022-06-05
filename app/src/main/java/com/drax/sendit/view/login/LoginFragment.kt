package com.drax.sendit.view.login

import android.app.Activity
import android.content.IntentSender
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import com.drax.sendit.BuildConfig
import com.drax.sendit.R
import com.drax.sendit.data.model.ModalMessage
import com.drax.sendit.data.service.SenditFirebaseService
import com.drax.sendit.databinding.LoginFragmentBinding
import com.drax.sendit.domain.network.model.SignInRequest
import com.drax.sendit.domain.network.model.SignInResponse
import com.drax.sendit.domain.network.model.type.UserSex
import com.drax.sendit.view.base.BaseFragment
import com.drax.sendit.view.util.DeviceInfoHelper
import com.drax.sendit.view.util.collect
import com.drax.sendit.view.util.modal
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import kotlinx.coroutines.isActive
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
                                firstName = credential.givenName ?: credential.displayName ?: "User Name",
                                lastName = credential.familyName ?: credential.displayName ?: "N/A",
                                fullName = credential.displayName ?: "N/A",
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

        collect(viewModel.uiState) {uiState->
                when(uiState){
                    is LoginUiState.LoginFailed -> modal(
                        if (uiState.errorCode in 1..799)
                            ModalMessage.FromNetError(uiState.errorCode)
                        else
                            ModalMessage.Failed(
                                when(uiState.errorCode){
                                    SignInResponse.DEVICE_IS_NOT_ACTIVE -> R.string.login_error_device_inactive
                                    SignInResponse.USER_IS_NOT_ACTIVE -> R.string.login_error_user_inactive
                                    else -> R.string.error_internal
                                }
                            ))
                    LoginUiState.Neutral -> Unit
                    LoginUiState.LoginSucceed -> Unit
                    is LoginUiState.GoogleSignInFailed -> modal(ModalMessage.FromNetError(uiState.message))
                    LoginUiState.Loading -> Unit
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

    override fun onStart() {
        super.onStart()
        startRocketAnimation()
    }

    private fun startRocketAnimation(){

        val animation = AppCompatResources.getDrawable(requireContext(), R.drawable.login_rocket_animated) as AnimatedVectorDrawable

        binding.rocketAnimated.setImageDrawable(animation)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            animation.registerAnimationCallback(object : Animatable2.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable?) {
                    super.onAnimationEnd(drawable)
                    if (lifecycleScope.isActive && !this@LoginFragment.isDetached)
                        if(viewModel.uiState.value == LoginUiState.LoginSucceed)
                            startRocketPreLaunchingAnimation()
                        else
                            startRocketAnimation()
                }
            })
        }
        animation.start()
    }

    private fun startRocketPreLaunchingAnimation(){
        stopRocketAnimation()
        val animation = AppCompatResources.getDrawable(requireContext(), R.drawable.rocket_pre_luaching) as AnimatedVectorDrawable

        binding.rocketAnimated.setImageDrawable(animation)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            animation.registerAnimationCallback(object : Animatable2.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable?) {
                    super.onAnimationEnd(drawable)
                    if (lifecycleScope.isActive && !this@LoginFragment.isDetached) {
                        startRocketLaunchingAnimation()
                        animateUp()
                    }
                }
            })
        }
        animation.start()
    }
    private fun animateUp(){
        binding.rocketAnimated.animate()
            .setDuration(2000)
            .translationY(binding.root.height * -1f)
            .setInterpolator(AccelerateInterpolator())
            .start()
    }

    private fun startRocketLaunchingAnimation(){
        val animation = AppCompatResources.getDrawable(requireContext(), R.drawable.rocket_launching) as AnimatedVectorDrawable

        binding.rocketAnimated.setImageDrawable(animation)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            animation.registerAnimationCallback(object : Animatable2.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable?) {
                    super.onAnimationEnd(drawable)
                    if (lifecycleScope.isActive && !this@LoginFragment.isDetached)
                        startRocketLaunchingAnimation()
                }
            })
        }
        animation.start()
    }

    private fun stopRocketAnimation(){
        binding.rocketAnimated.drawable.takeIf { it is AnimatedVectorDrawable }?.let { it as AnimatedVectorDrawable
            it.stop()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                it.clearAnimationCallbacks()
            }
        }
    }

    override fun onDestroy() {
        stopRocketAnimation()
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
        signInActivityResult.unregister()
        signupActivityResult.unregister()
    }
}