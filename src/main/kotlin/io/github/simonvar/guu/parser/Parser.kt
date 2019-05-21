package io.github.simonvar.guu.parser

import io.github.simonvar.guu.lexer.*
import io.github.simonvar.guu.vm.Environment

class Parser(
    val lexer: Lexer,
    val env: Environment,
    val messageListener: ParserMessageListener
) {

    fun prepare() {
        while (true) {
            val token = lexer.scan()
            if (token == Token.Empty) continue
            if (token == Token.EOF) break

            if (token.tag == Word.sub.tag) {
                val name = lexer.scan() as? Word ?: return messageListener.syntaxError()
                if (name.tag != Tag.ID) return messageListener.syntaxError()

                val end = lexer.scan() as? Word ?: return messageListener.syntaxError()
                if (end.tag != Tag.END) return messageListener.syntaxError()

                env.addFuncDeclaration(Func(name, lexer.currentLine))
            }
        }

        lexer.reset()
        val func = env.getFuncDeclaration(Word.main) ?: return messageListener.noMainError()
        lexer.currentLine = func.position
        env.stack.push(func)
    }

    fun next(stepListener: ParserStepListener) {
        val token = lexer.scan()

        if (token == Token.EOF) {
            sub(stepListener)
        } else when (token.tag) {
            Word.sub.tag -> sub(stepListener)
            Word.set.tag -> set(stepListener)
            Word.print.tag -> print(stepListener)
            Word.call.tag -> call(stepListener)
            Word.end.tag -> stepListener.step()
            else -> messageListener.syntaxError()
        }
    }

    private fun sub(stepListener: ParserStepListener) {
        val prev = env.stack.pop()
        if (prev == -1) return stepListener.eof()

        lexer.currentLine = prev

        stepListener.step()
        stepListener.stepOver()
    }

    private fun call(stepListener: ParserStepListener) {
        val name = lexer.scan() as? Word ?: return messageListener.syntaxError()
        if (name.tag != Tag.ID) return messageListener.syntaxError()

        val stopOnLine = lexer.currentLine + 1

        val func = env.getFuncDeclaration(name) ?: return messageListener.undeclaredError(name)
        lexer.currentLine = func.position

        env.stack.push(Func(name, stopOnLine))
        stepListener.step()
    }

    private fun print(stepListener: ParserStepListener) {
        val name = lexer.scan() as? Word ?: return messageListener.syntaxError()
        if (name.tag != Tag.ID) return messageListener.syntaxError()

        val num = env.heap[name] ?: return messageListener.undeclaredError(name)

        val end = lexer.scan() as? Word ?: return messageListener.syntaxError()
        if (end.tag != Tag.END) return messageListener.syntaxError()

        println(num.value)
        stepListener.step()
    }

    private fun set(stepListener: ParserStepListener) {
        val name = lexer.scan() as? Word ?: return messageListener.syntaxError()
        if (name.tag != Tag.ID) return messageListener.syntaxError()

        val value = lexer.scan() as? Num ?: return messageListener.syntaxError()

        val end = lexer.scan() as? Word ?: return messageListener.syntaxError()
        if (end.tag != Tag.END) return messageListener.syntaxError()

        env.heap[name] = value
        stepListener.step()
    }



}