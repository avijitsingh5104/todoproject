package com.todo.plugin.todo_highlighter

import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextField
import java.awt.BorderLayout
import javax.swing.DefaultListModel
import javax.swing.JPanel
import javax.swing.ListSelectionModel
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class TodoToolWindowPanel(private val project: Project) : JPanel(BorderLayout()) {
    private val listModel = DefaultListModel<String>()
    private val listComponent = JBList(listModel)
    private val searchField = JBTextField()
    private var allTodos: List<TodoEntry> = emptyList()

    init {
        // Setup UI
        add(searchField, BorderLayout.NORTH)
        listComponent.selectionMode = ListSelectionModel.SINGLE_SELECTION
        add(JBScrollPane(listComponent), BorderLayout.CENTER)

        // Setup search filter
        searchField.document.addDocumentListener(object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent) = updateList()
            override fun removeUpdate(e: DocumentEvent) = updateList()
            override fun changedUpdate(e: DocumentEvent) = updateList()
        })

        // Fixed navigation implementation
        listComponent.addListSelectionListener {
            if (!it.valueIsAdjusting) {
                val index = listComponent.selectedIndex
                if (index in 0 until listModel.size) {
                    val todo = allTodos[index]
                    navigateToTodo(todo)
                }
            }
        }

        // Register panel with service
        project.getService(TodoPanelService::class.java).registerPanel(this)
    }

    private fun navigateToTodo(todo: TodoEntry) {
        // Convert to zero-based line index
        val lineIndex = todo.line - 1
        OpenFileDescriptor(project, todo.file, lineIndex, 0).navigate(true)
    }

    fun updateTodos(todos: List<TodoEntry>) {
        allTodos = todos
        updateList()
    }

    private fun updateList() {
        val term = searchField.text
        val filtered = if (term.isBlank()) allTodos
        else allTodos.filter { it.text.contains(term, true) }

        listModel.clear()
        filtered.forEach { listModel.addElement("Line ${it.line}: ${it.text}") }
    }
}