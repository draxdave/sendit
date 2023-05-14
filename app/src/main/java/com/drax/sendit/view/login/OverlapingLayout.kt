package com.drax.sendit.view.login

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun OverlappingLayout(
    overlappingContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {


    Layout(
        modifier = modifier,
        content = overlappingContent,
    ) { measurables, constraints ->

        val placeables = measurables.map {
            it.measure(constraints)
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            var yPos = 0
            placeables.forEach { placeable ->
                placeable.placeRelative(
                    x = 0, y = yPos,
                )
                yPos += placeable.height/2
            }
        }
    }
}

fun Modifier.center() = layout { measurable, constraints ->
    val placable = measurable.measure(
        constraints
    )

    layout(width = placable.width, height = placable.height) {
        placable.placeRelative(
            0, 0
        )
    }

}

@Preview(name = "OverlappingLayout Preview (Light)")
@Composable
fun OverlappingLayoutPreview() {
    OverlappingLayout(
        overlappingContent = {
            Text(text = "Underlying text", Modifier.center())
            Text(text = "Overlying text")
            Text(text = "Overlying text")
            Text(text = "Overlying text")
        },
    )
}