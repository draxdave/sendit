package com.drax.sendit.view.shareContent

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
import com.drax.sendit.databinding.QrFragmentBinding
import com.drax.sendit.databinding.ScannerFragmentBinding
import com.drax.sendit.databinding.ShareContentFragmentBinding
import com.drax.sendit.domain.network.model.type.DevicePlatform
import com.drax.sendit.view.DeviceWrapper
import com.drax.sendit.view.base.BaseBottomSheet
import com.drax.sendit.view.base.BaseFragment
import com.drax.sendit.view.util.loadImageFromUri
import com.drax.sendit.view.util.modal
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShareContentFragment: BaseBottomSheet<ShareContentFragmentBinding, ShareContentVM>(ShareContentFragmentBinding::inflate) {
    override val viewModel: ShareContentVM by viewModel()

    private val contentToShare: String? by lazy {
        arguments?.getString(SHARE_CONTENT_KEY)
    }

    private val mAdapter: ShareConnectionsAdapter by lazy {
        ShareConnectionsAdapter { deviceWrapper ->
            if (contentToShare == null) {
                dismissAllowingStateLoss()
                return@ShareConnectionsAdapter

            } else {
                viewModel.share(contentToShare!!, deviceWrapper.connection.id)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { uiState ->
                when(uiState){
                    ShareContentUiState.Loading -> Unit
                    is ShareContentUiState.ConnectionsLoaded -> mAdapter.submitList(uiState.connections.map {
                        DeviceWrapper(it)
                    })
                    ShareContentUiState.NoConnectionsAvailable -> modal(ModalMessage.Neutral(R.string.no_connected_devices))
                    ShareContentUiState.SharingDone -> {
                        modal(ModalMessage.Success(R.string.share_success)) {
                            setResultAndDismiss()
                        }
                    }
                    is ShareContentUiState.SharingFailed -> modal(ModalMessage.FromNetError(uiState.reason.errorCode))
                }
            }
        }

        binding.list.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }
    }



    private fun setResultAndDismiss(){
        setFragmentResult(TAG, bundleOf())
        dismissAllowingStateLoss()
    }

    private fun platformToIcon(@DevicePlatform platform: Int) = when(platform){
        DevicePlatform.DevicePlatform_ANDROID -> R.drawable.ic_round_android_24
        DevicePlatform.DevicePlatform_CHROME -> R.drawable.ic_google_icon
        else -> R.drawable.ic_fragment_devices

    }

    companion object{
        const val SHARE_CONTENT_KEY = "SHARE_CONTENT_KEY"
        const val TAG = "ShareContentFragment"
    }
}