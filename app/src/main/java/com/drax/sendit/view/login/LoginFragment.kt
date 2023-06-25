package com.drax.sendit.view.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import app.siamak.sendit.BuildConfig
import app.siamak.sendit.R
import com.drax.sendit.view.base.BaseComposeFragment
import com.drax.sendit.view.theme.ComposeTheme
import com.drax.sendit.view.util.rememberImeState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseComposeFragment() {

    val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
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

                LoginForm(
                    modifier = Modifier.fillMaxWidth(.85f),
                    viewModel = viewModel,
                )
            }
        }
    }

    @Composable
    fun VersionText(appVersion: String) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center

        ) {
            Text(
                modifier = Modifier,
                text = appVersion,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                fontStyle = FontStyle.Italic,
                fontSize = MaterialTheme.typography.caption.fontSize,
            )
        }
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


    @Preview
    @Composable
    private fun VersionTextPreview() {
        ComposeTheme {
            VersionText(appVersion = "1.0.2")
        }
    }

}
