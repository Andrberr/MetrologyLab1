package com.example.metrologylab1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val rubyOperators = arrayOf(
        ".eql?(",
        "equal?",
        "and",
        "not",
        "or",
        "defined?",
        "return",
        "break",
        "next",
        "case when else end",
        "if elsif else",
        "loop do end",
        "while end",
        "for in do end",
        "until do end",
        "print",
        "puts",
        "putc",
        "gets"
    )

    private val shortOperators = arrayOf(
        "+", "–", "*", "/", "%", "", "<", ">", "=", "+=", "-=",
        "*=", "/=", "%=", "=", "(", "<=", ">=", "!=", "==",
        "<=>", "===", "||",
        "!", "?:", "..", "...", "&", "|", "^", "<<", ">>", "[",
        "::"
    )

    private val operators: Array<String> = arrayOf()
    private val operands: Array<String> = arrayOf()
    private var uniqueOperatorsAmount: Int = 0
    private var uniqueOperandsAmount: Int = 0
    private var operatorsAmount: Int = 0
    private var operandsAmount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val solutionView = findViewById<TextView>(R.id.solutionView)
        val inputView = findViewById<EditText>(R.id.inputView)
        findViewById<Button>(R.id.button).setOnClickListener {
            val input = inputView.text.toString()
            val correctInput = getCorrectInput(input)
            solution(correctInput)
            solutionView.text = uniqueOperatorsAmount.toString()
        }
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

    private fun addElementToMap(
        map: MutableMap<String, Int>,
        element: String
    ): MutableMap<String, Int> {
        if (map[element] != null) map[element] =
            map[element]?.plus(1) ?: 0
        else map[element] = 1
        return map
    }

    private fun solution(input: Array<String>) {
        var operatorsMap: MutableMap<String, Int> = mutableMapOf()
        for (i in input.indices) {
            var j = 0
            while (j < input[i].length) {
                var k = j
                var word = ""
                var shortWord = ""
                while (k < input[i].length && input[i][k] != ' ') {
                    shortWord += input[i][k]
                    var index = shortOperators.indexOf(shortWord)
                    if (index != -1) operatorsMap = addElementToMap(operatorsMap, shortOperators[index])
                    else {
                        index = shortOperators.indexOf(input[i][k].toString())
                        if (index != -1) operatorsMap = addElementToMap(operatorsMap, shortOperators[index])
                    }

                    word += input[i][k]
                    operatorsMap = findOperator(word, operatorsMap)
                    k++
                }
                if (j == k) j++
                else j = k
            }
        }
        uniqueOperatorsAmount = operatorsMap.size
    }

    private fun findOperator(
        word: String,
        operatorsMap: MutableMap<String, Int>
    ): MutableMap<String, Int> {
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