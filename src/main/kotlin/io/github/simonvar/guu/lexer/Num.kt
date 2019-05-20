package io.github.simonvar.guu.lexer

class Num(val value: Long) : Token(Tag.NUM) {

    override fun toString() = "token: $value [$tag]"

}