package com.drax.sendit.view.login

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import app.siamak.sendit.BuildConfig
import app.siamak.sendit.R
import app.siamak.sendit.databinding.LoginFragmentBinding
import com.drax.sendit.data.model.ModalMessage
import com.drax.sendit.data.service.Event
import com.drax.sendit.domain.network.model.SignInRequest
import com.drax.sendit.domain.network.model.SignInResponse
import com.drax.sendit.view.base.BaseFragment
import com.drax.sendit.view.util.DeviceInfoHelper
import com.drax.sendit.view.util.isActive
import com.drax.sendit.view.util.modal
import com.drax.sendit.view.util.observe
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : BaseFragment<LoginFragmentBinding, LoginVM>(LoginFragmentBinding::inflate) {

    override val viewModel: LoginVM by viewModel()
    private val deviceInfoHelper: DeviceInfoHelper by inject()

    private val ssoHandler: SsoHandler = SsoHandler(analytics, deviceInfoHelper) { event ->
        if (!isActive()) return@SsoHandler

        when (event) {
            is SsoEvent.SignInFailed -> viewModel.googleSignInFailed(event.stringId)
            is SsoEvent.SignSucceed -> tryLoginToServer(event.request)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ssoHandler.register(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ssoHandler.googleAuthInit(activity)
        initUI()
    }

    private fun initUI() {
        setupListeners()
        setupUI()
    }

    private fun setupUI() {
        binding.signInGoogle.setOnClickListener {
            analytics.set(Event.View.Clicked.SigninWithGoogle)
            ssoHandler.launchOneTapSignIn(activity ?: return@setOnClickListener)
        }

        RocketAnimationHandler(binding.rocketAnimated, lifecycle, viewModel.uiState)
            .startAnimation()
    }

    private fun setupListeners() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is LoginUiState.LoginFailed -> {
                    analytics.set(Event.SignIn.Failed("REQUEST FAILED"))
                    modal(
                        if (uiState.errorCode in 1..799)
                            ModalMessage.FromNetError(uiState.errorCode)
                        else
                            ModalMessage.Failed(
                                when (uiState.errorCode) {
                                    SignInResponse.DEVICE_IS_NOT_ACTIVE -> R.string.login_error_device_inactive
                                    SignInResponse.USER_IS_NOT_ACTIVE -> R.string.login_error_user_inactive
                                    else -> R.string.error_internal
                                }
                            )
                    )
                }
                LoginUiState.Neutral -> Unit
                LoginUiState.LoginSucceed -> analytics.set(Event.SignIn.Succeed)
                is LoginUiState.GoogleSignInFailed -> modal(ModalMessage.FromNetError(uiState.message))
                LoginUiState.Loading -> Unit
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    analytics.set(Event.SignIn.LeftSignIn)
                    this.remove()
                }
            })
    }

    private fun tryLoginToServer(signInRequest: SignInRequest?) {
        viewModel.login(signInRequest ?: return)
    }

    override fun onDetach() {
        super.onDetach()
        ssoHandler.unregister()
    }
}
