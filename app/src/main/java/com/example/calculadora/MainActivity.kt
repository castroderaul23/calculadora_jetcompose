package com.example.calculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculadora.ui.theme.CalculadoraTheme
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CalculatorScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen(modifier: Modifier = Modifier) {
    // Estados principais
    var display by rememberSaveable { mutableStateOf("0") }
    var firstOperand by rememberSaveable { mutableStateOf<Double?>(null) }
    var pendingOperator by rememberSaveable { mutableStateOf<String?>(null) }
    var waitingForSecond by rememberSaveable { mutableStateOf(false) }
    var lastWasEquals by rememberSaveable { mutableStateOf(false) }

    // Formatação do resultado para evitar .0 desnecessário
    fun formatResult(value: Double): String {
        val longVal = value.toLong()
        return if (value == longVal.toDouble()) longVal.toString() else value.toString()
    }

    // Aplica operador entre a e b
    fun applyOperator(op: String, a: Double, b: Double): Double {
        return when (op) {
            "+" -> a + b
            "-" -> a - b
            "*" -> a * b
            "/" -> if (b == 0.0) Double.NaN else a / b
            "^" -> a.pow(b)
            else -> b
        }
    }

    // Entrada de dígitos
    fun inputNumber(digit: String) {
        if (waitingForSecond || lastWasEquals) {
            // começa novo número
            display = digit
            waitingForSecond = false
            lastWasEquals = false
        } else {
            display = if (display == "0") digit else display + digit
        }
    }

    // Entrada do ponto decimal
    fun inputDecimal() {
        if (waitingForSecond || lastWasEquals) {
            display = "0."
            waitingForSecond = false
            lastWasEquals = false
        } else if (!display.contains(".")) {
            display += "."
        }
    }

    // Limpa tudo (AC)
    fun clearAll() {
        display = "0"
        firstOperand = null
        pendingOperator = null
        waitingForSecond = false
        lastWasEquals = false
    }

    // Apaga último dígito (⌫)
    fun backspace() {
        if (waitingForSecond || lastWasEquals) {
            // se estava esperando segundo operando ou acabou de pressionar =, volta para 0
            display = "0"
            waitingForSecond = false
            lastWasEquals = false
            return
        }
        display = if (display.length <= 1) "0" else display.dropLast(1)
    }

    // Quando um operador é pressionado
    fun onOperator(opLabel: String) {
        // normaliza símbolos visuais para operadores reais
        val op = when (opLabel) {
            "x", "×" -> "*"
            "÷" -> "/"
            else -> opLabel
        }

        val current = display.toDoubleOrNull() ?: 0.0

        if (firstOperand == null) {
            firstOperand = current
        } else if (pendingOperator != null && !waitingForSecond) {
            // calcula encadeado
            val result = applyOperator(pendingOperator!!, firstOperand!!, current)
            firstOperand = result
            display = if (result.isNaN()) "Error" else formatResult(result)
        }

        pendingOperator = op
        waitingForSecond = true
        lastWasEquals = false
    }

    // Quando = é pressionado
    fun onEquals() {
        val current = display.toDoubleOrNull() ?: 0.0
        if (pendingOperator != null && firstOperand != null) {
            val result = applyOperator(pendingOperator!!, firstOperand!!, current)
            display = if (result.isNaN()) "Error" else formatResult(result)
            // prepara para nova operação a partir do resultado
            firstOperand = null
            pendingOperator = null
            waitingForSecond = false
            lastWasEquals = true
        }
    }

    // UI
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // DISPLAY com altura fixa
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = display,
                fontSize = 48.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Área dos botões ocupa o resto da tela
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Linha superior reduzida: AC acima do 7, ⌫ acima do 8
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // altura reduzida em 30% com fillMaxHeight(0.7f) e cor #5A5A5A
                CalcButton(
                    "AC",
                    modifier = Modifier.weight(1f).fillMaxHeight(0.7f),
                    containerColor = Color(0xFF5A5A5A)
                ) { clearAll() }

                CalcButton(
                    "⌫",
                    modifier = Modifier.weight(1f).fillMaxHeight(0.7f),
                    containerColor = Color(0xFF5A5A5A)
                ) { backspace() }

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.weight(1f))
            }

            // Linha 1: 7 8 9 ÷
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                CalcButton("7", modifier = Modifier.weight(1f).fillMaxHeight()) { inputNumber("7") }
                CalcButton("8", modifier = Modifier.weight(1f).fillMaxHeight()) { inputNumber("8") }
                CalcButton("9", modifier = Modifier.weight(1f).fillMaxHeight()) { inputNumber("9") }
                CalcButton("÷", modifier = Modifier.weight(1f).fillMaxHeight(), containerColor = Color(0xFFFF9200)) { onOperator("÷") }
            }

            // Linha 2: 4 5 6 x
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                CalcButton("4", modifier = Modifier.weight(1f).fillMaxHeight()) { inputNumber("4") }
                CalcButton("5", modifier = Modifier.weight(1f).fillMaxHeight()) { inputNumber("5") }
                CalcButton("6", modifier = Modifier.weight(1f).fillMaxHeight()) { inputNumber("6") }
                CalcButton("x", modifier = Modifier.weight(1f).fillMaxHeight(), containerColor = Color(0xFFFF9200)) { onOperator("x") }
            }

            // Linha 3: 1 2 3 -
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                CalcButton("1", modifier = Modifier.weight(1f).fillMaxHeight()) { inputNumber("1") }
                CalcButton("2", modifier = Modifier.weight(1f).fillMaxHeight()) { inputNumber("2") }
                CalcButton("3", modifier = Modifier.weight(1f).fillMaxHeight()) { inputNumber("3") }
                CalcButton("-", modifier = Modifier.weight(1f).fillMaxHeight(), containerColor = Color(0xFFFF9200)) { onOperator("-") }
            }

            // Linha 4: 0 . = +
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                CalcButton("0", modifier = Modifier.weight(1f).fillMaxHeight()) { inputNumber("0") }
                CalcButton(".", modifier = Modifier.weight(1f).fillMaxHeight()) { inputDecimal() }
                CalcButton("=", modifier = Modifier.weight(1f).fillMaxHeight(), containerColor = Color(0xFFFF9200)) { onEquals() }
                CalcButton("+", modifier = Modifier.weight(1f).fillMaxHeight(), containerColor = Color(0xFFFF9200)) { onOperator("+") }
            }
        }
    }
}

@Composable
fun CalcButton(
    label: String,
    modifier: Modifier = Modifier,
    containerColor: Color = Color(0xFF2F2F2F),
    onClickAction: () -> Unit
) {
    // cor do texto: preto para laranja, branco para os demais
    val textColor = if (containerColor == Color(0xFFFF9200)) Color.Black else Color.White

    Button(
        onClick = onClickAction,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = label, fontSize = 24.sp, color = textColor)
        }
    }
}
