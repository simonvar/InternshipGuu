package io.github.simonvar.guu.vm

import io.github.simonvar.guu.lexer.Word


class Logger {

    fun syntaxError(line: Int) {
        throw Error("syntax error. [at $line]")
    }

    fun undeclaredError(line: Int, word: Word) {
        throw Error("${word.lexeme} undeclared. [at $line]")
    }

    fun noMain() {
        throw Error("main function required.")
    }

}