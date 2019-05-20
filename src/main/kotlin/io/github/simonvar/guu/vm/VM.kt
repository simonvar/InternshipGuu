package io.github.simonvar.guu.vm

import io.github.simonvar.guu.lexer.Num
import io.github.simonvar.guu.lexer.Word
import io.github.simonvar.guu.parser.Func

class VM(heapSize: Int, stackSize: Int) : Environment {

    override val heap: HashMap<Word, Num> = HashMap(heapSize)
    override val stack: Stack = Stack(stackSize)

    private val functions: HashMap<Word, Func> = HashMap()

    override fun addFuncDeclaration(f: Func) {
        functions[f.word] = f
    }

    override fun getFuncDeclaration(word: Word): Func? {
        return functions[word]
    }

    override fun printStackTrace() {
        stack.list.forEach { f ->
            println("${f.word.lexeme} at ${f.line}")
        }
    }

    override fun printVars() {
        heap.forEach { (w, n) ->
            println("${w.lexeme} = ${n.value}")
        }
    }



}