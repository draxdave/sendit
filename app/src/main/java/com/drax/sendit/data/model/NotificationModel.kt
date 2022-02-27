package com.drax.sendit.data.model

import androidx.room.PrimaryKey
import com.drax.sendit.R

data class NotificationModel (
    var titleStr            : String = "",
    var isEnabled           : Boolean = false,
    var rtcTime             : Long = System.currentTimeMillis(),
    var icon                : Int = 0,
    var isSticky            : Boolean = false,
    var title               : Int = R.string.app_name,
    var text               : Int = R.string.app_name,
    var tags               : Int = 0,
    var vibrate               : Boolean = false,
    var value               : Double=0.0

) {
    @PrimaryKey(autoGenerate = true)
    var id                  : Long = 0
}
