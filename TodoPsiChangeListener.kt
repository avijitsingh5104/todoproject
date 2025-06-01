package com.todo.plugin.todo_highlighter

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeEvent
import com.intellij.util.Alarm
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

class TodoPsiChangeListener(private val project: Project) : PsiTreeChangeAdapter() {
    private val alarm = Alarm(Alarm.ThreadToUse.POOLED_THREAD, project)
    private val filesToUpdate = ConcurrentHashMap.newKeySet<VirtualFile>()
    private val updateScheduled = AtomicBoolean(false)

    override fun childrenChanged(event: PsiTreeChangeEvent) = scheduleUpdate(event)
    override fun childAdded(event: PsiTreeChangeEvent) = scheduleUpdate(event)
    override fun childRemoved(event: PsiTreeChangeEvent) = scheduleUpdate(event)
    override fun childReplaced(event: PsiTreeChangeEvent) = scheduleUpdate(event)
    override fun childMoved(event: PsiTreeChangeEvent) = scheduleUpdate(event)
    override fun propertyChanged(event: PsiTreeChangeEvent) = scheduleUpdate(event)

    private fun scheduleUpdate(event: PsiTreeChangeEvent) {
        val file = event.file?.virtualFile ?: return
        filesToUpdate.add(file)

        if (updateScheduled.compareAndSet(false, true)) {
            alarm.addRequest({
                updateScheduled.set(false)
                val files = filesToUpdate.toList()
                filesToUpdate.clear()

                ApplicationManager.getApplication().runReadAction {
                    if (project.isDisposed) return@runReadAction

                    val todoService = project.getService(TodoService::class.java)
                    val todosMap = files.associateWith { todoService.getTodosForFile(it) }

                    ApplicationManager.getApplication().invokeLater {
                        if (project.isDisposed) return@invokeLater

                        val panelService = project.getService(TodoPanelService::class.java)
                        todosMap.forEach { (file, todos) ->
                            panelService.updateTodos(todos)
                        }
                    }
                }
            }, 300)
        }
    }
}