package com.drax.sendit.view.connections.unpair

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.drax.sendit.R
import com.drax.sendit.data.model.ModalMessage
import com.drax.sendit.databinding.ConnectionUnpairFragmentBinding
import com.drax.sendit.databinding.QrFragmentBinding
import com.drax.sendit.databinding.ScannerFragmentBinding
import com.drax.sendit.databinding.ShareContentFragmentBinding
import com.drax.sendit.domain.network.model.UnpairRequest
import com.drax.sendit.domain.network.model.type.DevicePlatform
import com.drax.sendit.view.DeviceWrapper
import com.drax.sendit.view.base.BaseBottomSheet
import com.drax.sendit.view.base.BaseFragment
import com.drax.sendit.view.util.collect
import com.drax.sendit.view.util.loadImageFromUri
import com.drax.sendit.view.util.modal
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class UnpairFragment (private val request: UnpairRequest):
    BaseBottomSheet<ConnectionUnpairFragmentBinding, UnpairVM>(ConnectionUnpairFragmentBinding::inflate) {
    override val viewModel: UnpairVM by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        collect(viewModel.uiState) { uiState ->
                when(uiState){
                    UnpairUiState.Done -> setResultAndDismiss(bundleOf())
                    is UnpairUiState.Failed -> modal(ModalMessage.FromNetError(uiState.reason.errorCode))
                    UnpairUiState.Loading -> Unit
                    UnpairUiState.Neutral -> Unit
                }
        }

        binding.cancelBtn.setOnClickListener { dismissAllowingStateLoss() }
        binding.unpairBtn.setOnClickListener { viewModel.unpairDevice(request) }
    }

    companion object{
        const val FRAGMENT_KEY = "UNPAIR_CONTENT_KEY"
        const val TAG = "UnpairContentFragment"
    }
}