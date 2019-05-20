package io.github.simonvar.guu.lexer

class Word(val lexeme: String, tag: Int) : Token(tag) {

    companion object {
        val sub = Word("sub", Tag.SUB)
        val call = Word("call", Tag.CALL)
        val set = Word("set", Tag.SET)
        val print = Word("print", Tag.PRINT)
        val end = Word("\n", Tag.END)
    }

    override fun toString() = "token: $lexeme [$tag]"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Word) return false
        if (!super.equals(other)) return false

        if (lexeme != other.lexeme) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + lexeme.hashCode()
        return result
    }

}