package io.github.simonvar.guu.lexer

import kotlin.collections.HashMap

class Lexer(text: String) {

    var line = 0
    var position = 0

    private val source = text.trimIndent()
        .replace("\n{2,}".toRegex(), "\n")
        .replace("\n +".toRegex(), "\n")

    private val words = HashMap<String, Word>()

    private val peek: Char
        get() = source[position]

    init {
        reserve(Word.sub)
        reserve(Word.call)
        reserve(Word.set)
        reserve(Word.print)
    }

    fun scan(): Token {
        skip()

        if (source.isBlank() || position >= source.length) return Token.Empty

        if (peek == '\n') {
            line += 1
            next()
            return Word.end
        }

        if (peek.isDigit()) return tokenNumber()
        if (peek.isLetter()) return tokenWord()
        return Token(peek.toInt())
    }

    fun reset() {
        line = 1
        position = 0
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
            if (position >= source.length) break
            if (peek == ' ' || peek == '\t') next()
            else break
        }
    }


    private fun next() {
        position += 1
    }

    private fun reserve(w: Word) {
        words[w.lexeme] = w
    }

}
