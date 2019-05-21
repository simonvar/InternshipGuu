package io.github.simonvar.guu

import io.github.simonvar.guu.lexer.*
import io.github.simonvar.guu.parser.Parser
import io.github.simonvar.guu.parser.ParserMessageListener
import io.github.simonvar.guu.parser.ParserStepListener
import io.github.simonvar.guu.vm.VM
import java.io.File


fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("usage: guu <source path>")
        return
    }

    val file = File(args[0])
    if (file.isDirectory) {
        println("${file.path} is not a file")
        return
    }

    val guu = Guu(file.readText())
    guu.run()
}


class Guu(
    source: String,
    heapSize: Int = 30,
    stackSize: Int = 30
) : ParserMessageListener {

    private val lex = Lexer(source.lines())
    private val env = VM(heapSize, stackSize)
    private val parser = Parser(lex, env, this)

    fun run() {
        parser.prepare()
        command()
    }

    private fun command() {
        print("[line ${lex.currentLine}] >>> ")
        when (readLine()) {
            STEP_INTO -> {
                parser.next(object : ParserStepListener {
                    override fun step() {
                        command()
                    }

                    override fun eof() {
                        return
                    }

                })
            }

            STEP_OVER -> {
                println("not implemented :(")
                command()
            }

            TRACE -> {
                env.printStackTrace()
                command()
            }

            VARS -> {
                env.printVars()
                command()
            }

            SOURCE -> {
                lex.lines.forEachIndexed { index, s ->
                    println("${index + 1}: $s")
                }
                command()
            }

            else -> {
                println("wrong command.")
                command()
            }
        }
    }


    override fun noMainError() {
        throw Error("main function required.")
    }

    override fun undeclaredError(word: Word) {
        throw Error("${word.lexeme} undeclared. [at ${lex.currentLine}]")
    }

    override fun syntaxError() {
        throw Error("syntax error. [at ${lex.currentLine}]")
    }

    companion object {
        const val STEP_INTO = "i"
        const val STEP_OVER = "o"
        const val TRACE = "trace"
        const val VARS = "var"
        const val SOURCE = "source"
    }

}




