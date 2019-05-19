package io.github.simonvar.guu

import java.util.*
import kotlin.collections.HashMap

fun main(args: Array<String>) {

    val heapSize = 30
    val stackSize = 30

    val heap = HashMap<String, Int>(heapSize)
    val stack = Vector<Function>(stackSize)

}

data class Function(
    val line: Int,
    val name: String
)



