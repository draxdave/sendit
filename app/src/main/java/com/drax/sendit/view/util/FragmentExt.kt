package com.drax.sendit.view.util

import androidx.fragment.app.Fragment
import com.drax.sendit.data.model.ModalMessage
import ir.drax.modal.Modal

fun Fragment.modal(message: ModalMessage){
    Modal.builder(requireView()).apply {
        type = Modal.Type.Alert
        title = getString(message.title)
        icon = message.icon
        direction =
            if(message.fromTop)
                Modal.Direction.Top
            else
                Modal.Direction.Bottom

        lockVisibility = message.lock
        message.description?.let {
            setMessage(getString(it))
        }
    }
        .build()
        .show()
}