package io.github.simonvar.guu.lexer

import kotlin.collections.HashMap

class Lexer(private val l: List<String>) {

    var currentLine: Int
        get() = position + 1
        set(value) {
            index = 0
            position = value - 1
        }

    val lines = l.filter {
        it.isNotBlank()
    }

    private var position = 0
    private var index = 0
    private val words = HashMap<String, Word>()

    private val line: String
        get() = lines[position]

    private val peek: Char
        get() = line[index]

    init {
        reserve(Word.sub)
        reserve(Word.call)
        reserve(Word.set)
        reserve(Word.print)
    }

    fun scan(): Token {
        if (position >= lines.size) {
            return Token.EOF
        }

        if (lines.isNullOrEmpty() || line.isBlank()) {
            nextLine()
            return Token.Empty
        }

        if (index >= line.length || peek == '\n') {
            nextLine()
            return Word.end
        }

        skip()

        if (peek.isDigit()) return tokenNumber()
        if (peek.isLetter()) return tokenWord()
        return Token(peek.toInt())
    }

    fun reset() {
        position = 0
        index = 0
    }

    private fun tokenNumber(): Token {
        var v = 0L
        do {
            v = 10 * v + Character.digit(peek, 10)
            nextIndex()
        } while (index < line.length && peek.isDigit())
        return Num(v)
    }

    private fun tokenWord(): Token {
        val buffer = StringBuilder()
        do {
            buffer.append(peek)
            nextIndex()
        } while (index < line.length && peek.isLetter())
        val value = buffer.toString()
        return words.getOrPut(value) {
            Word(value, Tag.ID)
        }
    }

    private fun skip() {
        while (true) {
            if (index >= line.length) break
            if (peek == ' ' || peek == '\t') nextIndex()
            else break
        }
    }


    private fun nextIndex() {
        index += 1
    }

    private fun nextLine() {
        index = 0
        position += 1
    }

    private fun reserve(w: Word) {
        words[w.lexeme] = w
    }

}
