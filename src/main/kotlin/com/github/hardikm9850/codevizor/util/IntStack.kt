package com.github.hardikm9850.codevizor.util

class IntStack {
    private val elements = mutableListOf<Int>()

    fun push(item: Int) {
        elements.add(item)
    }

    fun pop(): Int {
        return if (elements.isNotEmpty()) elements.removeAt(elements.size - 1) else 0
    }

    fun peek(): Int {
        return if (elements.isNotEmpty()) elements[elements.size - 1] else 0
    }

    fun isEmpty(): Boolean {
        return elements.isEmpty()
    }

    fun size(): Int {
        return elements.size
    }
}