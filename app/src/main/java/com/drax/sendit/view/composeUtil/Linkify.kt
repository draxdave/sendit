package com.drax.sendit.view.composeUtil

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun LinkifyText(text: String, modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    val layoutResult = remember {
        mutableStateOf<TextLayoutResult?>(null)
    }
    val linksList = extractLinks(text)
    val annotatedString = buildAnnotatedString {
        append(text)
        linksList.forEach {
            addStyle(
                style = SpanStyle(
                    color = Color.Companion.Blue,
                    textDecoration = TextDecoration.Underline
                ),
                start = it.start,
                end = it.end
            )
            addStringAnnotation(
                tag = "LINK",
                annotation = it.linkText,
                start = it.start,
                end = it.end
            )
        }
    }
    Text(text = annotatedString,
        style = MaterialTheme.typography.body1,
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures { offsetPosition ->
                layoutResult.value?.let {
                    val position = it.getOffsetForPosition(offsetPosition)
                    annotatedString.getStringAnnotations(position, position).firstOrNull()
                        ?.let { result ->
                            if (result.tag == "LINK") {
                                uriHandler.openUri(result.item)
                            }
                        }
                }
            }
        },
        onTextLayout = { layoutResult.value = it }
    )
}

fun extractLinks(text: String): List<TextLinkInfo> {
    val urlMatcher = android.util.Patterns.WEB_URL.matcher(text.lowercase())
    val emailMatcher = android.util.Patterns.EMAIL_ADDRESS.matcher(text.lowercase())
    val phoneMatcher = android.util.Patterns.PHONE.matcher(text.lowercase())
    var matchStart: Int
    var matchEnd: Int
    val links = arrayListOf<TextLinkInfo>()


    while (emailMatcher.find()) {
        matchStart = emailMatcher.start(0)
        matchEnd = emailMatcher.end()

        val email = text.substring(matchStart, matchEnd).let {
            "mailto:$it"
        }
        links.add(TextLinkInfo(email, matchStart, matchEnd))
    }

    while (urlMatcher.find()) {
        matchStart = urlMatcher.start(1)
        matchEnd = urlMatcher.end()
        var url = text.substring(matchStart, matchEnd)
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "https://$url"

        links.add(TextLinkInfo(url, matchStart, matchEnd))
    }


    while (phoneMatcher.find()) {
        matchStart = phoneMatcher.start(0)
        matchEnd = phoneMatcher.end()

        val phone = text.substring(matchStart, matchEnd).let {
            "tel:$it"
        }
        links.add(TextLinkInfo(phone, matchStart, matchEnd))
    }
    return links
}

data class TextLinkInfo(
    val linkText: String,
    val start: Int,
    val end: Int,
)