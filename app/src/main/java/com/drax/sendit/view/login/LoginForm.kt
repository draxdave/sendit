package com.drax.sendit.view.login

import android.util.Patterns
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import app.siamak.sendit.R
import com.drax.sendit.view.theme.aqua500
import java.util.regex.Pattern
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

private const val PASSWORD_REGEX = "^(?=.*[0-9]).{6,20}\$"

@Composable
fun AppEmailInput(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel,
    valueDelegate: MutableState<String>,
    colors: TextFieldColors,
    onValidationChange: (Boolean) -> Unit = {},
) {
    var username by valueDelegate
    var usernameHadError by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    if (!usernameHadError && username.isNotBlank()) {
        onValidationChange(true)
    } else {
        onValidationChange(false)
    }

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(7.dp)),
        value = username,
        label = {
            Text(
                text = stringResource(
                    id = if (usernameHadError) {
                        R.string.login_form_error_email_input
                    } else {
                        R.string.login_email_hint
                    }
                )
            )
        },
        placeholder = {
            Text(text = stringResource(id = R.string.login_email_placeholder))
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.login_input_username),
                contentDescription = null,
                modifier = Modifier
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        onValueChange = {
            scope.launch {
                usernameHadError = !viewModel.validateInput(
                    usernameInput = it,
                    pattern = Patterns.EMAIL_ADDRESS
                )
            }
            username = it
        },
        colors = colors,
        singleLine = true,
        isError = usernameHadError,
    )
}

@Composable
fun AppPasswordInput(
    viewModel: LoginViewModel,
    colors: TextFieldColors,
    valueDelegate: MutableState<String>,
    @StringRes label: Int,
    onValidationChange: (Boolean) -> Unit = {},
) {
    var password by valueDelegate
    var passwordHasError by rememberSaveable { mutableStateOf(false) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    if (!passwordHasError && password.isNotBlank()) {
        onValidationChange(true)
    } else {
        onValidationChange(false)
    }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(7.dp)
            ),
        singleLine = true,
        value = password,
        label = {
            Text(
                text = if (passwordHasError) {
                    stringResource(id = R.string.login_form_error_password_input)
                } else {
                    stringResource(id = label)
                }
            )
        },
        placeholder = {
            Text(text = stringResource(id = R.string.login_password_placeholder))
        },
        leadingIcon = {
            IconButton(onClick = {

            }) {
                Icon(
                    painter = painterResource(id = R.drawable.login_input_password),
                    ""
                )
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Outlined.Visibility
            else Icons.Filled.VisibilityOff

            // Please provide localized description for accessibility services
            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, description)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        onValueChange = {
            scope.launch {
                passwordHasError = !viewModel.validateInput(
                    usernameInput = it,
                    pattern = Pattern.compile(PASSWORD_REGEX)
                )
            }
            password = it
        },
        colors = colors,
        isError = passwordHasError,
    )
}

@Composable
fun LoginForm(
    modifier: Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
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
            viewModel = viewModel,
            colors = colors,
            valueDelegate = viewModel.usernameInput
        ) { isValid ->
            usernameIsValid = isValid
        }

        if (formType !is FormType.ForgetPassword)
            AppPasswordInput(
                label = R.string.login_password_placeholder,
                viewModel = viewModel, colors = colors,
                valueDelegate = viewModel.passwordInput,
            ) {
                passwordIsValid = it
            }

        if (formType is FormType.Register)
            AppPasswordInput(
                label = R.string.login_password_repeat_placeholder,
                viewModel = viewModel, colors = colors,
                valueDelegate = viewModel.passwordRepeatInput,
            ) {
                passwordRepeatIsValid = it
            }

        if (formType is FormType.Login)
            Text(
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

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .align(Alignment.CenterHorizontally),
            onClick = {
                formType = FormType.Register
                formState = FormState.Invalid
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.surface
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,

                ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_google_icon),
                    contentDescription = "Google icon"
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(alignment = Alignment.CenterVertically),
                    text = stringResource(id = R.string.sign_in_with_google),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.primary,
                )

            }
        }
    }
}

@Preview
@Composable
fun LoginPreview() {
    LoginForm(modifier = Modifier.background(color = Color.White), viewModel = viewModel())
}

@Serializable
sealed class FormType : java.io.Serializable {
    object Login : FormType()
    object Register : FormType()
    object ForgetPassword : FormType()
}

@Serializable
sealed class FormState : java.io.Serializable {
    object Loading : FormState()
    data class Error(
        val message: String? = null,
        @StringRes val messageResId: Int? = null,
        val iconResId: Int
    ) :
        FormState()

    object Valid : FormState()
    object Invalid : FormState()
    data class Success(@StringRes val messageRes: Int? = null) : FormState()
}

sealed class UiState {
    object Loading : UiState()
    data class Error(val message: String?) : UiState()
    data class Success(val message: String?) : UiState()
}
