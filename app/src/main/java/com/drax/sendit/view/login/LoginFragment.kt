package com.drax.sendit.view.login

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import app.siamak.sendit.BuildConfig
import app.siamak.sendit.R
import app.siamak.sendit.databinding.LoginFragmentBinding
import com.drax.sendit.data.model.ModalMessage
import com.drax.sendit.data.service.Event
import com.drax.sendit.data.service.SenditFirebaseService
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import java.time.Instant
import kotlinx.coroutines.isActive
import org.koin.androidx.viewmodel.ext.android.viewModel


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
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val googleAuthCallback =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == AppCompatActivity.RESULT_OK && result.data != null) {
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignInResult(task)
            }
        }

    private fun checkResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {

            val credential = Identity.getSignInClient(requireActivity()).getSignInCredentialFromIntent(result.data)
            val idToken = credential.googleIdToken

            when {
                idToken != null -> {
                    SenditFirebaseService.token({
                        analytics.set(Event.SignIn.Failed("Token is missing"))
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
                    analytics.set(Event.SignIn.Failed("No ID token or password!"))
                    viewModel.googleSignInFailed(R.string.signin_google_error)
                }
            }
        } else {
            analytics.set(Event.SignIn.Failed("Negative Result"))
            viewModel.googleSignInFailed(R.string.signin_google_error)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInGoogle.setOnClickListener {
            analytics.set(Event.View.Clicked.SigninWithGoogle)
            launchOneTapSignIn()
        }

        collect(viewModel.uiState) {uiState->
                when(uiState){
                    is LoginUiState.LoginFailed -> {
                        analytics.set(Event.SignIn.Failed("REQUEST FAILED"))
                        modal(
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
                    }
                    LoginUiState.Neutral -> Unit
                    LoginUiState.LoginSucceed -> analytics.set(Event.SignIn.Succeed)
                    is LoginUiState.GoogleSignInFailed -> modal(ModalMessage.FromNetError(uiState.message))
                    LoginUiState.Loading -> Unit
                }
            }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                analytics.set(Event.SignIn.LeftSignIn)
                this.remove()
            }
        })
        googleAuthInit()
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
                analytics.set(Event.SignIn.SsoDone)
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
                analytics.set(Event.SignIn.SignInFlowFailed)
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                Log.d(TAG, "Errorrrrr "+e.localizedMessage)
                showSignupFlow()
            }
    }

    private fun showSignupFlow() {
        analytics.set(Event.SignIn.SignUpFlowStarted)
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
                analytics.set(Event.SignIn.SsoDone)
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
                analytics.set(Event.SignIn.SignInFlowFailed)
                // No Google Accounts found. Just continue presenting the signed-out UI.
                Log.d(TAG, e.localizedMessage)
                googleAuth()
            }
    }

    private fun googleAuthInit(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestId()
            .requestIdToken(BuildConfig.ONETAP_CLIENT_ID)
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }
    private fun googleAuth(){
        googleAuthCallback.launch(mGoogleSignInClient.signInIntent)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val credential: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            loginApi(credential)

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            viewModel.googleSignInFailed(R.string.signin_google_error_no_account)
        }
    }

private fun loginApi(credential: GoogleSignInAccount) {
    if (credential.email!=null)
    viewModel.login(
        SignInRequest(
            firstName = credential.givenName ?: credential.displayName ?: "User Name",
            lastName = credential.familyName ?: credential.displayName ?: "N/A",
            fullName = credential.displayName ?: "N/A",
            email = credential.email ?: return,
            sex = UserSex.UserSex_NONE,
            birthDate = Instant.now(),
            avatarUrl = credential.photoUrl.toString(),
            deviceId = DeviceInfoHelper.getId(requireContext()),
            instanceId = credential.id ?: "",
            tokenId = credential.idToken ?: ""
        )
    )
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
                        if(viewModel.uiState.value == LoginUiState.LoginSucceed) {
                            startRocketPreLaunchingAnimation()
                        } else {
                            startRocketAnimation()
                        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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