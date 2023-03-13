package com.drax.sendit.view.util

import android.app.Activity
import com.drax.sendit.data.model.ModalMessage
import ir.drax.modal.Modal


fun Activity.modal(message: ModalMessage) {
    Modal.builder(this).apply {
        type = Modal.Type.Alert
        title = getString(message.title)
        icon = message.icon
        direction =
            if (message.fromTop)
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