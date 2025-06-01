package com.todo.plugin.todo_highlighter

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity


class TodoStartupActivity : ProjectActivity {
    override suspend fun execute(project: Project) {

        project.getService(TodoProjectService::class.java)
        project.getService(TodoStorageService::class.java)
    }
}