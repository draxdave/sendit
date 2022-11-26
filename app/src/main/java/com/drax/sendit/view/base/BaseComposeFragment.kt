package com.drax.sendit.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import app.siamak.sendit.BR
import com.drax.sendit.data.service.Analytics
import com.drax.sendit.data.service.Event
import org.koin.android.ext.android.inject

abstract class  BaseComposeFragment : Fragment(){

    val analytics: Analytics by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics.set(Event.Fragment.Viewed(this))
    }
}