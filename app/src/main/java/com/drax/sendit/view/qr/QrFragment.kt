package com.drax.sendit.view.qr

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import app.siamak.sendit.R
import app.siamak.sendit.databinding.QrFragmentBinding
import com.drax.sendit.data.model.ModalMessage
import com.drax.sendit.data.service.Event
import com.drax.sendit.view.base.BaseFragment
import com.drax.sendit.view.scanner.ScannerFragment
import com.drax.sendit.view.util.allPermissionsGranted
import com.drax.sendit.view.util.modal
import com.drax.sendit.view.util.observe
import ir.drax.modal.Modal
import ir.drax.modal.model.MoButton
import org.koin.androidx.viewmodel.ext.android.viewModel

class QrFragment: BaseFragment<QrFragmentBinding, QrVM>(QrFragmentBinding::inflate) {
    override val viewModel: QrVM by viewModel()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                launchScanner()
            } else {
                analytics.set(Event.QR.CameraPermissionRejected)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        viewModel.uiState.observe(viewLifecycleOwner){
            viewModel.uiState.collect{
                when(it){
                    QrUiState.Neutral -> Unit
                    QrUiState.QrLoading -> Unit
                }
            }
        }

        viewModel.state.observe(viewLifecycleOwner) {
            when(it){
                is QrState.QrLoadFailed -> {
                    analytics.set(Event.QR.LoadQRFailed)
                    modal(ModalMessage.Failed(it.reason))
                }
                is QrState.QrLoaded -> Unit
                is QrState.QrLoadFailedFromNet -> {
                    analytics.set(Event.QR.LoadQRFailedFromNet)
                    modal(ModalMessage.FromNetError(it.reason.errorCode))
                }
                is QrState.PairDone -> {
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
                is QrState.PairFailed -> {
                    analytics.set(Event.QR.ReceivedInvitationFailedToSend)
                    modal(ModalMessage.FromNetError(it.reason.errorCode))
                }
                QrState.ConnectionAlreadyActive -> modal(ModalMessage.Neutral(R.string.invitation_already_active))
                QrState.RequestRejected -> {
                    modal(ModalMessage.Neutral(R.string.invitation_rejected))
                }
                QrState.InvitationResponseWaiting ->
                    modal(ModalMessage.Neutral(R.string.invitation_waiting_for_peer))
            }
        }

        binding.scanBtn.setOnClickListener {
            checkPermissionAndLaunchScanner()
        }
    }

    private fun checkPermissionAndLaunchScanner(){
        when {
            allPermissionsGranted(listOf(REQUIRED_PERMISSION)) -> launchScanner()

            shouldShowRequestPermissionRationale(REQUIRED_PERMISSION) -> showPermissionRationaleModal {
                requestPermissionLauncher.launch(REQUIRED_PERMISSION)
            }
            else -> requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
    }

    private fun showPermissionRationaleModal(then: ()->Unit){
        Modal.builder(requireView()).apply {
            title = getString(R.string.permission_needed)
            setMessage(getString(R.string.permission_needed_camera))
            callback = MoButton(getString(ir.drax.modal.R.string.modal_ok)){
                then()
                true
            }

        }.build().show()
    }

    private fun launchScanner(){
        analytics.set(Event.QR.ScannerRequested)
        setFragmentResultListener(ScannerFragment.REQUEST_KEY) { _, bundle ->
            bundle.getString(ScannerFragment.RESPONSE_KEY)?.let { qrResponse ->
                if (qrResponse.isNotEmpty() && qrResponse.contains("::"))
                    sendPairRequest(qrResponse)
            }
        }

        findNavController().navigate(QrFragmentDirections.qrToScanner())
    }

    private fun sendPairRequest(requestCode: String){
        viewModel.sendPairRequest(requestCode)
    }

    companion object{
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}