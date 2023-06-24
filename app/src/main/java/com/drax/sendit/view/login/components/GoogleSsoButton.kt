package com.drax.sendit.view.login.components

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.siamak.sendit.BuildConfig
import app.siamak.sendit.R
import com.drax.sendit.view.login.FormState
import com.drax.sendit.view.login.LoginViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@Preview
@Composable
fun PreviewGoogleSso() {
    SsoButton(
        modifier = Modifier,
        viewModel = viewModel()
    )
}

@Composable
fun SsoButton(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel,
) {
    val context = LocalContext.current
    var formState by viewModel.formState

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) {
            // The user cancelled the login, was it due to an Exception?
            if (result.data?.action == ActivityResultContracts.StartIntentSenderForResult.ACTION_INTENT_SENDER_REQUEST) {
                val exception: Exception? = result.data?.getSerializableExtra(
                    ActivityResultContracts.StartIntentSenderForResult.EXTRA_SEND_INTENT_EXCEPTION
                ) as Exception?
                Log.e("LOG", "Couldn't start One Tap UI: ${exception?.localizedMessage}")
            }
            formState = FormState.Error(
                messageResId = R.string.signin_google_error,
                iconResId = R.drawable.warning
            )
            return@rememberLauncherForActivityResult
        }
        val oneTapClient = Identity.getSignInClient(context)
        val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
        val idToken = credential.googleIdToken
        if (idToken != null) {
            // Got an ID token from Google. Use it to authenticate
            // with your backend.
            Log.d("LOG", idToken)

            viewModel.loginWithGoogle(idToken)

        } else {
            Log.d("LOG", "Null Token")
            formState = FormState.Error(
                messageResId = R.string.signin_google_error,
                iconResId = R.drawable.warning
            )
        }
    }
    val scope = rememberCoroutineScope()

    Button(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        onClick = {
            formState = FormState.Loading
            scope.launch {
                signInWithGoogle(
                    context = context,
                    launcher = launcher,
                    formState = viewModel.formState,
                )
            }
        }, colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.surface
        ), shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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

suspend fun signInWithGoogle(
    context: Context,
    launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
    formState: MutableState<FormState>,
) {

    val oneTapClient: SignInClient by lazy {
        Identity.getSignInClient(context)
    }
    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                // Your server's client ID, not your Android client ID.
                .setServerClientId(BuildConfig.ONETAP_CLIENT_ID)
                // Only show accounts previously used to sign in.
                .setFilterByAuthorizedAccounts(true)
                .build()
        )
        // Automatically sign in when exactly one credential is retrieved.
        .setAutoSelectEnabled(true)
        .build()

    try {
        val result = oneTapClient.beginSignIn(signInRequest).await()
        launcher.launch(
            IntentSenderRequest.Builder(result.pendingIntent).build()
        )
    } catch (e: Exception) {
//        onEvent(SsoEvent.SignInFailed(R.string.signin_google_error))
        Log.d("GoogleSSO", "Couldn't start One Tap UI: ${e.localizedMessage}")
        signUpWithGoogle(context = context, launcher = launcher, formState = formState)
    }
}

suspend fun signUpWithGoogle(
    context: Context,
    launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
    formState: MutableState<FormState>,
) {

    val oneTapClient: SignInClient by lazy {
        Identity.getSignInClient(context)
    }
    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                // Your server's client ID, not your Android client ID.
                .setServerClientId(BuildConfig.ONETAP_CLIENT_ID)
                // Show all accounts on the device.
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .build()

    try {
        val result = oneTapClient.beginSignIn(signInRequest).await()
        launcher.launch(
            IntentSenderRequest.Builder(result.pendingIntent).build()
        )
    } catch (e: Exception) {
        Log.d("GoogleSSO", "Couldn't start One Tap UI: ${e.localizedMessage}")
        formState.value = FormState.Error(
            messageResId = R.string.signin_google_error,
            iconResId = R.drawable.warning
        )
    }
}
