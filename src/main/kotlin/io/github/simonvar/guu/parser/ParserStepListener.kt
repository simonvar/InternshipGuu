package io.github.simonvar.guu.parser

interface ParserStepListener {

    fun step()

    fun stepOver()

    fun eof()

}