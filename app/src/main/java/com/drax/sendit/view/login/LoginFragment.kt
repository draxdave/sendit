package com.drax.sendit.view.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.WindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import app.siamak.sendit.BuildConfig
import app.siamak.sendit.R
import com.drax.sendit.data.model.ModalMessage
import com.drax.sendit.data.service.Event
import com.drax.sendit.domain.network.model.auth.sso.SignInSsoRequest
import com.drax.sendit.domain.network.model.auth.sso.SignInSsoResponse
import com.drax.sendit.view.base.BaseComposeFragment
import com.drax.sendit.view.theme.ComposeTheme
import com.drax.sendit.view.util.DeviceInfoHelper
import com.drax.sendit.view.util.isActive
import com.drax.sendit.view.util.modal
import com.drax.sendit.view.util.observe
import com.drax.sendit.view.util.rememberImeState
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseComposeFragment() {

    private val viewModel: LoginVM by viewModels()

    @Inject
    lateinit var deviceInfoHelper: DeviceInfoHelper

    private val ssoHandler: SsoHandler by lazy {
        SsoHandler(analytics = analytics, deviceInfoHelper = deviceInfoHelper) { event ->
            if (isActive()) return@SsoHandler

            when (event) {
                is SsoEvent.SignInFailed -> viewModel.googleSignInFailed(event.stringId)
                is SsoEvent.SignSucceed -> {
//                    tryLoginToServer(event.request)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            Toast.makeText(requireContext(), "onCreateView", Toast.LENGTH_SHORT).show()
            LoginScreen()
        }
    }

    @Composable
    private fun LoginScreen() {
        ComposeTheme {
            LoginContent()
        }
    }


    @Preview
    @Composable
    private fun LoginScreenPreview() {
        ComposeTheme {
            LoginContent()
        }
    }

    @Composable
    fun LoginContent() {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                VersionText(BuildConfig.VERSION_NAME)
            }
        ) { contentPadding ->
            val imeState = rememberImeState()
            val scrollState = rememberScrollState()

            LaunchedEffect(key1 = imeState.value) {
                if (imeState.value && scrollState.value > 0) {
                    scrollState.animateScrollTo(scrollState.maxValue, tween(300))
                }
            }

            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val config = LocalConfiguration.current
                val height = config.screenHeightDp * 0.3

                Image(
                    painter = painterResource(id = R.drawable.login_moving_bg),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height.dp),
                )

                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.h1,
                    modifier = Modifier
                )

                LoginForm(Modifier.fillMaxWidth(.85f))
            }
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

    @Composable
    fun VersionText(appVersion: String) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center

        ) {
            Text(
                modifier = Modifier,
                text = appVersion,
                color = MaterialTheme.colors.secondaryVariant,
                fontStyle = FontStyle.Italic,
                fontSize = MaterialTheme.typography.caption.fontSize,
            )
        }
    }/*
        @Preview(
            name = "Login Preview (Light)",
            showBackground = true,
            showSystemUi = true,
            backgroundColor = 0xFFCCCCCC,
            uiMode = Configuration.UI_MODE_NIGHT_NO
        )
        @Composable
        fun LoginPreviewLight() {
            LoginScreen()
        }*/


    private fun initUI() {
        setupListeners()
        setupUI()
//        setupObservers()
    }

    @Composable
    fun HeaderLayout(modifier: Modifier = Modifier) {
        Column(
            modifier = modifier
                .background(Color.LightGray)
                .fillMaxHeight()
        ) {
            Image(
                painter = painterResource(id = R.drawable.login_moving_bg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
            )

            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.h1,
                modifier = Modifier
                    .padding(16.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            )
        }
    }
//
//    @Preview
//    @Composable
//    fun HeaderPreview(){
//        HeaderLayout()
//    }

    /*private fun setupObservers(): Unit = with(binding) {
        viewModel.uiState.observe(viewLifecycleOwner) {
            binding.loadingLayout.isShowing = it == LoginUiState.Loading
        }
        BuildConfig.BASE_URL

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
        }*/

    private fun setupUI() {

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is LoginUiState.LoginFailed -> {
                    analytics.set(Event.SignIn.Failed("REQUEST FAILED"))
                    modal(
                        if (uiState.errorCode in 1..799) ModalMessage.FromNetError(uiState.errorCode)
                        else ModalMessage.Failed(
                            when (uiState.errorCode) {
                                SignInSsoResponse.DEVICE_IS_NOT_ACTIVE -> R.string.login_error_device_inactive
                                SignInSsoResponse.INCORRECT_CREDENTIALS -> R.string.login_error_user_pass_incorrect
                                SignInSsoResponse.USER_IS_NOT_ACTIVE -> R.string.login_error_user_inactive
                                SignInSsoResponse.USER_ALREADY_ACTIVE -> R.string.login_error_user_inactive
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
                LoginUiState.ForgetPasswordDone -> modal(ModalMessage.Success(R.string.login_forget_pass))
                LoginUiState.SignupDone -> modal(ModalMessage.Success(R.string.signup_done))
            }
        }
    }

    private fun setupListeners() {/*binding.tvBottomAction.setOnClickListener {
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

        */
    }

    private fun clearPasswordInputs() {
//        binding.etPassword.setText("")
//        binding.etConfirmPassword.setText("")
    }

    private fun handleSignup() {/*if (binding.emailTextField.isValid(
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
            if (binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString()) {
                binding.confirmPasswordTextField.apply {
                    requestFocus()
                    error = getString(R.string.login_form_error_password_equal)
                }
                return
            }


            val email = binding.etEmail.text.toString()
            val passwordHash = binding.etPassword.text.toString().md5()

            SenditFirebaseService.token(
                onError = {
                    analytics.set(Event.SignIn.Failed("Token is missing"))
                    toast(getString(R.string.signin_google_error))

                }) { instanceId ->
                clearPasswordInputs()
                viewModel.signupWithEmail(
                    email = email,
                    passwordHash = passwordHash,
                    instanceId = instanceId,
                    deviceId = deviceInfoHelper.getId()
                )
            }
        }*/
    }

    private fun handleSignin() {/*if (binding.emailTextField.isValid(
                Patterns.EMAIL_ADDRESS,
                R.string.login_form_error_email_input
            ) &&
            binding.passwordTextField.isValid(
                Pattern.compile(PASSWORD_REGEX),
                R.string.login_form_error_password_input
            )
        ) {

            val email = binding.etEmail.text.toString()
            val passwordHash = binding.etPassword.text.toString().md5()

            SenditFirebaseService.token(
                onError = {
                    analytics.set(Event.SignIn.Failed("Token is missing"))
                    toast(getString(R.string.signin_google_error))

                }) { instanceId ->
                clearPasswordInputs()
                viewModel.authoriseWithEmail(
                    email = email,
                    passwordHash = passwordHash,
                    instanceId = instanceId,
                    deviceId = deviceInfoHelper.getId()
                )
            }
        }*/
    }

    private fun handleForgot() {/*if (binding.emailTextField.isValid(
                Patterns.EMAIL_ADDRESS,
                R.string.login_form_error_email_input
            )
        ) {
            val email = binding.etEmail.text.toString()
            viewModel.forgetPassword(email)
        }*/
    }

    private fun trySso(signInRequest: SignInSsoRequest?) {
        viewModel.authoriseWithSso(signInRequest ?: return)
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
        private const val PASSWORD_REGEX = "^(?=.*[0-9]).{6,20}\$"
    }
}
