package com.example.lr1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val operand1 = findViewById<EditText>(R.id.operand1)
        val operand2 = findViewById<EditText>(R.id.operand2)
        val operationText = findViewById<TextView>(R.id.operationText)
        val resultText = findViewById<TextView>(R.id.resultText)

        findViewById<Button>(R.id.addButton).setOnClickListener {
            applyOperation(operand1, operand2, operationText, resultText, "+")
        }
        findViewById<Button>(R.id.subButton).setOnClickListener {
            applyOperation(operand1, operand2, operationText, resultText, "-")
        }
        findViewById<Button>(R.id.mulButton).setOnClickListener {
            applyOperation(operand1, operand2, operationText, resultText, "*")
        }
        findViewById<Button>(R.id.divButton).setOnClickListener {
            applyOperation(operand1, operand2, operationText, resultText, "/")
        }
        findViewById<Button>(R.id.clearButton).setOnClickListener {
            operand1.text.clear()
            operand2.text.clear()
            operationText.setText(R.string.operation_add)
            resultText.text = ""
        }
    }

    private fun applyOperation(
        operand1: EditText,
        operand2: EditText,
        operationText: TextView,
        resultText: TextView,
        symbol: String
    ) {
        operationText.text = symbol

        val left = parseNumber(operand1.text.toString())
        val right = parseNumber(operand2.text.toString())
        if (left == null || right == null) {
            resultText.setText(R.string.error_value)
            return
        }
        if (symbol == "/" && right == 0.0) {
            resultText.setText(R.string.error_divide_by_zero)
            return
        }

        val value = when (symbol) {
            "+" -> left + right
            "-" -> left - right
            "*" -> left * right
            "/" -> left / right
            else -> return
        }
        resultText.text = if (value.isFinite()) formatResult(value) else getString(R.string.error_divide_by_zero)
    }

    private fun parseNumber(raw: String): Double? {
        val normalized = raw.trim().replace(',', '.')
        if (normalized.isEmpty()) return null
        return normalized.toDoubleOrNull()
    }

    private fun formatResult(value: Double): String {
        val rounded = BigDecimal.valueOf(value).setScale(8, RoundingMode.HALF_UP).stripTrailingZeros()
        val shown = if (rounded.scale() <= 0) rounded.setScale(1) else rounded
        return shown.toPlainString().let { text ->
            if (text == "-0.0") "0.0" else text
        }
    }
}
