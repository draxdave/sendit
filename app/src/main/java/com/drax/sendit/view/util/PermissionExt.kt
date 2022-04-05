package com.drax.sendit.view.util

import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


/**
 *  Permission dialog
 * */

fun Fragment.allPermissionsGranted(permissions: List<String>) = permissions.all {
    ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
}
