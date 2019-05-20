package io.github.simonvar.guu.lexer

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LexerTest {

    private val source =
        """
            sub main
                set a 5
                call foo
                print a

            sub foo
                set a 6
        """.trimIndent()

    private val lex = Lexer(source)

    @Test
    fun `correct tokens test`() {
        val lexeme1 = lex.scan()
        val lexeme2 = lex.scan()
        val lexeme3 = lex.scan()
        val lexeme4 = lex.scan()
        println(lexeme1)
        println(lexeme2)
        println(lexeme3)
        println(lexeme4)
    }

}