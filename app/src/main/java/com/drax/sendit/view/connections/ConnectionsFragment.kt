package com.drax.sendit.view.connections

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.drax.sendit.databinding.ConnectionsFragmentBinding
import com.drax.sendit.domain.network.model.UnpairRequest
import com.drax.sendit.view.base.BaseFragment
import com.drax.sendit.view.connections.adapter.ConnectionsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConnectionsFragment : BaseFragment<ConnectionsFragmentBinding,ConnectionsVM>(ConnectionsFragmentBinding::inflate) {

    override val viewModel: ConnectionsVM by viewModel()

    private val adapter : ConnectionsAdapter by lazy { ConnectionsAdapter { connection_id ->
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
            adapter = this@ConnectionsFragment.adapter
        }
        viewModel.devices.observe(viewLifecycleOwner) {
//            adapter.submitList(it.map {device->
//                DeviceWrapper(device)
//            })
//            if (it.isEmpty())
//                viewModel.addSelfDevice()
        }
    }

}