import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Long.formatToDate(format: String): String {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    val date = Date(this * 1000)
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(date)
}