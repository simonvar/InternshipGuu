package io.github.simonvar.guu.lexer

import org.junit.Assert
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

    private val lex = Lexer(source.lines())

    @Test
    fun `correct tokens test`() {
        val lexeme1 = lex.scan()
        val lexeme2 = lex.scan()
        val lexeme3 = lex.scan()
        val lexeme4 = lex.scan()

        Assert.assertEquals(Tag.SUB, lexeme1.tag)
        Assert.assertEquals(Tag.ID, lexeme2.tag)
        Assert.assertEquals(Tag.END, lexeme3.tag)
        Assert.assertEquals(Tag.SET, lexeme4.tag)
    }

}