package com.todo.plugin.todo_highlighter

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager

@Service(Service.Level.PROJECT)
class TodoProjectService(private val project: Project) {
    init {

        PsiManager.getInstance(project).addPsiTreeChangeListener(
                TodoPsiChangeListener(project),
                project
        )
    }
}
