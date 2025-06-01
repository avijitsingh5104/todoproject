package com.todo.plugin.todo_highlighter

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager

@Service(Service.Level.PROJECT)
class TodoService(private val project: Project) {
    private val scanner = TodoScanner()

    fun getTodosForFile(file: VirtualFile): List<TodoEntry> {
        val psiFile = PsiManager.getInstance(project).findFile(file) ?: return emptyList()
        return scanner.scan(psiFile.text).map { (line, text) ->
            TodoEntry(file, line, text)
        }
    }
}