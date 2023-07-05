package com.drax.sendit.view.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import app.siamak.sendit.R

val fontMain = FontFamily(
    Font(R.font.roboto_regular, weight = FontWeight.Normal, style = FontStyle.Normal),
    Font(R.font.roboto_medium , style = FontStyle.Normal),
)

val fontDosisBoldFamily = FontFamily(
    Font(R.font.dosis_bold),
//    Font(R.font.dosis, weight = FontWeight.Bold, style = FontStyle.Normal),
)
val fontDosisRegularFamily = FontFamily(
    Font(R.font.dosis , style = FontStyle.Normal),
    Font(R.font.dosis, weight = FontWeight.Normal, style = FontStyle.Normal),
)