package com.drax.sendit.view.login.components

import android.util.Patterns
import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import app.siamak.sendit.R
import com.drax.sendit.view.login.LoginViewModel
import com.drax.sendit.view.login.PASSWORD_REGEX
import java.util.regex.Pattern
import kotlinx.coroutines.launch


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