package com.todo.plugin.todo_highlighter

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager

@Service(Service.Level.PROJECT)
class TodoPanelService(private val project: Project) {
    private var panel: TodoToolWindowPanel? = null

    fun registerPanel(panel: TodoToolWindowPanel) {
        this.panel = panel
    }

    fun updateTodos(todos: List<TodoEntry>) {
        panel?.updateTodos(todos)
        ToolWindowManager.getInstance(project).getToolWindow("TodoPanel")?.show()
    }
}