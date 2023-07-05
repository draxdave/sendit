package com.drax.sendit.view.qr

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import app.siamak.sendit.R
import com.drax.sendit.data.model.ModalMessage
import com.drax.sendit.data.service.Event
import com.drax.sendit.view.base.BaseComposeFragment
import com.drax.sendit.view.qr.components.QrDisplayBanner
import com.drax.sendit.view.qr.components.QrScanButton
import com.drax.sendit.view.scanner.ScannerFragment
import com.drax.sendit.view.util.allPermissionsGranted
import com.drax.sendit.view.util.modal
import dagger.hilt.android.AndroidEntryPoint
import ir.drax.modal.Modal
import ir.drax.modal.model.MoButton

@AndroidEntryPoint
class QrFragment : BaseComposeFragment() {
    val viewModel: QrVM by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                launchScanner()
            } else {
                analytics.set(Event.QR.CameraPermissionRejected)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            QrScreen()
        }
    }

    @Composable
    fun QrScreen() {
        val uiState by viewModel.uiState.collectAsState()
        val qrState by viewModel.qrState
        val qrPairState by viewModel.qrPairState


        val annotatedString = buildAnnotatedString {
            append(stringResource(id = R.string.qr_help_desc))
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary
                )
            ) {
                append(stringResource(id = R.string.learn_more))
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                QrDisplayBanner(
                    modifier = Modifier.padding(vertical = 24.dp),
                    uiState = qrState,
                )

                Text(
                    text = annotatedString,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(24.dp),
                )

                QrScanButton(
                    modifier = Modifier
                        .padding(24.dp),
                    uiState = uiState,
                ) {
                    checkPermissionAndLaunchScanner()
                }
            }

            if (uiState is QrUiState.Loading)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background.copy(alpha = 0.3f))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center,

                    ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .background(MaterialTheme.colors.background.copy(alpha = 0.3f)),
                        color = MaterialTheme.colors.primary,
                    )

                }
        }


        when (qrPairState) {

            is QrPairState.PairDone -> {
                analytics.set(Event.QR.InvitationSent)
                modal(
                    ModalMessage.Full(
                        mTitle = R.string.invitation_sent_title,
                        mDescription = R.string.invitation_sent_desc,
                        mIcon = R.drawable.tick,
                        mFromTop = true,
                        mLock = false
                    )
                )
            }

            is QrPairState.PairFailed -> {
                analytics.set(Event.QR.ReceivedInvitationFailedToSend)
                (qrPairState as? QrPairState.PairFailed)?.let {
                    modal(ModalMessage.FromNetError(it.reason.errorCode))
                }
            }

            QrPairState.ConnectionAlreadyActive -> modal(ModalMessage.Neutral(R.string.invitation_already_active))
            QrPairState.RequestRejected -> {
                modal(ModalMessage.Neutral(R.string.invitation_rejected))
            }

            QrPairState.InvitationResponseWaiting ->
                modal(ModalMessage.Neutral(R.string.invitation_waiting_for_peer))

            QrPairState.Neutral -> Unit
        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun PreviewQrScreen() {
        QrScreen()
    }

    private fun checkPermissionAndLaunchScanner() {
        when {
            allPermissionsGranted(listOf(REQUIRED_PERMISSION)) -> launchScanner()

            shouldShowRequestPermissionRationale(REQUIRED_PERMISSION) -> showPermissionRationaleModal {
                requestPermissionLauncher.launch(REQUIRED_PERMISSION)
            }

            else -> requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
    }

    private fun showPermissionRationaleModal(then: () -> Unit) {
        Modal.builder(requireView()).apply {
            title = getString(R.string.permission_needed)
            setMessage(getString(R.string.permission_needed_camera))
            callback = MoButton(getString(ir.drax.modal.R.string.modal_ok)) {
                then()
                true
            }

        }.build().show()
    }

    private fun launchScanner() {
        analytics.set(Event.QR.ScannerRequested)
        setFragmentResultListener(ScannerFragment.REQUEST_KEY) { _, bundle ->
            bundle.getString(ScannerFragment.RESPONSE_KEY)?.let { qrResponse ->
                if (qrResponse.isNotEmpty() && qrResponse.contains("::"))
                    sendPairRequest(qrResponse)
            }
        }

        findNavController().navigate(QrFragmentDirections.qrToScanner())
    }

    private fun sendPairRequest(requestCode: String) {
        viewModel.sendPairRequest(requestCode)
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
