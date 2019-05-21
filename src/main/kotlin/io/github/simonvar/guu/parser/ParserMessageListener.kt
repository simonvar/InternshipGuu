package io.github.simonvar.guu.parser

import io.github.simonvar.guu.lexer.Word

interface ParserMessageListener {

    fun noMainError()

    fun undeclaredError(word: Word)

    fun syntaxError()

}