package com.example.metrologylab1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import java.io.IOException
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    companion object {
        private const val FUN_WORD = "def"
        private const val ONE_LINE_COMMENT = "#"
        private const val BEGIN_MULTI_LINE_COMMENT = "=begin"
        private const val END_MULTI_LINE_COMMENT = "=end"
    }

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
        "if elsif else end",
        "unless else end",
        "loop do end",
        "while do end",
        "for in do end",
        "until do end",
        "begin end while",
        "redo",
        "print",
        "puts",
        "putc",
        "p",
        "gets"
    )

    private val shortOperators = arrayOf(
        "+", "–", "*", "/", "%", "**", "<", ">", "=", "+=", "-=",
        "*=", "/=", "%=", "=", "(", "<=", ">=", "!=", "==",
        "<=>", "===", "||", "\"",
        "!", "?:", "..", "...", "&", "|", "^", "<<", ">>", "[", "{",
        "::", ",", "."
    )

    private val rubyReservedWords = arrayOf(
        "BEGIN", "do", "next", "then", "END", "else", "nil", "true",
        "alias", "elsif", "not", "undef", "and", "end", "or", "unless",
        "begin", "ensure", "redo", "until", "break", "false", "rescue", "when",
        "case", "for", "retry", "while", "class", "if", "return", "while",
        "def", "in", "self", "__FILE__", "defined?", "module", "super", "__LINE__"
    )

    private val closedBrackets = arrayOf(
        ')', ']', '}'
    )

    private var functions = arrayOf("")

    private var uniqueOperatorsAmount: Int = 0
    private var uniqueOperandsAmount: Int = 0
    private var operatorsAmount: Int = 0
    private var operandsAmount: Int = 0

    private var operatorsMap: MutableMap<String, Int> = mutableMapOf()
    private var operandsMap: MutableMap<String, Int> = mutableMapOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val solutionView = findViewById<TextView>(R.id.solutionView)
        val inputView = findViewById<EditText>(R.id.inputView)
        findViewById<Button>(R.id.button).setOnClickListener {
            clean()
            val input = inputView.text.toString()
            val correctInput = getCorrectInput(input)
            for (str in correctInput){
                println(str)
            }
            solution(correctInput)
            var output = ""
            for (operator in operatorsMap) {
                var key = operator.key
                var value = operator.value
                when (key) {
                    "(" -> key = "( )"
                    "[" -> key = "[ ]"
                    "{" -> key = "{ }"
                    "\"" -> {
                        key = "\" \""
                        value /= 2
                    }
                }
                output += "   $key -> $value\n"
            }

            var number = 1
            for (operand in operandsMap) {
                output += "   " + number.toString() + "    ->    " + operand.key + "    ->   " + operand.value + "\n"
                number++
            }
            solutionView.text = output
        }

        findViewById<ImageButton>(R.id.fileButton).setOnClickListener {
            readFromFile("source.txt", inputView)
        }
    }

    private fun clean() {
        operatorsMap.clear()
        operandsMap.clear()
    }

    private fun getMaxLengthOperator(): Int {
        var max = shortOperators[0].length
        for (i: Int in 1 until shortOperators.size) {
            if (shortOperators[i].length > max) max = shortOperators[i].length
        }
        return max
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
        val ahead = getMaxLengthOperator() - 1
        var isCommented = false
        for (i in input.indices) {
            if (input[i].isNotEmpty()) {
                if (input[i].indexOf(ONE_LINE_COMMENT) != -1) input[i] =
                    input[i].substring(0, input[i].indexOf(ONE_LINE_COMMENT))

                var j = 0
                var comment = ""
                var l = j
                while (l < input[i].length && input[i][l] != ' ') {
                    comment += input[i][l]
                    l++
                }

                if (comment == BEGIN_MULTI_LINE_COMMENT) {
                    j = l
                    isCommented = true
                }
                if (comment == END_MULTI_LINE_COMMENT) isCommented = false
                if (isCommented) continue

                while (j < input[i].length) {
                    var k = j
                    var word = ""
                    while (k < input[i].length && input[i][k] != ' ') {
                        word += input[i][k]
                        k++
                    }

                    if (word == FUN_WORD) {
                        var funOperator = ""
                        k++
                        while (k < input[i].length && input[i][k] != '(' && input[i][k] != ' ') {
                            funOperator += input[i][k]
                            k++
                        }
                        operatorsMap = addElementToMap(operatorsMap, funOperator)

                    } else if (!findOperator(word)) {
                        var m = 0
                        var testWord = ""
                        while (m < word.length) {
                            var isFound = false

                            for (maxLengthI in 0..ahead) {
                                var shortWord = ""
                                if (m + ahead - maxLengthI < word.length) {
                                    for (strLenI in 0..ahead - maxLengthI) {
                                        shortWord += word[m + strLenI].toString()
                                    }
                                    if (shortOperators.indexOf(shortWord) != -1) {
                                        operatorsMap = addElementToMap(operatorsMap, shortWord)
                                        m += ahead - maxLengthI + 1
                                        isFound = true
                                    }
                                }
                                if (isFound) break
                            }

                            if (!isFound) {
                                if (!closedBrackets.contains(word[m])) testWord += word[m]
                                m++
                            }
                            if ((!isFound && m == word.length) || isFound) {
                                if (testWord != "" && !rubyReservedWords.contains(testWord)) {
                                    addElementToMap(operandsMap, testWord)
                                    testWord = ""
                                }
                            }
                        }
                    }
                    if (j == k) j++
                    else j = k
                }
            }
        }
    }

    private fun findOperator(
        word: String
    ): Boolean {
        var contains = false
        var operator = ""
        for (elem in rubyOperators) {
            operator = if (elem.contains(' ')) elem.substring(0, elem.indexOf(' '))
            else elem
            if (operator == word) {
                contains = true
                if (operatorsMap[elem] != null) operatorsMap[elem] =
                    operatorsMap[elem]?.plus(1) ?: 0
                else operatorsMap[elem] = 1
            }
        }
        return contains
    }

    private fun readFromFile(file: String, view: EditText) {
        val myInputStream: InputStream
        val output: String
        try {
            myInputStream = assets.open(file)
            val size: Int = myInputStream.available()
            val buffer = ByteArray(size)
            myInputStream.read(buffer)
            output = String(buffer)
            view.setText(output)
        } catch (e: IOException) {
            Toast.makeText(this, "Файл не найден!!!", Toast.LENGTH_LONG).show()
        }
    }
}
