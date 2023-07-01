package com.drax.sendit.view.scanner

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import app.siamak.sendit.R
import app.siamak.sendit.databinding.ScannerFragmentBinding
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.drax.sendit.data.service.Event
import com.drax.sendit.view.base.BaseComposeFragment
import com.drax.sendit.view.base.BaseFragment
import com.drax.sendit.view.theme.aqua200
import com.drax.sendit.view.theme.low_white
import com.drax.sendit.view.util.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScannerFragment : BaseComposeFragment() {
    private lateinit var codeScanner: CodeScanner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            ScannerScreen()
        }
    }

    @Composable
    fun ScannerScreen() {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        codeScanner.startPreview()
                    },
                factory = { context ->
                    CodeScannerView(context).apply {
                        autoFocusButtonColor = Color.WHITE
                        isAutoFocusButtonVisible = true
                        flashButtonColor = low_white.toArgb()
                        isFlashButtonVisible = true
                        frameAspectRatioHeight = 1f
                        frameAspectRatioWidth = 1f
                        frameColor = aqua200.toArgb()
                        frameCornersRadius = 10
                        frameCornersSize = 50
                        frameSize = 0.75f
                        frameThickness = 2
                        maskColor = Color.parseColor("#77000000")

                        codeScanner = CodeScanner(requireContext(), this)
                        codeScanner.decodeCallback = DecodeCallback {
                            analytics.set(Event.QR.Scanned)
                            view?.post {
                                submitResultAndDismiss(it.text)
                            }
                        }
                    }
                })

            Image(
                modifier = Modifier
                    .padding(top = 48.dp)
                    .padding(16.dp)
                    .clickable {
                        findNavController().navigateUp()
                    },
                painter = painterResource(id = R.drawable.ic_outline_arrow_circle_left_24),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.surface),
            )
        }
    }

    @Preview
    @Composable
    fun ScannerScreenPreview() {
        ScannerScreen()
    }

    private fun submitResultAndDismiss(qrData: String) {
        setFragmentResult(REQUEST_KEY, bundleOf(RESPONSE_KEY to qrData))
        findNavController().navigateUp()
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    companion object {
        const val REQUEST_KEY = "SCANNER_REQUEST_KEY"
        const val RESPONSE_KEY = "qrData"
    }
}