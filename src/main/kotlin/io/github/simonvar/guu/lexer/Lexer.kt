package io.github.simonvar.guu.lexer

import kotlin.collections.HashMap

class Lexer(val source: String) {

    var line = 1
        private set

    var position = -1
        private set

    val words = HashMap<String, Word>()

    private val peek: Char
        get() = source[position]

    init {
        reserve(Word.sub)
        reserve(Word.call)
        reserve(Word.set)
        reserve(Word.print)
    }

    fun scan(): Token {
        if (source.isBlank()) return Token(0)

        skip()
        if (peek.isDigit()) return tokenNumber()
        if (peek.isLetter()) return tokenWord()
        return Token(peek.toInt())
    }


    private fun tokenNumber(): Token {
        var v = 0L
        do {
            v = 10 * v + Character.digit(peek, 10)
            next()
        } while (position < source.length && peek.isDigit())
        return Num(v)
    }

    private fun tokenWord(): Token {
        val buffer = StringBuilder()
        do {
            buffer.append(peek)
            next()
        } while (position < source.length && peek.isLetter())
        val value = buffer.toString()
        return words.getOrPut(value) {
            Word(value, Tag.ID)
        }
    }

    private fun skip() {
        while (true) {
            next()
            if (position >= source.length) break
            if (peek == ' ' || peek == '\t') continue
            else if (peek == '\n') line.inc()
            else break
        }
    }


    private fun next(){
        position += 1
    }

    private fun reserve(w: Word) {
        words[w.lexeme] = w
    }

}
