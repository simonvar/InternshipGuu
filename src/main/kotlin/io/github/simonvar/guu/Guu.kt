package io.github.simonvar.guu

import io.github.simonvar.guu.lexer.*
import io.github.simonvar.guu.parser.Parser
import io.github.simonvar.guu.vm.Logger
import io.github.simonvar.guu.vm.VM


fun main(args: Array<String>) {

    val heapSize = 30
    val stackSize = 30

    val source =
        """
        sub main
            set a 5
            call foo
            print a

        sub foo
            set a 109
            set a 3

        """

    val lex = Lexer(source)

    val logger = Logger()
    val env = VM(heapSize, stackSize)

    val parser = Parser(lex, env, logger)

    parser.prepare()


    do {
        print("[line ${lex.line}] >>> ")
        val command = readLine()
        val res = when (command) {
            "i" -> {
                parser.next()
            }
            "o" -> {
                parser.next()
            }
            "trace" -> {
                env.printStackTrace()
                Parser.OK
            }
            "var" -> {
                env.printVars()
                Parser.OK
            }
            else -> Parser.OK
        }
    } while (res == Parser.OK)

}



