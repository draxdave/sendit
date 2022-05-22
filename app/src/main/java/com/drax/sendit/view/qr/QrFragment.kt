package com.drax.sendit.view.qr

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.drax.sendit.R
import com.drax.sendit.data.model.ModalMessage
import com.drax.sendit.data.service.PushProcessor.Companion.INVITATION_RESPONSE
import com.drax.sendit.data.service.models.NewInvitation
import com.drax.sendit.databinding.NewInvitationModalBinding
import com.drax.sendit.databinding.QrFragmentBinding
import com.drax.sendit.domain.network.model.type.PairResponseType
import com.drax.sendit.view.base.BaseFragment
import com.drax.sendit.view.scanner.ScannerFragment
import com.drax.sendit.view.util.allPermissionsGranted
import com.drax.sendit.view.util.collect
import com.drax.sendit.view.util.loadImageFromUri
import com.drax.sendit.view.util.modal
import ir.drax.modal.Modal
import ir.drax.modal.model.MoButton
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class QrFragment: BaseFragment<QrFragmentBinding, QrVM>(QrFragmentBinding::inflate) {
    override val viewModel: QrVM by viewModel()

    private val broadcastReceiver: BroadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            intent?.getSerializableExtra("data")?.let { it as NewInvitation
                showInvitationModal(it.deviceName, it.connectionId.toLong())
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                launchScanner()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        println("uiState: initView")

        collect(viewModel.uiState){
            println("uiState: launchWhenCreated")
            viewModel.uiState.collect{
                println("uiState: $it")
                when(it){
                    QrUiState.Neutral -> Unit
                    is QrUiState.QrLoadFailed -> modal(ModalMessage.Failed(it.reason))
                    is QrUiState.QrLoaded -> {
                        binding.deviceQr.loadImageFromUri(it.qrUrl)
                        binding.deviceQrContainer.visibility = View.VISIBLE
                    }
                    QrUiState.QrLoading -> Unit
                    is QrUiState.QrLoadFailedFromNet -> modal(ModalMessage.FromNetError(it.reason.errorCode))
                    is QrUiState.InvitationFailed -> modal(ModalMessage.FromNetError(it.reason.errorCode))
                    QrUiState.InvitationSending -> Unit
                    QrUiState.InvitationSent -> modal(
                        ModalMessage.Full(
                            mTitle = R.string.invitation_sent_title,
                            mDescription = R.string.invitation_sent_desc,
                            mIcon = R.drawable.tick,
                            mFromTop = true,
                            mLock = false
                        )
                    )
                    QrUiState.InvitationResponseSending -> Unit
                    is QrUiState.InvitationResponseFailed -> modal(ModalMessage.FromNetError(it.reason.errorCode))
                    QrUiState.InvitationResponseSent -> modal(ModalMessage.Success(R.string.new_invitation_accept_sent))

                    QrUiState.InvitationResponseAlreadyActive -> modal(ModalMessage.Neutral(R.string.invitation_already_active))
                    QrUiState.InvitationResponseRejected -> modal(ModalMessage.Neutral(R.string.invitation_rejected))
                    QrUiState.InvitationResponseWaiting ->
                        modal(ModalMessage.Neutral(R.string.invitation_waiting_for_peer))
                }
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

    override fun onResume() {
        super.onResume()
        registerListener()
    }

    override fun onPause() {
        super.onPause()
        unregisterListener()
    }

    private fun registerListener(){
        requireActivity().registerReceiver(broadcastReceiver, IntentFilter(INVITATION_RESPONSE))
    }

    private fun unregisterListener(){
        requireActivity().unregisterReceiver(broadcastReceiver)
    }

    private fun launchScanner(){
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

    private fun showInvitationModal(deviceName: String, connectionId: Long){
        Modal.builder(requireView()).apply {


            type = Modal.Type.Custom
            contentView = NewInvitationModalBinding.inflate(layoutInflater).apply {
                setDeviceName(deviceName)
                acceptBtn.setOnClickListener {
                    viewModel.sendInvitationResponse(connectionId, PairResponseType.PairResponseType_ACCEPT)
                    Modal.hide(requireView())
                }

                declineBtn.setOnClickListener {
                    viewModel.sendInvitationResponse(connectionId, PairResponseType.PairResponseType_DECLINE)
                    Modal.hide(requireView())
                }
            }.root

        }.build()
            .show()
    }

    companion object{
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}