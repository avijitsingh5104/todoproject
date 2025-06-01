package com.todo.plugin.todo_highlighter

class TodoScanner {
    fun scan(text: String): List<Pair<Int, String>> {
        val result = mutableListOf<Pair<Int, String>>()
        val lines = text.lines()
        for ((index, line) in lines.withIndex()) {
            if (line.contains("TODO", true)) {
                result.add(Pair(index + 1, line.trim()))
            }
        }
        return result
    }
}