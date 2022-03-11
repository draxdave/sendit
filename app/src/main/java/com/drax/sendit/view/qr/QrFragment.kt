package com.drax.sendit.view.qr

import com.drax.sendit.databinding.QrFragmentBinding
import com.drax.sendit.view.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class QrFragment: BaseFragment<QrFragmentBinding, QrVM>(QrFragmentBinding::inflate) {
    override val viewModel: QrVM by viewModel()
}