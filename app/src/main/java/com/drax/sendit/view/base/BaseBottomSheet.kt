package com.drax.sendit.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.drax.sendit.BR
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class  BaseBottomSheet<out T: ViewDataBinding, E : ViewModel>(
    private val inflate:Inflate<T>
) : BottomSheetDialogFragment(){

    protected abstract val viewModel: E

    private var _binding: T? = null
    val binding get() = _binding!!

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}