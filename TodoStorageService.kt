package com.todo.plugin.todo_highlighter

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.vfs.LocalFileSystem

@State(name = "TodoStorage", storages = [Storage("TodoStorage.xml")])
@Service(Service.Level.PROJECT)
class TodoStorageService : PersistentStateComponent<TodoStorageService.State> {
    data class TodoEntryState(val filePath: String, val line: Int, val text: String)

    data class State(var todos: MutableList<TodoEntryState> = mutableListOf())

    private var state = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    fun saveTodos(entries: List<TodoEntry>) {
        state.todos = entries.map {
            TodoEntryState(it.file.path, it.line, it.text)
        }.toMutableList()
    }

    fun loadTodos(): List<TodoEntry> {
        return state.todos.mapNotNull { saved ->
            val file = LocalFileSystem.getInstance().findFileByPath(saved.filePath)
            if (file != null) {
                TodoEntry(file, saved.line, saved.text)
            } else null
        }
    }
}
