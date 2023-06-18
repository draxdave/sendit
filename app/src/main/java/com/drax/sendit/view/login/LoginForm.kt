package com.drax.sendit.view.login

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
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.lifecycle.viewmodel.compose.viewModel
import app.siamak.sendit.R
import com.drax.sendit.view.theme.aqua500


@Composable
fun LoginForm(
    modifier: Modifier,
    viewModel: LoginViewModel = viewModel()
) {
    val formState by viewModel.formState


    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        var username by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }
        var passwordRepeat by rememberSaveable { mutableStateOf("") }
        var passwordVisible by rememberSaveable { mutableStateOf(false) }
        var passwordRepeatVisible by rememberSaveable { mutableStateOf(false) }
        val colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Unspecified,
            focusedLabelColor = Color.Unspecified,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Unspecified,

            )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(7.dp)),
            value = username,
            label = {
                Text(text = stringResource(id = R.string.login_email_hint))
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
                username = it
            },
            colors = colors,
            singleLine = true,
        )

        if (formState !is FormState.ForgetPassword)
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
                    Text(text = stringResource(id = R.string.login_password_hint))
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
                    password = it
                },
                colors = colors,
            )

        if (formState is FormState.Register)
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
                value = passwordRepeat,
                label = {
                    Text(text = stringResource(id = R.string.login_password_repeat_placeholder))
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.login_password_repeat_placeholder))
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
                visualTransformation = if (passwordRepeatVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordRepeatVisible)
                        Icons.Outlined.Visibility
                    else Icons.Filled.VisibilityOff

                    // Please provide localized description for accessibility services
                    val description =
                        if (passwordRepeatVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordRepeatVisible = !passwordRepeatVisible }) {
                        Icon(imageVector = image, description)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = {
                    passwordRepeat = it
                },
                colors = colors
            )

        if (formState is FormState.Login)
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
                        viewModel.updateFormState(FormState.ForgetPassword)
                    },
                text = stringResource(id = R.string.login_bottom_action_forgot),
                color = MaterialTheme.colors.secondary,
                style = MaterialTheme.typography.body2,
            )

        Button(
            modifier = Modifier
                .fillMaxWidth(.8f)
                .padding(vertical = 8.dp)
                .align(Alignment.CenterHorizontally),
            onClick = {

            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primaryVariant,
            )
        ) {
            Text(
                text = stringResource(
                    id = when (formState) {
                        is FormState.Login -> R.string.login_submit_signin
                        is FormState.Register -> R.string.login_submit_signup
                        is FormState.ForgetPassword -> R.string.login_submit_forget
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
                    viewModel.updateFormState(
                        when (formState) {
                            FormState.Login -> FormState.Register
                            FormState.Register, FormState.ForgetPassword -> FormState.Login
                        }
                    )
                },
            text = stringResource(
                id = when (formState) {
                    FormState.Login -> R.string.login_bottom_action_signup
                    FormState.ForgetPassword, FormState.Register -> R.string.login_submit_signin
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
                viewModel.updateFormState(FormState.Register)
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
    LoginForm(modifier = Modifier.background(color = Color.White))
}

sealed class FormState {
    object Login : FormState()
    object Register : FormState()
    object ForgetPassword : FormState()
}

sealed class UiState {
    object Loading : UiState()
    data class Error(val message: String?) : UiState()
    data class Success(val message: String?) : UiState()
}
