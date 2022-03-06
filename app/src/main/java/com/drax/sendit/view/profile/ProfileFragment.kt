package com.drax.sendit.view.profile

import com.drax.sendit.databinding.ProfileFragmentBinding
import com.drax.sendit.view.base.BaseFragment
import com.drax.sendit.view.devices.DevicesVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment: BaseFragment<ProfileFragmentBinding, ProfileVM>(ProfileFragmentBinding::inflate) {
    override val viewModel: ProfileVM by viewModel()
}