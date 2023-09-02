package com.example.mypetprojectalculator.model

/*
 * Author: Brandon McAnsh
 * https://www.jetpackcompose.app/snippets/AutosizeText
*/

//import android.util.Log
//import androidx.compose.material3.LocalTextStyle
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.TextUnit
//import androidx.compose.ui.draw.drawWithContent
//import androidx.compose.ui.text.AnnotatedString
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextDecoration
//
//sealed class AutoSizeConstraint(open val min: TextUnit = TextUnit.Unspecified) {
//    data class Width(override val min: TextUnit = TextUnit.Unspecified) : AutoSizeConstraint(min)
//    data class Height(override val min: TextUnit = TextUnit.Unspecified) : AutoSizeConstraint(min)
//}
//
//@Composable
//fun AutoSizeText(
//    text: AnnotatedString,
//    modifier: Modifier = Modifier,
//    color: Color = Color.Unspecified,
//    fontSize: TextUnit = TextUnit.Unspecified,
//    fontStyle: FontStyle? = null,
//    fontWeight: FontWeight? = null,
//    fontFamily: FontFamily? = null,
//    letterSpacing: TextUnit = TextUnit.Unspecified,
//    textDecoration: TextDecoration? = null,
//    textAlign: TextAlign? = null,
//    lineHeight: TextUnit = TextUnit.Unspecified,
//    overflow: TextOverflow = TextOverflow.Clip,
//    softWrap: Boolean = true,
//    maxLines: Int = 1,
//    style: TextStyle = LocalTextStyle.current,
//    constraint: AutoSizeConstraint = AutoSizeConstraint.Width(),
//) {
//    var textStyle by remember { mutableStateOf(style) }
//    Log.d("MyLog", "Text style: ${textStyle.fontSize}")
//    var readyToDraw by remember { mutableStateOf(false) }
//    Log.d("MyLog", "Ready to draw: $readyToDraw")
//
//    Text(
//        modifier = modifier.drawWithContent {
//            if (readyToDraw) drawContent()
//        },
//        text = text,
//        color = color,
//        fontStyle = fontStyle,
//        fontWeight = fontWeight,
//        fontFamily = fontFamily,
//        letterSpacing = letterSpacing,
//        textDecoration = textDecoration,
//        textAlign = textAlign,
//        lineHeight = lineHeight,
//        overflow = overflow,
//        softWrap = softWrap,
//        maxLines = maxLines,
//        style = style.copy(fontSize = fontSize),
//        onTextLayout = { result ->
//            fun constrain() {
//                val reducedSize = textStyle.fontSize * 0.9f
//                Log.d("MyLog", "Reduced size: $reducedSize")
//                if (constraint.min != TextUnit.Unspecified && reducedSize <= constraint.min) {
//                    textStyle = textStyle.copy(fontSize = constraint.min)
//                    readyToDraw = true
//                } else {
//                    textStyle = textStyle.copy(fontSize = textStyle.fontSize * 0.9f)
//                }
//            }
//            when (constraint) {
//                is AutoSizeConstraint.Height -> {
//                    if (result.didOverflowHeight) {
//                        constrain()
//                    } else {
//                        readyToDraw = true
//                    }
//                }
//
//                is AutoSizeConstraint.Width -> {
//                    if (result.didOverflowWidth) {
//                        constrain()
//                    } else {
//                        readyToDraw = true
//                    }
//                }
//            }
//        }
//    )
//}
//
//@Composable
//fun AutoSizeText(
//    text: String,
//    modifier: Modifier = Modifier,
//    color: Color = Color.Unspecified,
//    fontSize: TextUnit = TextUnit.Unspecified,
//    fontStyle: FontStyle? = null,
//    fontWeight: FontWeight? = null,
//    fontFamily: FontFamily? = null,
//    letterSpacing: TextUnit = TextUnit.Unspecified,
//    textDecoration: TextDecoration? = null,
//    textAlign: TextAlign? = null,
//    lineHeight: TextUnit = TextUnit.Unspecified,
//    overflow: TextOverflow = TextOverflow.Clip,
//    softWrap: Boolean = true,
//    maxLines: Int = Int.MAX_VALUE,
//    style: TextStyle = LocalTextStyle.current,
//    constraint: AutoSizeConstraint = AutoSizeConstraint.Width(),
//) {
//    AutoSizeText(
//        modifier = modifier,
//        text = AnnotatedString(text),
//        color = color,
//        fontSize = fontSize,
//        fontStyle = fontStyle,
//        fontWeight = fontWeight,
//        fontFamily = fontFamily,
//        letterSpacing = letterSpacing,
//        textDecoration = textDecoration,
//        textAlign = textAlign,
//        lineHeight = lineHeight,
//        overflow = overflow,
//        softWrap = softWrap,
//        maxLines = maxLines,
//        style = style,
//        constraint = constraint
//    )
//}
//===================================== Спосіб 2 ============================================
//@Composable
//fun AutoSizeText(
//    enteredValues: MutableState<List<String>>,
//    initialFontSize: TextUnit = TextUnit.Unspecified,
//    baseTextSize: Int = 13,
//    maxTextSize: Int = 25,
//
//    text: String,
//    modifier: Modifier = Modifier,
//    color: Color = Color.Unspecified,
//    fontSize: TextUnit,
//    fontStyle: FontStyle? = null,
//    fontWeight: FontWeight? = null,
//    fontFamily: FontFamily?,
//    letterSpacing: TextUnit,
//    textDecoration: TextDecoration?,
//    textAlign: TextAlign?,
//    lineHeight: TextUnit,
//    overflow: TextOverflow,
//    softWrap: Boolean,
//    maxLines: Int,
//    onTextLayout: (TextLayoutResult) -> Unit,
//    style: TextStyle
//) {
//    val sizeEnteredValues = enteredValues.value.size
//
//    val textSize = if (sizeEnteredValues > baseTextSize && sizeEnteredValues < maxTextSize) {
//        (initialFontSize.value - 1).sp
//    } else initialFontSize
//
//    Text(
//        text = text,
//        modifier = modifier,
//        color = color,
//        fontSize = fontSize,
//        fontStyle = fontStyle,
//        fontWeight = fontWeight,
//        fontFamily = fontFamily,
//        letterSpacing = letterSpacing,
//        textDecoration = textDecoration,
//        textAlign = textAlign,
//        lineHeight = lineHeight,
//        overflow = overflow,
//        softWrap = softWrap,
//        maxLines = maxLines,
//        onTextLayout = onTextLayout,
//        style = style
//    )
//
//}
//
//@Composable
//fun AutoSizeText(
//    text: String,
//    modifier: Modifier = Modifier,
//    style: TextStyle = LocalTextStyle.current,
//    overflow: TextOverflow = TextOverflow.Clip,
//    fontSize: TextUnit,
//    minFontSize: TextUnit,
//    color: Color = Color.Unspecified,
//    textAlign: TextAlign? = null,
//    lineHeight: TextUnit = TextUnit.Unspecified,
//    stepSize: TextUnit = 1.sp,
//    onTextSizeChange: (TextUnit) -> Unit = {},
//    densityBox: Density,
//    boxWidth: Dp,
//    context: Context
//) {
//    val density = densityBox
//    var pixelFontSize = with(density) { fontSize.roundToPx() }
//    val pixelWidth = with(density) { boxWidth.roundToPx() }
//    val pixelStepSize = with(density) { stepSize.roundToPx() }
//
//    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
//
//    fun pxToSp(
//        px: Int,
//        context: Context
//    ): TextUnit {
//        val scaledDensity = context.resources.displayMetrics.scaledDensity
//        return (px / scaledDensity).sp
//    }
//
//    val fontSizePxToSp = pxToSp(pixelFontSize, context)
//
//    Text(
//        text = text,
//        style = style.copy(fontSize = fontSizePxToSp),
//        modifier = modifier,
//        overflow = overflow,
//        maxLines = Int.MAX_VALUE,
//        color = color,
//        textAlign = textAlign,
//        lineHeight = lineHeight,
//        onTextLayout = { result ->
//            layoutResult.value = result
//        }
//    )
//
//    LaunchedEffect(layoutResult) {
//        val result = layoutResult.value ?: return@LaunchedEffect
//
//        if (result.size.width > pixelWidth) {
//            if (fontSizePxToSp > minFontSize) {
//                pixelFontSize -= pixelStepSize
//                onTextSizeChange(fontSizePxToSp)
//            }
//        } else {
//            if (fontSizePxToSp < fontSize) {
//                pixelFontSize += pixelStepSize
//                onTextSizeChange(fontSizePxToSp)
//            }
//        }
//    }
//}
//====================================== Спосіб 3 ==============================================
import android.util.Log
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit

@Composable
fun AutoSizeText(
    text: String,
    minFontSize: TextUnit = TextUnit.Unspecified,
    density: Density,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current
) {
    var scaledTextStyle by remember { mutableStateOf(style) }
    Log.d("MyLog", "Scaled text style: ${scaledTextStyle.fontSize}")
    Log.d("MyLog", "Font size: ${style.fontSize}")

    Text(
        text = text,
        color = color,
        maxLines = maxLines,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = true,
        style = scaledTextStyle,
        onTextLayout = { result ->
            val scaleByHeight =
                result.layoutInput.constraints.maxHeight / 2
            Log.d("MyLog", "Scale by height: $scaleByHeight")
            val scaleByWidth =
                result.layoutInput.constraints.maxWidth / result.layoutInput.text.length
            Log.d("MyLog", "Scale by width: $scaleByWidth")
            scaledTextStyle = scaledTextStyle.copy(
                fontSize = with(density) {
                    if (scaleByHeight < scaleByWidth)
                        scaleByHeight.toSp()
                    else
                        scaleByWidth.toSp()
                }
            )
        },
        modifier = modifier.drawWithContent { drawContent() }
    )
}
// ========================================= Спосіб 4 ============================================
//import android.text.TextPaint
//import android.util.Log
//import androidx.compose.foundation.layout.BoxWithConstraints
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.material3.LocalTextStyle
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableFloatStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.drawWithContent
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.text.TextLayoutResult
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextDecoration
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.Density
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.TextUnit
//
//@Composable
//fun AutoSizeText(
//    text: String,
//    fontSize: TextUnit,
//    minFontSize: TextUnit,
//    containerWidth: Dp,
//    modifier: Modifier = Modifier,
//    color: Color = Color.Unspecified,
//    fontStyle: FontStyle? = null,
//    fontWeight: FontWeight? = null,
//    fontFamily: FontFamily? = null,
//    letterSpacing: TextUnit = TextUnit.Unspecified,
//    textDecoration: TextDecoration? = null,
//    textAlign: TextAlign? = null,
//    lineHeight: TextUnit = TextUnit.Unspecified,
//    overflow: TextOverflow = TextOverflow.Clip,
//    softWrap: Boolean = true,
//    maxLines: Int = 1,
//    style: TextStyle = LocalTextStyle.current,
////    onTextLayout: (TextLayoutResult) -> Unit = {}
//) {
//    val density = LocalDensity.current
//    var readyToDraw by remember { mutableStateOf(false) }
//    val currentFontSize by remember { mutableStateOf(fontSize) }
//    val containerWidthPx = with(density) {
//        containerWidth.toPx()
//    }
//
//    Text(
//        text = text,
//        modifier = modifier
//            .drawWithContent {
//                if (readyToDraw) drawContent()
//            },
//        color = color,
//        fontStyle = fontStyle,
//        fontWeight = fontWeight,
//        fontFamily = fontFamily,
//        letterSpacing = letterSpacing,
//        textDecoration = textDecoration,
//        textAlign = textAlign,
//        lineHeight = lineHeight,
//        overflow = overflow,
//        softWrap = softWrap,
//        maxLines = maxLines,
//        style = style.copy(fontSize = currentFontSize),
//        onTextLayout =  { result ->
//            val resultDidOverflowWidth = result.didOverflowWidth
//            Log.d("MyLog", "Did result overflow width?: $resultDidOverflowWidth")
//            if (resultDidOverflowWidth) {
//                currentFontSize
//            } else {
//                readyToDraw = true
//            }
//        }
//    )
//}