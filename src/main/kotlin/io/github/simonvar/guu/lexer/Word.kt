package io.github.simonvar.guu.lexer

class Word(val lexeme: String, tag: Int) : Token(tag) {

    companion object {
        val sub = Word("sub", Tag.SUB)
        val call = Word("call", Tag.CALL)
        val set = Word("set", Tag.SET)
        val print = Word("print", Tag.PRINT)
    }

    override fun toString() = "token: $lexeme [$tag]"

}