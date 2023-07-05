package com.drax.sendit.view.base

import android.content.Context
import androidx.fragment.app.Fragment
import com.drax.sendit.data.service.Analytics
import com.drax.sendit.data.service.Event
import javax.inject.Inject

abstract class BaseComposeFragment : Fragment() {

    @Inject
    lateinit var analytics: Analytics

    override fun onAttach(context: Context) {
        super.onAttach(context)
        analytics.set(Event.Fragment.Viewed(this))
    }
}