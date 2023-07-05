package com.drax.sendit.view.base

import android.os.Bundle
import androidx.fragment.app.setFragmentResult
import com.drax.sendit.data.service.Analytics
import com.drax.sendit.data.service.Event
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject


abstract class BaseComposeBottomSheet : BottomSheetDialogFragment() {

    @Inject
    lateinit var analytics: Analytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        analytics.set(Event.Fragment.Viewed(this))
    }

    fun setResultAndDismiss(tag: String, bundleOf: Bundle) {
        setFragmentResult(tag, bundleOf)
        dismissAllowingStateLoss()
    }
}