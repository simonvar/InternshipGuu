package io.github.simonvar.guu.lexer

open class Token(val tag: Int) {

    override fun toString() = "token: [$tag]"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Token) return false

        if (tag != other.tag) return false

        return true
    }

    override fun hashCode(): Int {
        return tag
    }

    companion object {
        val Empty = Token(-1)
    }

}