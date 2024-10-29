package com.github.hardikm9850.codevizor.marker

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.util.IconLoader.getIcon
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiReferenceExpression


class SideEffectLineMarkerProvider : LineMarkerProvider {
    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        if (element is PsiReferenceExpression) {
            // Check if expression has method
            if (element.resolve() is PsiMethod) {
                val lineMarker = LineMarkerInfo(
                    element,
                    element.textRange,
                    ICON,
                    null,
                    null,
                    GutterIconRenderer.Alignment.LEFT
                ) { "Function Marker" }
                return lineMarker

            }
        }
        return null
    }

    companion object {
        private val ICON = getIcon("/icons/dragon.png", SideEffectLineMarkerProvider::class.java)
    }
}