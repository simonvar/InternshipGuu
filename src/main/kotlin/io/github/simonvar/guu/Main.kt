package io.github.simonvar.guu

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


