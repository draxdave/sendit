package com.drax.sendit.view.main

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.drax.sendit.databinding.MainFragmentBinding
import com.drax.sendit.view.main.adapter.DevicesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : Fragment() {

    private val model: MainViewModel by viewModel()
    private val adapter : DevicesAdapter by lazy { DevicesAdapter { id ->

    }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return MainFragmentBinding.inflate(inflater, container, false).let {
            it.lifecycleOwner = viewLifecycleOwner
            it.model = model
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        model.devices.observe(viewLifecycleOwner){
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

class RtlLayoutManager(context: Context,count:Int) : GridLayoutManager(context,count,VERTICAL,false) {

    override fun isLayoutRTL(): Boolean {
        return true
    }
}