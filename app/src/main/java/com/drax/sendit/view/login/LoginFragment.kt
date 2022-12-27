package com.drax.sendit.view.login

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import app.siamak.sendit.BuildConfig
import app.siamak.sendit.R
import app.siamak.sendit.databinding.LoginFragmentBinding
import com.drax.sendit.data.model.ModalMessage
import com.drax.sendit.data.service.Event
import com.drax.sendit.domain.network.model.SignInRequest
import com.drax.sendit.domain.network.model.SignInResponse
import com.drax.sendit.view.base.BaseVBFragment
import com.drax.sendit.view.util.DeviceInfoHelper
import com.drax.sendit.view.util.isActive
import com.drax.sendit.view.util.modal
import com.drax.sendit.view.util.observe
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : BaseVBFragment<LoginFragmentBinding, LoginVM>(LoginFragmentBinding::inflate) {

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
        setupObservers()
    }

    private fun setupObservers(): Unit = with(binding) {
        viewModel.uiState.observe(viewLifecycleOwner) {
            binding.loadingLayout.isShowing = it == LoginUiState.Loading
        }

        viewModel.signinFormState.observe(viewLifecycleOwner) { formState ->
            when (formState) {
                SigninFormState.Forgot -> {
                    tvBottomAction.setText(R.string.login_bottom_action_signin)
                    submitButton.setText(R.string.login_submit_forgot)
                    passwordTextField.isVisible = false
                    tvForgotAction.isVisible = false
                    confirmPasswordTextField.isVisible = false
                }
                SigninFormState.Signin -> {
                    tvBottomAction.setText(R.string.login_bottom_action_signup)
                    submitButton.setText(R.string.login_submit_signin)
                    passwordTextField.isVisible = true
                    tvForgotAction.isVisible = true
                    confirmPasswordTextField.isVisible = false
                }
                SigninFormState.Signup -> {
                    tvBottomAction.setText(R.string.login_bottom_action_signin)
                    submitButton.setText(R.string.login_submit_signup)
                    passwordTextField.isVisible = true
                    tvForgotAction.isVisible = false
                    confirmPasswordTextField.isVisible = true
                }
                SigninFormState.FormHidden -> {
                    passwordTextField.isVisible = false
                    tvForgotAction.isVisible = false
                    confirmPasswordTextField.isVisible = false
                    emailTextField.isVisible = false
                    submitButton.isVisible = false
                    tvBottomAction.isVisible = false
                    signInGoogle.isVisible = false
                }
            }
        }

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
                LoginUiState.LoginSucceed -> {
                    analytics.set(Event.SignIn.Succeed)
                    viewModel.updateSigninFormState(SigninFormState.FormHidden)
                }
                is LoginUiState.GoogleSignInFailed -> modal(ModalMessage.FromNetError(uiState.message))
                LoginUiState.Loading -> Unit
            }
        }
    }

    private fun setupUI() {
        binding.versionText.text = viewModel.versionText
        binding.tvForgotAction.paint.isUnderlineText = true

        RocketAnimationHandler(binding.rocketAnimated, lifecycle, viewModel.uiState)
            .startAnimation()
    }

    private fun setupListeners() {
        binding.tvBottomAction.setOnClickListener {
            viewModel.updateSigninFormState(
                if (binding.tvBottomAction.text.equals(getString(R.string.login_bottom_action_signin))) {
                    SigninFormState.Signin
                } else {
                    SigninFormState.Signup
                }
            )
        }
        binding.signInGoogle.setOnClickListener {
            analytics.set(Event.View.Clicked.SigninWithGoogle)
            ssoHandler.launchOneTapSignIn(activity ?: return@setOnClickListener)
        }
        binding.tvForgotAction.setOnClickListener {
            viewModel.updateSigninFormState(SigninFormState.Forgot)
        }
        binding.submitButton.setOnClickListener {
            when (viewModel.signinFormState.value) {
                SigninFormState.Forgot -> handleForgot()
                SigninFormState.FormHidden -> Unit
                SigninFormState.Signin -> handleSignin()
                SigninFormState.Signup -> handleSignup()
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

    private fun handleSignup() {
        if (binding.emailTextField.isValid(
                Patterns.EMAIL_ADDRESS,
                R.string.login_form_error_email_input
            ) &&
            binding.passwordTextField.isValid(
                Pattern.compile(PASSWORD_REGEX),
                R.string.login_form_error_password_input
            ) &&
            binding.confirmPasswordTextField.isValid(
                Pattern.compile(PASSWORD_REGEX),
                R.string.login_form_error_password_input
            )
        ) {
            if (binding.etPassword.text != binding.etConfirmPassword.text) {
                binding.confirmPasswordTextField.apply {
                    requestFocus()
                    error = getString(R.string.login_form_error_password_equal)
                }
                return
            }

            Toast.makeText(context, "handleSignup", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleSignin() {
        if (binding.emailTextField.isValid(
                Patterns.EMAIL_ADDRESS,
                R.string.login_form_error_email_input
            ) &&
            binding.passwordTextField.isValid(
                Pattern.compile(PASSWORD_REGEX),
                R.string.login_form_error_password_input
            )
        ) {
            Toast.makeText(context, "sign in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleForgot() {
        if (binding.emailTextField.isValid(
                Patterns.EMAIL_ADDRESS,
                R.string.login_form_error_email_input
            )
        ) {
            Toast.makeText(context, "handleForgot", Toast.LENGTH_SHORT).show()
        }
    }

    private fun tryLoginToServer(signInRequest: SignInRequest?) {
        viewModel.login(signInRequest ?: return)
    }

    override fun onDetach() {
        super.onDetach()
        ssoHandler.unregister()
    }

    private fun TextInputLayout.isValid(pattern: Pattern, errorText: Int): Boolean {
        return if (editText?.text?.matches(pattern.toRegex()) == true) {
            isErrorEnabled = false
            true
        } else {
            error = getString(errorText)
            requestFocus()
            false
        }
    }

    companion object {
        private const val PASSWORD_REGEX =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~\$^+=<>]).{8,20}\$"
    }
}
