package io.github.simonvar.guu.parser

import io.github.simonvar.guu.lexer.*
import io.github.simonvar.guu.vm.Environment
import io.github.simonvar.guu.vm.Logger

class Parser(
    val lexer: Lexer,
    val env: Environment,
    val logger: Logger
) {

    fun prepare(): Int {
        while (true) {
            val token = lexer.scan()
            if (token == Token.Empty) break

            if (token.tag == Word.sub.tag) {
                val name = lexer.scan() as? Word ?: return syntaxError(lexer.line)
                if (name.tag != Tag.ID) return syntaxError(lexer.line)

                val end = lexer.scan() as? Word ?: return syntaxError(lexer.line)
                if (end.tag != Tag.END) return syntaxError(lexer.line)

                env.addFuncDeclaration(Func(name, lexer.position, lexer.line))
            }
        }

        lexer.reset()
        val main = Word("main", Tag.ID)
        val func = env.getFuncDeclaration(main) ?: return noMainError()
        lexer.position = func.position
        lexer.line = func.line
        env.stack.push(func)
        return OK

    }

    fun next(): Int {
        val token = lexer.scan()

        if (token.tag == Word.sub.tag || token == Token.Empty) {
            return sub()
        }

        if (token.tag == Word.set.tag) {
            return set()
        }

        if (token.tag == Word.print.tag) {
            return print()
        }

        if (token.tag == Word.call.tag) {
            return call()
        }

        if (token.tag == Word.end.tag) {
            return OK
        }

        return syntaxError(lexer.line)
    }

    private fun sub(): Int {
        val prev = env.stack.pop()
        if (prev == -1) return END

        lexer.position = prev
        return OK
    }

    private fun call(): Int {
        val name = lexer.scan() as? Word ?: return syntaxError(lexer.line)
        if (name.tag != Tag.ID) return syntaxError(lexer.line)

        val lastPos = lexer.position
        val func = env.getFuncDeclaration(name) ?: return undeclaredError(lexer.line, name)
        lexer.position = func.position
        env.stack.push(Func(name, lastPos, lexer.line))
        return OK
    }

    private fun print(): Int {
        val name = lexer.scan() as? Word ?: return syntaxError(lexer.line)
        if (name.tag != Tag.ID) return syntaxError(lexer.line)

        val num = env.heap[name] ?: return undeclaredError(lexer.line, name)

        val end = lexer.scan() as? Word ?: return syntaxError(lexer.line)
        if (end.tag != Tag.END) return syntaxError(lexer.line)

        println(num.value)
        return OK
    }

    private fun set(): Int {
        val name = lexer.scan() as? Word ?: return syntaxError(lexer.line)
        if (name.tag != Tag.ID) return syntaxError(lexer.line)

        val value = lexer.scan() as? Num ?: return syntaxError(lexer.line)

        val end = lexer.scan() as? Word ?: return syntaxError(lexer.line)
        if (end.tag != Tag.END) return syntaxError(lexer.line)

        env.heap[name] = value
        return OK
    }

    private fun syntaxError(line: Int): Int {
        logger.syntaxError(line)
        return ERROR
    }

    private fun undeclaredError(line: Int, word: Word): Int {
        logger.undeclaredError(line, word)
        return ERROR
    }

    private fun noMainError(): Int {
        logger.noMain()
        return ERROR
    }


    companion object {
        const val OK = 0
        const val END = 1
        const val ERROR = -1
    }

}