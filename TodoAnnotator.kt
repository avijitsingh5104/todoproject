package com.todo.plugin.todo_highlighter

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import java.awt.Color

class TodoAnnotator : Annotator {

    companion object {
        val TODO_HIGHLIGHT: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
                "TODO_HIGHLIGHT",
                TextAttributes(Color.RED, null, null, null, 0)
        )
    }

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is PsiComment && containsTodo(element.text)) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(element)
                    .textAttributes(TODO_HIGHLIGHT)
                    .create()
        }
    }

    private fun containsTodo(text: String): Boolean {
        return "(?i)\\btodo\\b".toRegex().containsMatchIn(text)
    }
}
