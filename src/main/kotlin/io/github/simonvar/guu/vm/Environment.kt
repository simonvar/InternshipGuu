package io.github.simonvar.guu.vm

import io.github.simonvar.guu.lexer.Num
import io.github.simonvar.guu.lexer.Word
import io.github.simonvar.guu.parser.Func


interface Environment {

    val heap: HashMap<Word, Num>
    val stack: Stack

    fun addFuncDeclaration(f: Func)

    fun getFuncDeclaration(word: Word): Func?

    fun printStackTrace()

    fun printVars()
}