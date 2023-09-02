package com.example.mypetprojectalculator.model

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import java.math.BigDecimal

class Utils {
    private val operators = mutableListOf("÷", "+", "-", "×")

    // The function for add numbers (0-9) on the display
    fun addNumberForScreen(
        number: String,
        enteredValues: MutableState<List<String>>
    ) {
        val currentValue = enteredValues.value
        val lastOperatorIndex = currentValue.indexOfLast { it in operators }
        val lastCommaIndex = currentValue.indexOfLast { it == "," }
        if (lastOperatorIndex == -1 && lastCommaIndex == -1) {
            // Перевірте кількість чисел, якщо немає жодного оператора або коми
            if (currentValue.size >= 15) {
                // Не додавайте більше чисел, якщо їх вже 15
                return
            }
        } else if (lastOperatorIndex != -1 && lastCommaIndex == -1) {
            // Перевірте кількість чисел після останнього оператора
            if (currentValue.drop(lastOperatorIndex + 1).size >= 15) {
                // Не додавайте більше чисел, якщо їх вже 15
                return
            }
        } else if (lastCommaIndex != -1) {
            // Перевірте кількість чисел після останньої коми
            if (currentValue.drop(lastCommaIndex + 1).size >= 18) {
                // Не додавайте більше чисел, якщо їх вже 18
                return
            }
        }
        if (currentValue == listOf("0")) {
            enteredValues.value = listOf(number)
        } else if (currentValue.last() == "0" && currentValue.size > 1
            && currentValue[currentValue.lastIndex - 1] in operators
        ) {
            enteredValues.value = currentValue.dropLast(1) + number
        } else {
            enteredValues.value = currentValue + number
        }
    }

    // The function for add symbols (+ - ÷ ×) on the display
    fun addOperatorsForScreen(
        symbol: String,
        enteredValues: MutableState<List<String>>
    ) {
        val lastSymbol = enteredValues.value.lastOrNull()
        if (lastSymbol == ",") {
            cleanupFloatingPointForScreen(enteredValues)
        }
        val currentSymbol = enteredValues.value

        if (lastSymbol == "÷" || lastSymbol == "+" || lastSymbol == "-" || lastSymbol == "×") {
            val newSymbol = currentSymbol.dropLast(1) + symbol
            enteredValues.value = newSymbol
        } else {
            enteredValues.value += listOf(symbol)
        }
    }

    // The function for delete the last character
    fun delLastCharForScreen(enteredValues: MutableState<List<String>>) {
        val currentAllElements = enteredValues.value
        if (currentAllElements.isNotEmpty()) {
            val newAllElements = currentAllElements.dropLast(1).toMutableList()
            if (newAllElements.isEmpty()) {
                newAllElements += "0"
                enteredValues.value = newAllElements
            } else {
                enteredValues.value = newAllElements
            }
        }
    }

    // The function for deletes all items
    fun delAllElementsForScreen(enteredValues: MutableState<List<String>>) {
        val currentAllElements = enteredValues.value
        if (currentAllElements.isNotEmpty()) {
            enteredValues.value = listOf("0")
        }
    }

    // The function for add floating point to a number
    fun addOrDropFloatingPointForScreen(
        symbol: String,
        enteredValues: MutableState<List<String>>
    ) {
        val copyEnteredValues = enteredValues
        val containsOperator = enteredValues.value.any { it in operators }
        val listOfSubLists = divisionIntoSublists(copyEnteredValues)
        val lastSubList = listOfSubLists.lastOrNull()
        val lastSymbolInLastSubList = lastSubList?.lastOrNull()
        val isFloatingInLastSubList = lastSubList?.contains(symbol)
        val isOperatorALastSubList = lastSubList?.any { it in operators }

        fun newEnteredValues(): MutableState<List<String>> {
            if (isFloatingInLastSubList == true) {
                if (lastSymbolInLastSubList == symbol) {
                    lastSubList.removeAt(lastSubList.size - 1)
                    listOfSubLists[listOfSubLists.size - 1] = lastSubList
                }
            } else {
                lastSubList?.add(symbol)
                if (lastSubList != null) {
                    listOfSubLists[listOfSubLists.size - 1] = lastSubList
                }
            }

            val newListOfSubLists = mutableStateOf(listOfSubLists.flatten())
            enteredValues.value = newListOfSubLists.value
            return enteredValues
        }
        if (containsOperator) {
            if (isOperatorALastSubList == false) {
                newEnteredValues()
            }
        } else newEnteredValues()
    }

    // The function for floating point cleanup
    private fun cleanupFloatingPointForScreen(enteredValues: MutableState<List<String>>) {
        val lastSymbol = enteredValues.value.lastOrNull()
        if (lastSymbol == ",") {
            enteredValues.value = enteredValues.value.dropLast(1)
        }
    }

    // The function for conversion to percentage
    fun convToPercentageForScreen(
        enteredValues: MutableState<List<String>>
    ): MutableList<MutableList<String>> {
        val listOfLists = divisionIntoSublists(enteredValues)
        val lastSublist = listOfLists.last()
        val containsOperator = lastSublist.any { it in operators }

        // Replace commas with dots
        val lastSublistWithDots = lastSublist.map { if (it == ",") "." else it }

        return if (!containsOperator) {
            val number = lastSublistWithDots.joinToString("").toDouble()
            val result = Calculation().conversionToPercentage(number)
            // Use BigDecimal.toPlainString() to format the result
            val newResult = BigDecimal(result.toString()).stripTrailingZeros().toPlainString()
            listOfLists[listOfLists.lastIndex] = convFromStringToMutable(newResult)
            listOfLists
        } else {
            listOfLists
        }
    }

    // The function for division into sublists
    private fun divisionIntoSublists(
        enteredValues: MutableState<List<String>>
    ): MutableList<MutableList<String>> {
        val result = mutableListOf<MutableList<String>>()
        var currentSublist = mutableListOf<String>()
        for (value in enteredValues.value) {
            if (value in operators) {
                if (currentSublist.isNotEmpty()) {
                    result.add(currentSublist)
                    currentSublist = mutableListOf<String>()
                }
                result.add(mutableListOf(value))
            } else {
                currentSublist.add(value)
            }
        }
        if (currentSublist.isNotEmpty()) {
            result.add(currentSublist)
        }
        return result
    }

    // The function to converts String to MutableList<String>
    private fun convFromStringToMutable(value: String): MutableList<String> {
        val newValue = value
            .replace(".", ",")
            .replace("/", "÷")
            .replace("*", "×")
            .toMutableList()
            .map { it.toString() }
            .toMutableList()
        return newValue
    }

    private fun convFromDoubleToString(
        value: Double
    ): String {
        val isIntResult = value % 1 == 0.0
        return if (isIntResult) {
            val resultToIntToString = value.toLong()
                .toString()
                .replace(".", ",")
            resultToIntToString
        } else {
            val newResult = BigDecimal(value.toString())
                .stripTrailingZeros()
                .toPlainString()
                .replace(".", ",")
            newResult
        }
    }

    // The function for add minus before a value
    fun addOrDropMinusBeforeLastSublist(
        enteredValues: MutableState<List<String>>
    ) {
        val listOfSubLists = divisionIntoSublists(enteredValues)
        val lastSubList = listOfSubLists.last()
        val isOperatorInLastSubList = lastSubList.any { it in operators }
        val containsOperator = listOfSubLists.any { subList ->
            subList.any { it in operators }
        }
        val numberOfLists = listOfSubLists.size
        val listOperators = mutableListOf(
            mutableListOf("÷"),
            mutableListOf("+"),
            mutableListOf("-"),
            mutableListOf("×")
        )
        val minusSublist = mutableListOf("-")
        val plusSublist = mutableListOf("+")
        val zeroSubList = mutableListOf("0")

        fun newEnteredValues(): MutableState<List<String>> {
            val newListOfSubLists = mutableStateOf(listOfSubLists.flatten())
            enteredValues.value = newListOfSubLists.value
            return enteredValues
        }

        if (containsOperator) {
            if (isOperatorInLastSubList) { // Перевіряю чи останій підсписок має оператор
                if (numberOfLists < 3) { // Перевіряю кількість підсписків
                    listOfSubLists.add(0, minusSublist)
                    newEnteredValues()
                } else if (numberOfLists == 3) {
                    val operatorBeforeValue = listOfSubLists[0]
                    if (operatorBeforeValue == minusSublist) {
                        listOfSubLists.removeAt(0)
                        newEnteredValues()
                    }
                } else {
                    val operatorBeforeValue = listOfSubLists[numberOfLists - 3]
                    if (operatorBeforeValue == plusSublist) {
                        listOfSubLists[numberOfLists - 3] = minusSublist
                        newEnteredValues()
                    } else if (operatorBeforeValue == minusSublist) {
                        if (listOfSubLists[numberOfLists - 4] in listOperators) {
                            listOfSubLists.removeAt(numberOfLists - 3)
                            newEnteredValues()
                        } else {
                            listOfSubLists[numberOfLists - 3] = plusSublist
                            newEnteredValues()
                        }
                    } else {
                        listOfSubLists.add(numberOfLists - 2, minusSublist)
                        newEnteredValues()
                    }
                }
            } else {
                val operatorBeforeValue = listOfSubLists[numberOfLists - 2]
                if (numberOfLists < 3 && operatorBeforeValue == minusSublist) { // Перевіряю кількість підсписків
                    listOfSubLists.removeAt(0)
                    newEnteredValues()
                } else if (operatorBeforeValue == plusSublist) {
                    listOfSubLists[numberOfLists - 2] = minusSublist
                    newEnteredValues()
                } else if (operatorBeforeValue == minusSublist) {
                    if (listOfSubLists[numberOfLists - 3] in listOperators) {
                        listOfSubLists.removeAt(numberOfLists - 2)
                        newEnteredValues()
                    } else {
                        listOfSubLists[numberOfLists - 2] = plusSublist
                        newEnteredValues()
                    }
                } else {
                    listOfSubLists.add(numberOfLists - 1, minusSublist)
                    newEnteredValues()
                }
            }
        } else {
            if (lastSubList == zeroSubList) {
                newEnteredValues()
            } else {
                listOfSubLists.add(listOfSubLists.size - 1, minusSublist)
                newEnteredValues()
            }
        }
    }

    // The function for obtaining the result of the calculation
    fun resultCalculation(
        enteredValues: MutableState<List<String>>,
    ): MutableList<MutableList<String>> {
        val valuesForCalculation = enteredValues.value.joinToString("")
        val formattedValue = valuesForCalculation.replace("(-", "(0-")
        val resultCalculation = Calculation().evaluateExpression(formattedValue)

        val isIntResultCalculator = resultCalculation % 1 == 0.0

        val finalResult: MutableList<MutableList<String>>

        return if (isIntResultCalculator) {
            val resultCalculationString = resultCalculation.toLong().toString()
            val resultCalculationToMutableList = convFromStringToMutable(resultCalculationString)
            finalResult = mutableListOf(resultCalculationToMutableList)
            finalResult
        } else {
            // Use BigDecimal.toPlainString() to format the result
            val newResult =
                BigDecimal(resultCalculation.toString()).stripTrailingZeros().toPlainString()
            val newResultToMutableList = convFromStringToMutable(newResult)
            finalResult = mutableListOf(newResultToMutableList)
            finalResult
        }
    }

    // The function for dynamic result
    fun dynamicResult(
        enteredValues: MutableState<List<String>>
    ): String {
        val copyEnteredValues = enteredValues
        val lastSymbolInListOfLists = copyEnteredValues.value.lastOrNull()
        val isLastSymbolAFloating = lastSymbolInListOfLists == ","
        val isLastSymbolAnOperator = lastSymbolInListOfLists in operators
        val containsOperator = copyEnteredValues.value.any { it in operators }

        fun result(
            values: MutableState<List<String>>
        ): String {
            val expression = values.value.joinToString("")
            val formattedExpression = expression.replace("(-", "(0-")
            val result = Calculation().evaluateExpression(formattedExpression)
            return convFromDoubleToString(result)
        }

        return if (containsOperator || isLastSymbolAFloating) {
            if (isLastSymbolAnOperator) {
                val values = mutableStateOf(copyEnteredValues.value.dropLast(1))
                val isOperatorInValues = values.value.any { it in operators }
                if (isOperatorInValues) {
                    result(values)
                } else {
                    values.value.joinToString("")
                }
            } else if (isLastSymbolAFloating) {
                val values = mutableStateOf(copyEnteredValues.value + "0")
                val isOperatorInValues = values.value.any { it in operators }
                if (isOperatorInValues) {
                    result(values)
                } else {
                    values.value.joinToString("")
                }
            } else result(copyEnteredValues)

        } else copyEnteredValues.value.joinToString("")
    }

    fun formatNumbersWithThousands(input: List<String>): String {
        val result = mutableListOf<String>()
        var temp = mutableListOf<String>()
        for (item in input) {
            if (item in operators) {
                result.add(temp.joinToString(""))
                result.add(item)
                temp = mutableListOf<String>()
            } else {
                temp.add(item)
            }
        }
        result.add(temp.joinToString(""))
        return result.joinToString("") { part ->
            if (part in operators) {
                part
            } else {
                val split = part.split(",")
                if (split.size == 2) {
                    val beforeComma = split[0].reversed().chunked(3).joinToString(" ").reversed()
                    "$beforeComma,${split[1]}"
                } else {
                    if (part.startsWith("0") || part.length < 4) {
                        part
                    } else {
                        part.reversed().chunked(3).joinToString(" ").reversed()
                    }
                }
            }
        }
    }

    fun dropAnLastOperator(
        enteredValues: MutableState<List<String>>
    ): MutableState<List<String>> {
        val currentEnteredValue = enteredValues.value
        val lastSymbolInEnteredValues = currentEnteredValue.lastOrNull()
        return if (lastSymbolInEnteredValues in operators) {
            enteredValues.value = currentEnteredValue.dropLast(1).toMutableList()
            enteredValues
        } else {
            enteredValues.value = currentEnteredValue.toMutableList()
            enteredValues
        }
    }
}