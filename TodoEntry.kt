package com.todo.plugin.todo_highlighter

import com.intellij.openapi.vfs.VirtualFile

data class TodoEntry(val file: VirtualFile, val line: Int, val text: String)