package com.todo.plugin.todo_highlighter

import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager

class EditorChangeListener : EditorFactoryListener {
    override fun editorCreated(event: EditorFactoryEvent) {
        val editor = event.editor
        val project = editor.project ?: ProjectManager.getInstance().defaultProject


        val file = FileEditorManager.getInstance(project).selectedFiles.firstOrNull()
        if (file != null) {
            val todos = project.getService(TodoService::class.java).getTodosForFile(file)
            project.getService(TodoPanelService::class.java).updateTodos(todos)
        }
    }
}