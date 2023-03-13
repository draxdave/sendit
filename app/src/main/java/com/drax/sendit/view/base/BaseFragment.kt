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
import javax.inject.Inject


typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<out T : ViewDataBinding, E : ViewModel>(
    private val inflate: Inflate<T>
) : Fragment() {

    @Inject
    lateinit var analytics: Analytics
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
        return inflate.invoke(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            setVariable(BR.model, viewModel)
            _binding = this
        }.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}