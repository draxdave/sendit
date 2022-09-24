package com.drax.sendit.view.scanner

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.drax.sendit.data.service.Event
import app.siamak.sendit.databinding.ScannerFragmentBinding
import com.drax.sendit.view.base.BaseFragment
import com.drax.sendit.view.util.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScannerFragment: BaseFragment<ScannerFragmentBinding, ScannerVM>(ScannerFragmentBinding::inflate) {
    override val viewModel: ScannerVM by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    private lateinit var codeScanner: CodeScanner

    private fun initView() {
        collect(viewModel.uiState) {
            when(it){
                ScannerUiState.Neutral -> Unit
            }
        }

        codeScanner = CodeScanner(requireContext(), binding.scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            analytics.set(Event.QR.Scanned)
            view?.post {
                submitResultAndDismiss(it.text)
            }
        }
        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun submitResultAndDismiss(qrData: String){
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
    companion object{
        const val REQUEST_KEY = "SCANNER_REQUEST_KEY"
        const val RESPONSE_KEY = "qrData"
    }
}