package com.example.mypetprojectalculator.view

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.mypetprojectalculator.R
import com.example.mypetprojectalculator.model.CalculationOfElementsSize
import com.example.mypetprojectalculator.viewmodel.CalculatorViewModel
import com.example.mypetprojectalculator.model.SettingsManager
import com.example.mypetprojectalculator.ui.theme.ColorContentOnButtons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsFragment(
    context: Context,
    calculatorViewModel: CalculatorViewModel,
    onButtonClick: () -> Unit
) {
    val settingsManager = SettingsManager(context)
    val enteredValues = remember {
        calculatorViewModel.enteredValues
    }

    val lazyListState = rememberLazyListState()

    StatusBarColor(context = context)

    @Composable
    fun PressableButtonInSetting(
        text: String,
        underLine: Boolean = false,
        fontSize: TextUnit,
        strokeWidthUnderLine: Float,
        onClick: () -> Unit
    ) {
        val underlineOffset by animateFloatAsState(if (underLine) 1f else 0f)

        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(Color.Transparent)
        ) {

            Text(
                text = text,
                color = settingsManager.colorContentNumber(),
                fontSize = fontSize,
                modifier = Modifier
                    .drawWithContent {
                        drawContent()
                        if (underLine) {
                            val lineY = size.height - underlineOffset
                            drawLine(
                                color = ColorContentOnButtons,
                                start = Offset(0f, lineY),
                                end = Offset(size.width, lineY),
                                strokeWidth = strokeWidthUnderLine
                            )
                        }
                    }
                    .padding(0.dp)
            )
        }

    }

    @Composable
    fun TextSettingName(
        text: String,
        fontSize: TextUnit
    ) {
        Text(
            text = text,
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(Alignment.CenterVertically),
            color = settingsManager.colorContentNotNumber(),
            fontSize = fontSize,
        )

    }

    @Composable
    fun TextThanks(
        text: String,
        fontSize: TextUnit
    ) {
        var textRemember by remember { mutableStateOf(text) }

        LaunchedEffect(settingsManager.currentLanguage.value) {
            // Змінюємо значення textRemember на новий перекладений текст
            textRemember = settingsManager.translatedText(R.string.app_text_Gratitude)
        }

        TextField(
            value = textRemember,
            onValueChange = { textRemember = it },
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .wrapContentHeight(align = Alignment.CenterVertically),
            textStyle = TextStyle(
                fontSize = fontSize,
                color = settingsManager.colorContentNotNumber(),
                textAlign = TextAlign.Center
            ),
            enabled = false,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            )
        )
    }

    fun updateLanguage(language: String) {
        settingsManager.saveLanguage(language)
        settingsManager.currentLanguage.value = language
        settingsManager.applyLanguage()
    }

// ======================================== Layout settings =====================================
    // menu settings
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(settingsManager.colorBackground())
            .padding(start = 35.dp, top = 46.dp, end = 35.dp, bottom = 50.dp)
    ) {
        val density = LocalDensity.current
        val calculationOfElementsSize =
            CalculationOfElementsSize(maxWidth, maxHeight, density)
        val textSizeTitle = calculationOfElementsSize.calculateTextSize(0.06f)
        val textSizeSettings = calculationOfElementsSize.calculateTextSize(0.055f)
        val textSizeThanks = calculationOfElementsSize.calculateTextSize(0.05f)
        val buttonBackSize = calculationOfElementsSize.calculateButtonSize()
        val iconOnButtonBackSize = calculationOfElementsSize.calculateIconSizeOnButton() * 1.5f
        val strokeWidthUnderLine = if (maxHeight < 460.dp && maxWidth < 340.dp) 1f else 5f
        val fillMaxHeightBoxForSettings = if (maxHeight < 460.dp && maxWidth < 340.dp) 0.45f else 0.3f

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // The button - go back
            Button(
                onClick = onButtonClick,
                modifier = Modifier
                    .height(buttonBackSize.second / 2)
                    .width(buttonBackSize.second / 2)
                    .offset(x = (-25.dp / 2)),
                shape = ShapeDefaults.ExtraLarge,
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "arrow_back",
                    modifier = Modifier.size(iconOnButtonBackSize / 3),
                    tint = settingsManager.colorContentNotNumber()
                )
            }

            // The text is settings title
            Text(
                text = settingsManager.translatedText(R.string.app_title_settings),
                modifier = Modifier
                    .fillMaxWidth(),
                color = settingsManager.colorContentNotNumber(),
                fontSize = textSizeTitle
            )

            // Box for settings
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fillMaxHeightBoxForSettings)
            )
            {
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxHeight(),
                ) {
                    // Box with settings names
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .alignBy(FirstBaseline),
                        contentAlignment = Alignment.Center

                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            TextSettingName(
                                text = settingsManager.translatedText(R.string.app_title_theme_settings) + ":",
                                fontSize = textSizeSettings
                            )
                            TextSettingName(
                                text = settingsManager.translatedText(R.string.app_title_language_settings) + ":",
                                fontSize = textSizeSettings
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // Box with parameters settings
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // The button is light theme
                                PressableButtonInSetting(
                                    text = settingsManager.translatedText(R.string.app_name_settings_theme_light),
                                    underLine = settingsManager.currentTheme.value == "light",
                                    fontSize = textSizeSettings,
                                    strokeWidthUnderLine = strokeWidthUnderLine
                                ) {
                                    settingsManager.saveTheme("light")
                                    settingsManager.currentTheme.value = "light"
                                }

                                // The button is black theme
                                PressableButtonInSetting(
                                    text = settingsManager.translatedText(R.string.app_name_settings_theme_black),
                                    underLine = settingsManager.currentTheme.value == "dark",
                                    fontSize = textSizeSettings,
                                    strokeWidthUnderLine = strokeWidthUnderLine
                                ) {
                                    settingsManager.saveTheme("dark")
                                    settingsManager.currentTheme.value = "dark"
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // The button is UK
                                PressableButtonInSetting(
                                    text = "UK",
                                    underLine = settingsManager.currentLanguage.value == "uk",
                                    fontSize = textSizeSettings,
                                    strokeWidthUnderLine = strokeWidthUnderLine
                                ) {
                                    updateLanguage("uk")
                                }

                                // The button is EN
                                PressableButtonInSetting(
                                    text = "EN",
                                    underLine = settingsManager.currentLanguage.value == "en",
                                    fontSize = textSizeSettings,
                                    strokeWidthUnderLine = strokeWidthUnderLine
                                ) {
                                    updateLanguage("en")
                                }
                            }
                        }
                    }
                }
            }

// ------------------------------------------ Gratitude ------------------------------------------
            TextThanks(
                text = settingsManager.translatedText(R.string.app_text_Gratitude),
                fontSize = textSizeThanks
            )


        }
    }

}