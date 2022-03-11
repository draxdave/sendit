package com.drax.sendit.view.transmissions

import com.drax.sendit.databinding.TransmissionsFragmentBinding
import com.drax.sendit.view.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransmissionsFragment: BaseFragment<TransmissionsFragmentBinding, TransmissionsVM>(TransmissionsFragmentBinding::inflate) {
    override val viewModel: TransmissionsVM by viewModel()
}