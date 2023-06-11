package com.drax.sendit.view.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.siamak.sendit.R


@Composable
fun LoginForm(modifier: Modifier) {
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

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(7.dp)),
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

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(7.dp)),
            singleLine = true,
            value = passwordRepeat,
            label = {
                Text(text = stringResource(id = R.string.login_password_hint))
            },
            placeholder = {
                Text(text = stringResource(id = R.string.login_password_repeat_placeholder))
            },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.login_input_password),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                )
            },
            visualTransformation = if (passwordRepeatVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordRepeatVisible)
                    Icons.Outlined.Visibility
                else Icons.Filled.VisibilityOff

                // Please provide localized description for accessibility services
                val description = if (passwordRepeatVisible) "Hide password" else "Show password"

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

        Text(
            modifier = Modifier,
            text = stringResource(id = R.string.login_bottom_action_forgot),
            color = MaterialTheme.colors.primaryVariant,
            fontStyle = FontStyle.Italic,
            fontSize = MaterialTheme.typography.caption.fontSize,
        )
    }
}

@Preview
@Composable
fun LoginPreview() {
    LoginForm(modifier = Modifier.background(color = Color.White))
}