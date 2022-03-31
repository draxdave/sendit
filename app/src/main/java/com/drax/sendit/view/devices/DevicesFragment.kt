package com.drax.sendit.view.devices

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.drax.sendit.databinding.DevicesFragmentBinding
import com.drax.sendit.domain.network.model.UnpairRequest
import com.drax.sendit.view.DeviceWrapper
import com.drax.sendit.view.base.BaseFragment
import com.drax.sendit.view.devices.adapter.DevicesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class DevicesFragment : BaseFragment<DevicesFragmentBinding,DevicesVM>(DevicesFragmentBinding::inflate) {

    override val viewModel: DevicesVM by viewModel()

    private val adapter : DevicesAdapter by lazy { DevicesAdapter { connection_id ->
        viewModel.removeDevice(UnpairRequest(connection_id))
    }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.list.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = this@DevicesFragment.adapter
        }
        viewModel.devices.observe(viewLifecycleOwner) {
            adapter.submitList(it.map {device->
                DeviceWrapper(device)
            })
//            if (it.isEmpty())
//                viewModel.addSelfDevice()
        }
    }

}