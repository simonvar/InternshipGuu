package io.github.simonvar.guu.vm

import io.github.simonvar.guu.parser.Func

class Stack(val size: Int) {

    val list = mutableListOf<Func>()

    fun push(f: Func) {
        if (list.size == size) error("Stack Overflow")
        list.add(f)
    }

    fun pop(): Int {
        val func = list.removeAt(list.size - 1)
        if(list.isEmpty()) return -1
        return func.position
    }


}