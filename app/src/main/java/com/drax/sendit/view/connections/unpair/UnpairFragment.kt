package com.drax.sendit.view.connections.unpair

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import app.siamak.sendit.databinding.ConnectionUnpairFragmentBinding
import com.drax.sendit.data.model.ModalMessage
import com.drax.sendit.data.service.Event
import com.drax.sendit.domain.network.model.UnpairRequest
import com.drax.sendit.view.base.BaseBottomSheet
import com.drax.sendit.view.util.modal
import com.drax.sendit.view.util.observe

class UnpairFragment(private val request: UnpairRequest) :
    BaseBottomSheet<ConnectionUnpairFragmentBinding, UnpairVM>(ConnectionUnpairFragmentBinding::inflate) {
    override val viewModel: UnpairVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                UnpairUiState.Done -> {
                    analytics.set(Event.Connections.Unpaired)
                    setResultAndDismiss(TAG, bundleOf())
                }
                is UnpairUiState.Failed -> modal(ModalMessage.FromNetError(uiState.reason.errorCode))
                UnpairUiState.Loading -> Unit
                UnpairUiState.Neutral -> Unit
            }
        }

        binding.cancelBtn.setOnClickListener { dismissAllowingStateLoss() }
        binding.unpairBtn.setOnClickListener { viewModel.unpairDevice(request) }
    }

    companion object {
        const val FRAGMENT_KEY = "UNPAIR_CONTENT_KEY"
        const val TAG = "UnpairContentFragment"
    }
}