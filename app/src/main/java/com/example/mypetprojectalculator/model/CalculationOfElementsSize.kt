package com.example.mypetprojectalculator.model

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times

class CalculationOfElementsSize(
    val maxWidthBox: Dp = 0.dp,
    val maxHeightBox: Dp = 0.dp,
    val density: Density
) {
    fun calculateButtonSize(): Pair<Dp, Dp> {
        val columns = 4
        val rows = 5
        val spacing = 3.dp

        val horizontalSpacingTotal = (columns - 1) * spacing
        val verticalSpacingTotal = (rows - 1) * spacing

        val buttonWidth = (maxWidthBox - horizontalSpacingTotal) / columns
        val buttonHeight = (maxHeightBox - verticalSpacingTotal) / rows

        return Pair(buttonWidth, buttonHeight)
    }

    fun calculateTextSizeOnButton() : TextUnit {
        val (buttonWidth, buttonHeight) = calculateButtonSize()
        val buttonMinSize = minOf(buttonWidth, buttonHeight)
        val textSize = with(density) { (buttonMinSize * 0.5f).toSp() }
        return textSize
    }

    fun calculateIconSizeOnButton() : Dp {
        val (buttonWidth, buttonHeight) = calculateButtonSize()
        val buttonMinSize = minOf(buttonWidth, buttonHeight)
        val iconSize = buttonMinSize * 0.5f
        return iconSize
    }

    fun calculateTextSize(scaleFactor: Float) : TextUnit {
        val maxWidthBoxPx = with(density) {
            maxWidthBox.toPx()
        }
        val maxHeightBoxPx = with(density) {
            maxHeightBox.toPx()
        }
        val maxTextWidth = maxWidthBoxPx / density.density
        val maxTextHeight = maxHeightBoxPx / density.density
        return (minOf(maxTextWidth, maxTextHeight) * scaleFactor).sp
    }
}