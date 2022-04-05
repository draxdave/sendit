package com.drax.sendit.view.qr

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.drax.sendit.data.model.ModalMessage
import com.drax.sendit.databinding.QrFragmentBinding
import com.drax.sendit.view.base.BaseFragment
import com.drax.sendit.view.util.loadImageFromUri
import com.drax.sendit.view.util.modal
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class QrFragment: BaseFragment<QrFragmentBinding, QrVM>(QrFragmentBinding::inflate) {
    override val viewModel: QrVM by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect{
             when(it){
                 QrUiState.Neutral -> Unit
                 is QrUiState.QrLoadFailed -> modal(ModalMessage.Failed(it.reason))
                 is QrUiState.QrLoaded -> binding.deviceQr.loadImageFromUri(it.qrUrl)
                 QrUiState.QrLoading -> Unit
                 is QrUiState.QrLoadFailedFromNet -> modal(ModalMessage.FromNetError(it.reason.errorCode))
             }
            }
        }
    }
}