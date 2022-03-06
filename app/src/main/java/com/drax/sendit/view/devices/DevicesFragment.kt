package com.drax.sendit.view.devices

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.drax.sendit.databinding.DevicesFragmentBinding
import com.drax.sendit.view.base.BaseFragment
import com.drax.sendit.view.devices.adapter.DevicesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class DevicesFragment : BaseFragment<DevicesFragmentBinding,DevicesVM>(DevicesFragmentBinding::inflate) {

    override val viewModel: DevicesVM by viewModel()

    private val adapter : DevicesAdapter by lazy { DevicesAdapter { id ->

    }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.devices.observe(viewLifecycleOwner){
            it?.let {
                adapter.submitList(it)
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.SEND_SMS),
                100
            )

            checkActivation()
        }
    }


    override fun onResume() {
        super.onResume()
        checkActivation()
    }


    private fun checkActivation() {
//        model.permissionGranted.postValue(checkPermissions())
    }


}