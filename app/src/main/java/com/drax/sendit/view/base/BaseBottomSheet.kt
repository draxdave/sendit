package com.drax.sendit.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModel
import com.drax.sendit.BR
import com.drax.sendit.data.service.Analytics
import com.drax.sendit.data.service.Event
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject

abstract class  BaseBottomSheet<out T: ViewDataBinding, E : ViewModel>(
    private val inflate:Inflate<T>
) : BottomSheetDialogFragment(){
    val analytics: Analytics by inject()

    protected abstract val viewModel: E

    private var _binding: T? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics.set(Event.Fragment.Viewed(this))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflate.invoke(inflater,container,false).apply {
            lifecycleOwner = viewLifecycleOwner
            setVariable(BR.model,viewModel)
            _binding = this
        }.root
    }


    fun setResultAndDismiss(tag: String, bundleOf: Bundle){
        setFragmentResult(tag, bundleOf)
        dismissAllowingStateLoss()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}