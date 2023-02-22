package com.example.metrologylab1

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import java.util.*

class MainActivity : AppCompatActivity() {

    private val rubyOperators = arrayOf(
        "+", "–", "*", "/", "%", "", "<", ">", "=", "+=", "-=",
        "*=", "/=", "%=", "=", "(", "<=", ">=", "!=", "==",
        "<=>", "===", ".eql?(", "equal?", "and", "not", "or", "||",
        "!", "?:", "..", "...", "&", "|", "^", "<<", ">>", "[",
        "::", "defined?", "return", "break", "next", "case when else end",
        "if elsif else", "loop do end", "while end", "for in do end",
        "until do end", "print", "puts", "putc", "gets"
    )

    private val operators: Array<String> = arrayOf()
    private val operands: Array<String> = arrayOf()
    private val uniqueOperatorsAmount: Int = 0
    private val uniqueOperandsAmount: Int = 0
    private val operatorsAmount: Int = 0
    private val operandsAmount: Int = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputView = findViewById<EditText>(R.id.inputView)
        val input = inputView.text.toString()
        val correctInput = getCorrectInput(input)

    }

    private fun getCorrectInput(input: String): Array<String> {
        val correctInput: MutableList<String> = mutableListOf()
        var j = 0
        for (i in input.indices) {
            if (input[i] == '\n') {
                correctInput.add(input.substring(j, i))
                j = i + 1
            }
        }
        return correctInput.toTypedArray()
    }

    private fun solution(input: Array<String>) {
        val operatorsMap: MutableMap<String, Int> = mutableMapOf()
        for (i in input.indices) {
            var j = 0
            while (j < input[i].length) {
                var k = j
                var word = ""
                while (input[i][k] != ' ' && k < input[i].length) {
                    word += input[i][k]
                    k++
                    TODO()
                }
                j = k
            }
        }
    }

    private fun findOperator(
        word: String,
        operatorsMap: MutableMap<String, Int>
    ): Map<String, Int> {
        var operator = ""
        for (elem in rubyOperators) {
            operator = if (elem.contains(' ')) elem.substring(0, elem.indexOf(' '))
            else elem
            if (operator == word) {
                if (operatorsMap[operator] != null) operatorsMap[operator] =
                    operatorsMap[operator]?.plus(1) ?: 0
                else operatorsMap[operator] = 1
                return operatorsMap
            }
        }
        return operatorsMap
    }
}