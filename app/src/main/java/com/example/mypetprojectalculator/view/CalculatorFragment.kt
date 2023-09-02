package com.example.mypetprojectalculator.view

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mypetprojectalculator.R
import com.example.mypetprojectalculator.model.CalculationOfElementsSize
import com.example.mypetprojectalculator.viewmodel.CalculatorViewModel
import com.example.mypetprojectalculator.model.OperationHistory
import com.example.mypetprojectalculator.model.SettingsManager
import com.example.mypetprojectalculator.model.Utils
import com.example.mypetprojectalculator.ui.theme.ColorDivider
import com.example.mypetprojectalculator.ui.theme.ColorContentOnButtons
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import com.example.mypetprojectalculator.model.AutoSizeText

// Constants for numbers and symbols
const val NUMBER_ZERO = "0"
const val NUMBER_ONE = "1"
const val NUMBER_TWO = "2"
const val NUMBER_THREE = "3"
const val NUMBER_FOUR = "4"
const val NUMBER_FIVE = "5"
const val NUMBER_SIX = "6"
const val NUMBER_SEVEN = "7"
const val NUMBER_EIGHT = "8"
const val NUMBER_NINE = "9"
const val SYMBOL_PLUS = "+"
const val SYMBOL_MINUS = "—"
const val SYMBOL_MINUS_FOR_CALCULATION = "-"
const val SYMBOL_MULTIPLICATION = "×"
const val SYMBOL_DIVISION = "÷"
const val SYMBOL_FLOATING_POINT = ","

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorFragment(
    context: Context,
    calculatorViewModel: CalculatorViewModel,
    onButtonClick: () -> Unit
) {
    val operators = listOf("+", "-", "×", "÷")

    // Create a settings manager object for managing settings
    val settingsManager = SettingsManager(context)

    // Load icons
    val iconDelete = painterResource(id = R.drawable.ic_backspace)
    val iconSettings = painterResource(id = R.drawable.ic_settings)
    val iconPlusAndMinus = painterResource(id = R.drawable.ic_plus_and_minus)

    // Get the displayCalculator value from the calculatorViewModel
    val enteredValues = remember {
        calculatorViewModel.enteredValues
    }

    var showResult by remember { mutableStateOf(false) }
    val resultEnterValues = remember {
        mutableStateOf(listOf(""))
    }
    val resultEnterValuesForDisplay = "= " + resultEnterValues.value.joinToString("")

    val operationsHistory = remember { mutableStateListOf<OperationHistory>() }
    var newOperation = OperationHistory(
        "",
        ""
    )

    val lazyListState = rememberLazyListState()

    val coroutine = rememberCoroutineScope()
    LaunchedEffect(coroutine) {
        operationsHistory.add(newOperation)
        lazyListState.scrollToItem(operationsHistory.lastIndex)
    }

    val textStyleBodyMedium = MaterialTheme.typography.bodyMedium
    var textStyle by remember { mutableStateOf(textStyleBodyMedium) }
    var readyToDraw by remember { mutableStateOf(false) }

    // Calculate the dynamic result
    val dynamicResult = Utils().dynamicResult(enteredValues)
    val newDynamicResult = dynamicResult.map { it.toString() }

    // Set the status bar color
    StatusBarColor(context = context)

    @Composable
    fun PressableButton(
        text: String,
        icon: Painter? = null,
        contentDescription: String = "",
        useText: Boolean = true,
        maxWidthBox: Dp,
        maxHeightBox: Dp,
        onClick: () -> Unit
    ) {
        // Create an interaction source to track button press state
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()

        // Define the colors for the button based on the press state
        val listFunButtons =
            listOf("iconDelete", "iconPlusAndMinus", "iconSettings", "AC", "%", "÷", "+", "—", "×")
        val colorButton = if (isPressed) settingsManager.colorPressableButton()
        else if (text == "=") ColorContentOnButtons else Color.Transparent
        val colorContent = if (text in listFunButtons)
            ColorContentOnButtons else if (text == "=") Color.White else settingsManager.colorContentNumber()

        // Calculate the button size, text size, and icon size based on the constraints
        val density = LocalDensity.current
        val calculationOfElementsSize =
            CalculationOfElementsSize(maxWidthBox, maxHeightBox, density)
        val buttonSize = calculationOfElementsSize.calculateButtonSize()
        val textSize = calculationOfElementsSize.calculateTextSizeOnButton()
        val iconSize = calculationOfElementsSize.calculateIconSizeOnButton()

        // Create the button
        Button(
            onClick = onClick,
            modifier = Modifier
                .height(buttonSize.second)
                .width(buttonSize.first)
                .padding(3.dp),
            shape = ShapeDefaults.ExtraSmall,
            colors = ButtonDefaults.buttonColors(containerColor = colorButton),
            border = BorderStroke(1.dp, ColorDivider),
            contentPadding = PaddingValues(0.dp),
            interactionSource = interactionSource
        ) {
            if (useText) {
                // If useText is true, display the button text
                Text(
                    text = text,
                    color = colorContent,
                    fontSize = textSize,
                    fontFamily = FontFamily.SansSerif
                )
            } else {
                // If useText is false, display the button icon
                icon?.let {
                    Icon(
                        painter = it,
                        contentDescription = contentDescription,
                        modifier = Modifier.size(iconSize),
                        tint = colorContent
                    )
                }
            }
        }
    }

    @Composable
    fun TextForLazyColumn(
        text: String,
        maxWidthBox: Dp,
        maxHeightBox: Dp,
        density: Density
    ) {
        val calculationOfElementsSize =
            CalculationOfElementsSize(maxWidthBox, maxHeightBox, density)
        val fontSizeText = calculationOfElementsSize.calculateTextSize(0.07f)

        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .wrapContentWidth(Alignment.End)
                .wrapContentHeight(Alignment.CenterVertically),
            color = settingsManager.colorContentNumber(),
            fontSize = fontSizeText
        )
    }

//======================================== Start creating the UI ==================================
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(settingsManager.colorBackground())
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Create the output screen
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .padding(
                        start = 45.dp,
                        top = 25.dp,
                        end = 45.dp,
                        bottom = 10.dp
                    ),
                contentAlignment = Alignment.BottomEnd
            ) {
                val density = LocalDensity.current
                val widthBoxFirst = maxWidth
                val calculationOfElementsSize =
                    CalculationOfElementsSize(maxWidth, maxHeight, density)
                val textSizeText1 = calculationOfElementsSize.calculateTextSize(0.15f)
                val textSizeText2 = calculationOfElementsSize.calculateTextSize(0.11f)
                val textSizeForResultText = calculationOfElementsSize.calculateTextSize(0.14f)

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.End
                ) {
                    BoxWithConstraints() {
                        val densityBox = LocalDensity.current
                        val widthBox = maxWidth
                        // Display the operations history
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.6f),
                            state = lazyListState,
                            reverseLayout = true,
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.End
                        ) {
                            items(operationsHistory.reversed()) { operation ->
                                if (operation.result != "" && enteredValues.value.isNotEmpty()) {
                                    val newOperationResult = operation.result.map { it.toString() }
                                    Text(text = "")
                                    TextForLazyColumn(
                                        text = "=" + Utils().formatNumbersWithThousands(
                                            newOperationResult
                                        ),
                                        maxWidth, maxHeight,
                                        densityBox
                                    )
                                    TextForLazyColumn(
                                        text = Utils().formatNumbersWithThousands(
                                            operation.expression.map { it.toString() }
                                        ),
                                        maxWidth,
                                        maxHeight,
                                        densityBox
                                    )
                                }
                            }
                        }
                    }

                    if (showResult == true) {
                        // Display the calculator screen value
                        Text(
                            text = Utils().formatNumbersWithThousands(
                                resultEnterValuesForDisplay.map { it.toString() }
                            ),
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(0.9f)
                                .wrapContentHeight(Alignment.Bottom),
                            color = settingsManager.colorContentNotNumber(),
                            fontSize = textSizeForResultText,
                            textAlign = TextAlign.End,
                            lineHeight = 50.sp
                        )
                    } else {
                        // Display the calculator screen value
//                        // Спосіб 4
//                        AutoSizeText(
//                            text = Utils().formatNumbersWithThousands(enteredValues.value.toList()),
//                            fontSize = textSizeText1,
//                            minFontSize = textSizeText1/2,
//                            containerWidth = widthBoxFirst,
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .weight(0.9f)
//                                .wrapContentHeight(Alignment.Bottom),
//                            color = settingsManager.colorContentNotNumber(),
//                            textAlign = TextAlign.End,
//                            lineHeight = 50.sp
//                        )
                        // Спосіб 3
                        AutoSizeText(
                            text = Utils().formatNumbersWithThousands(enteredValues.value.toList()),
                            minFontSize = textSizeText1/2,
                            density = density,
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(0.9f)
                                .wrapContentHeight(Alignment.Bottom),
                            color = settingsManager.colorContentNotNumber(),
//                            fontSize = textSizeText1,
                            textAlign = TextAlign.End,
                            lineHeight = 50.sp,
                            style = TextStyle(fontSize = textSizeText1)
                        )
                        // Спосіб 1
//                        AutoSizeText(
//                            text = Utils().formatNumbersWithThousands(enteredValues.value.toList()),
//                            overflow = TextOverflow.Clip,
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .weight(0.9f)
//                                .wrapContentHeight(Alignment.Bottom),
//                            color = settingsManager.colorContentNotNumber(),
//                            fontSize = textSizeText1,
//                            textAlign = TextAlign.End,
//                            lineHeight = 50.sp,
//                            constraint = AutoSizeConstraint.Width()
//                        )
//                        Text(
//                            text = Utils().formatNumbersWithThousands(enteredValues.value.toList()),
//                            style = textStyle,
//                            overflow = TextOverflow.Clip,
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .weight(0.9f)
//                                .wrapContentHeight(Alignment.Bottom)
//                                .drawWithContent {
//                                    if (readyToDraw) drawContent()
//                                },
//                            color = settingsManager.colorContentNotNumber(),
//                            fontSize = textSizeText1,
//                            textAlign = TextAlign.End,
//                            lineHeight = 50.sp,
//                            onTextLayout = { textLayoutResult ->
//                                if (textLayoutResult.didOverflowHeight) {
//                                    textStyle = textStyle.copy(fontSize = textStyle.fontSize * 0.9)
//                                } else {
//                                    readyToDraw = true
//                                }
//                            }
//                        )
                        // Display the dynamic result
                        Text(
                            text = "= " + Utils().formatNumbersWithThousands(newDynamicResult),
                            fontSize = textSizeText2,
                            color = settingsManager.colorContentNumber(),
                            textAlign = TextAlign.Right
                        )
                    }
                }

            }

            // Add the dividing line
            Divider(
                color = ColorDivider,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 45.dp,
                        end = 45.dp
                    )
            )

            // Create a box for the buttons
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(30.dp)
            ) {
                val maxWidthBox = maxWidth
                val maxHeightBox = maxHeight

                // Add function buttons
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val buttons = listOf(
                        listOf("AC", "iconDelete", "%", SYMBOL_DIVISION),
                        listOf(NUMBER_SEVEN, NUMBER_EIGHT, NUMBER_NINE, SYMBOL_MULTIPLICATION),
                        listOf(NUMBER_FOUR, NUMBER_FIVE, NUMBER_SIX, SYMBOL_MINUS),
                        listOf(NUMBER_ONE, NUMBER_TWO, NUMBER_THREE, SYMBOL_PLUS),
                        listOf("iconPlusAndMinus", NUMBER_ZERO, SYMBOL_FLOATING_POINT, "=")
                    )

                    buttons.forEach { rowButtons ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            rowButtons.forEach { button ->
                                // Create a pressable button
                                PressableButton(
                                    text = button,
                                    icon = if (button == "iconPlusAndMinus") iconPlusAndMinus
                                    else if (button == "iconDelete") iconDelete else null,
                                    contentDescription = if (button == "iconPlusAndMinus") "plusAndMinus"
                                    else if (button == "iconDelete") "delete" else "",
                                    useText = if (button == "iconPlusAndMinus") false
                                    else if (button == "iconDelete") false else true,
                                    maxWidthBox = maxWidthBox,
                                    maxHeightBox = maxHeightBox,
                                    onClick = {
                                        if (button != "=") {
                                            showResult = false
                                        }
                                        when (button) {
                                            "iconDelete" -> Utils().delLastCharForScreen(
                                                enteredValues
                                            )

                                            "iconPlusAndMinus" -> Utils().addOrDropMinusBeforeLastSublist(
                                                enteredValues
                                            )

                                            "AC" -> Utils().delAllElementsForScreen(enteredValues)
                                            "%" -> {
                                                val newMutableList =
                                                    Utils().convToPercentageForScreen(
                                                        enteredValues
                                                    )
                                                enteredValues.value = newMutableList.flatten()
                                            }

                                            SYMBOL_DIVISION -> Utils().addOperatorsForScreen(
                                                SYMBOL_DIVISION,
                                                enteredValues
                                            )

                                            SYMBOL_MULTIPLICATION -> Utils().addOperatorsForScreen(
                                                SYMBOL_MULTIPLICATION,
                                                enteredValues
                                            )

                                            SYMBOL_MINUS -> Utils().addOperatorsForScreen(
                                                SYMBOL_MINUS_FOR_CALCULATION,
                                                enteredValues
                                            )

                                            SYMBOL_PLUS -> Utils().addOperatorsForScreen(
                                                SYMBOL_PLUS,
                                                enteredValues
                                            )

                                            NUMBER_ZERO -> Utils().addNumberForScreen(
                                                NUMBER_ZERO,
                                                enteredValues
                                            )

                                            NUMBER_SEVEN -> Utils().addNumberForScreen(
                                                NUMBER_SEVEN,
                                                enteredValues
                                            )

                                            NUMBER_EIGHT -> Utils().addNumberForScreen(
                                                NUMBER_EIGHT,
                                                enteredValues
                                            )

                                            NUMBER_NINE -> Utils().addNumberForScreen(
                                                NUMBER_NINE,
                                                enteredValues
                                            )

                                            NUMBER_FOUR -> Utils().addNumberForScreen(
                                                NUMBER_FOUR,
                                                enteredValues
                                            )

                                            NUMBER_FIVE -> Utils().addNumberForScreen(
                                                NUMBER_FIVE,
                                                enteredValues
                                            )

                                            NUMBER_SIX -> Utils().addNumberForScreen(
                                                NUMBER_SIX,
                                                enteredValues
                                            )

                                            NUMBER_ONE -> Utils().addNumberForScreen(
                                                NUMBER_ONE,
                                                enteredValues
                                            )

                                            NUMBER_TWO -> Utils().addNumberForScreen(
                                                NUMBER_TWO,
                                                enteredValues
                                            )

                                            NUMBER_THREE -> Utils().addNumberForScreen(
                                                NUMBER_THREE,
                                                enteredValues
                                            )

                                            SYMBOL_FLOATING_POINT -> {
                                                Utils().addOrDropFloatingPointForScreen(
                                                    SYMBOL_FLOATING_POINT,
                                                    enteredValues
                                                )
                                            }

                                            "=" -> {
                                                Utils().dropAnLastOperator(enteredValues)
                                                val lastEnteredValues =
                                                    enteredValues.value.joinToString("")
                                                val newMutableList =
                                                    Utils().resultCalculation(enteredValues)
                                                enteredValues.value = newMutableList.flatten()
                                                resultEnterValues.value = enteredValues.value

                                                val isLastEnteredValuesZero =
                                                    lastEnteredValues == "0"

                                                // Check if the result is zero
                                                val isResultZero =
                                                    resultEnterValues.value.joinToString("") == "0"

                                                if (isResultZero) {
                                                    resultEnterValues.value = listOf("0")
                                                }

                                                if (!isLastEnteredValuesZero) {
                                                    newOperation = OperationHistory(
                                                        lastEnteredValues,
                                                        if (isResultZero) "0" else dynamicResult
                                                    )
                                                }

                                                operationsHistory.add(newOperation)

                                                showResult = true

                                                if (lastEnteredValues.equals(
                                                        enteredValues.value.joinToString(
                                                            ""
                                                        )
                                                    )
                                                ) {
                                                    Utils().delAllElementsForScreen(
                                                        enteredValues
                                                    )
                                                }
                                            }

                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

