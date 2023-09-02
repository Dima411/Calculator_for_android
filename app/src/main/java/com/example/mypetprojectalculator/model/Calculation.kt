package com.example.mypetprojectalculator.model

import android.util.Log
import java.math.RoundingMode
import java.util.Stack

/*
* The class for mathematical calculations
*/

class Calculation() {
    // The function for conversion to percentage
    fun conversionToPercentage(value: Double): Double {
        return value / 100
    }

    // The function to calculate all values
    fun evaluateExpression(expression: String): Double {
        // Replace symbols in the expression
        val normalizedExpression = expression
            .replace("÷", "/")
            .replace("×", "*")
            .replace(",", ".")

        val operatorPrecedence = mapOf(
            "+" to 1,
            "-" to 1,
            "*" to 2,
            "/" to 2,
            "u-" to 3 // Add precedence for unary minus
        )
        val operators = operatorPrecedence.keys
        val operatorStack = Stack<String>()
        val outputQueue = ArrayDeque<String>()

        // Tokenize the expression
        var previousToken: String? = null
        val tokens = mutableListOf<String>()
        var currentToken = ""
        for (char in normalizedExpression) {
            if (char.isDigit() || char == '.') {
                currentToken += char
            } else {
                if (currentToken.isNotEmpty()) {
                    tokens.add(currentToken)
                    currentToken = ""
                }
                if (char == '-' && (previousToken == null || previousToken in operators || previousToken == "(")) {
                    // Treat the current "-" as a unary minus
                    tokens.add("u-")
                } else {
                    tokens.add(char.toString())
                }
            }
            previousToken = char.toString()
        }
        if (currentToken.isNotEmpty()) {
            tokens.add(currentToken)
        }

        // This function checks whether a string can be converted to a number in Double format
        fun String.isDouble(): Boolean = this.toDoubleOrNull() != null

        // Convert the expression to Reverse Polish Notation using the shunting-yard algorithm
        for (token in tokens) {
            when {
                token.isDouble() -> outputQueue.addLast(token)
                token in operators -> {
                    while (operatorStack.isNotEmpty() && operatorStack.peek() in operators &&
                        operatorPrecedence[operatorStack.peek()]!! >= operatorPrecedence[token]!!
                    ) {
                        outputQueue.addLast(operatorStack.pop())
                    }
                    operatorStack.push(token)
                }
                token == "(" -> operatorStack.push(token)
                token == ")" -> {
                    while (operatorStack.peek() != "(") {
                        outputQueue.addLast(operatorStack.pop())
                    }
                    operatorStack.pop()
                }
            }
        }
        while (operatorStack.isNotEmpty()) {
            outputQueue.addLast(operatorStack.pop())
        }

        // Evaluate the RPN expression
        val operandStack = Stack<Double>()
        for (token in outputQueue) {
            when {
                token.isDouble() -> operandStack.push(token.toDouble())
                token in operators -> {
                    if (token == "u-") {
                        // Handle unary minus
                        if (operandStack.isEmpty()) {
                            throw IllegalArgumentException("Invalid expression: not enough operands for unary minus")
                        }
                        val operand = operandStack.pop()
                        operandStack.push(-operand)
                    } else {
                        if (operandStack.size < 2) {
                            throw IllegalArgumentException("Invalid expression: not enough operands for binary operator $token")
                        }
                        val rightOperand = operandStack.pop()
                        val leftOperand = operandStack.pop()

                        val result = when (token) {
                            "+" -> leftOperand + rightOperand
                            "-" -> leftOperand - rightOperand
                            "*" -> leftOperand * rightOperand
                            "/" -> {
                                if (rightOperand == 0.0) {
                                    // Handle division by zero
                                    operandStack.push(0.0) // Push 0 as the result
                                    continue
                                }
                                leftOperand / rightOperand
                            }
                            else -> throw IllegalArgumentException("Unknown operator: $token")
                        }
                        operandStack.push(result)
                    }
                }
            }
        }
        if (operandStack.isEmpty()) {
            throw IllegalArgumentException("Invalid expression: no result on the operand stack")
        }
        val result = operandStack.pop()
        val roundedResult = result.toBigDecimal().setScale(9, RoundingMode.HALF_UP).toDouble()
        return roundedResult
    }

}
