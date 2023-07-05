package com.drax.sendit.view.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import app.siamak.sendit.R
import com.drax.sendit.view.login.components.AppEmailInput
import com.drax.sendit.view.login.components.AppPasswordInput
import com.drax.sendit.view.login.components.SsoButton
import com.drax.sendit.view.theme.aqua500

internal const val PASSWORD_REGEX = "^(?=.*[0-9]).{6,20}\$"


@Composable
fun LoginForm(
    modifier: Modifier,
    viewModel: LoginViewModel,
) {
    var formType by viewModel.formType
    var formState by viewModel.formState

    var usernameIsValid by rememberSaveable {
        mutableStateOf(false)
    }

    var passwordIsValid by rememberSaveable {
        mutableStateOf(false)
    }

    var passwordRepeatIsValid by rememberSaveable {
        mutableStateOf(false)
    }

    when (formState) {
        FormState.Loading -> {}
        is FormState.Error -> Unit
        FormState.Invalid -> when (formType) {
            FormType.ForgetPassword -> {
                if (usernameIsValid) {
                    formState = FormState.Valid
                }
            }

            FormType.Login -> {
                if (usernameIsValid && passwordIsValid) {
                    formState = FormState.Valid
                }
            }

            FormType.Register -> {
                if (usernameIsValid && passwordIsValid && passwordRepeatIsValid) {
                    formState = FormState.Valid
                }
            }
        }

        is FormState.Success -> Unit
        FormState.Valid -> when (formType) {
            FormType.ForgetPassword -> {
                if (!usernameIsValid) {
                    formState = FormState.Invalid
                }
            }

            FormType.Login -> {
                if (!usernameIsValid || !passwordIsValid) {
                    formState = FormState.Invalid
                }
            }

            FormType.Register -> {
                if (!usernameIsValid || !passwordIsValid || !passwordRepeatIsValid) {
                    formState = FormState.Invalid
                }
            }
        }
    }

    println("formState: $formState")
    println("formType: $formType")
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Unspecified,
            focusedLabelColor = Color.Unspecified,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Unspecified,

            )


        AppEmailInput(
            viewModel = viewModel, colors = colors, valueDelegate = viewModel.usernameInput
        ) { isValid ->
            usernameIsValid = isValid
        }

        if (formType !is FormType.ForgetPassword) AppPasswordInput(
            label = R.string.login_password_placeholder,
            viewModel = viewModel, colors = colors,
            valueDelegate = viewModel.passwordInput,
        ) {
            passwordIsValid = it
        }

        if (formType is FormType.Register) AppPasswordInput(
            label = R.string.login_password_repeat_placeholder,
            viewModel = viewModel, colors = colors,
            valueDelegate = viewModel.passwordRepeatInput,
        ) {
            passwordRepeatIsValid = it
        }

        if (formType is FormType.Login) Text(
            modifier = Modifier
                .padding(start = 8.dp)
                .drawBehind {
                    val strokeWidth = size.width
                    val strokeHeightStart = size.height - 1f

                    drawLine(
                        color = aqua500,
                        start = Offset(x = 0f, y = strokeHeightStart),
                        end = Offset(x = strokeWidth, y = size.height),
                        strokeWidth = 1f,
                    )
                }
                .clickable {
                    formType = FormType.ForgetPassword
                    formState = FormState.Invalid
                },
            text = stringResource(id = R.string.login_bottom_action_forgot),
            color = MaterialTheme.colors.secondary,
            style = MaterialTheme.typography.body2,
        )

        (formState as? FormState.Error)?.let {
            Text(
                modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                text = it.message ?: stringResource(
                    id = it.messageResId ?: R.string.login_form_error
                ),
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.error,
            )
        }

        (formState as? FormState.Success)?.let {
            Text(
                modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                text = stringResource(
                    id = it.messageRes ?: R.string.login_form_success
                ),
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.error,
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth(.8f)
                .padding(vertical = 8.dp)
                .align(Alignment.CenterHorizontally),
            onClick = {
                when (formType) {
                    FormType.ForgetPassword -> viewModel.forgetPassword()
                    FormType.Login -> viewModel.loginWithEmail()
                    FormType.Register -> viewModel.register()
                }
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primaryVariant,
                disabledBackgroundColor = MaterialTheme.colors.primaryVariant.copy(alpha = 0.5f),
            ),
            enabled = formState !in listOf(FormState.Invalid, FormState.Loading),
        ) {
            Text(
                text = stringResource(
                    id = when (formType) {
                        is FormType.Login -> R.string.login_submit_signin
                        is FormType.Register -> R.string.login_submit_signup
                        is FormType.ForgetPassword -> R.string.login_submit_forget
                    }
                ),
            )
        }

        Text(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .align(Alignment.CenterHorizontally)
                .drawBehind {
                    val strokeWidth = size.width
                    val boxHeight = size.height
                    val strokeHeight = (size.height) - 1

                    drawLine(
                        color = Color.DarkGray,
                        start = Offset(x = 0f, y = strokeHeight),
                        end = Offset(x = strokeWidth, y = boxHeight),
                        strokeWidth = 1f
                    )
                }
                .clickable {
                    formType = when (formType) {
                        FormType.Login -> FormType.Register
                        FormType.Register, FormType.ForgetPassword -> FormType.Login
                    }
                    formState = FormState.Invalid
                },
            text = stringResource(
                id = when (formType) {
                    FormType.Login -> R.string.login_bottom_action_signup
                    FormType.ForgetPassword, FormType.Register -> R.string.login_submit_signin
                }
            ),
            style = MaterialTheme.typography.button,
        )

        SsoButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            viewModel = viewModel,
        )
    }
}


@Preview
@Composable
fun LoginPreview() {
    LoginForm(
        modifier = Modifier.background(color = Color.White),
        viewModel = viewModel(),
    )
}
