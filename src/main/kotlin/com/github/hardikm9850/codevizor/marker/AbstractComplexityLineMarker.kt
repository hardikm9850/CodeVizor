package com.github.hardikm9850.codevizor.marker

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.psi.*
import org.jetbrains.annotations.Nls
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtFunction

abstract class AbstractComplexityLineMarker(
    @Nls(capitalization = Nls.Capitalization.Title) val complexityType: String
) : LineMarkerProviderDescriptor() {

    init {
        thisLogger().warn("Woohoooo AbstractComplexityLineMarker")
    }

    override fun getName(): String? = "$complexityType line marker"

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        val parent = element.parent
        if (element is PsiIdentifier) {
            if ((parent is KtFunction && parent.nameIdentifier == element) ||
                (parent is KtClass && parent.nameIdentifier == element)
            ) {
                return getComplexityMarkerInfo(element, parent)
            }
        } else if (element is KtElement) {
            if (
                ((element.textMatches(PsiKeyword.SWITCH) || element.textMatches(PsiKeyword.CASE)) && (parent is PsiSwitchStatement || parent is PsiSwitchExpression)) ||
                (element.textMatches(PsiKeyword.WHILE) && parent is PsiWhileStatement) ||
                (element.textMatches(PsiKeyword.FOR) && (parent is PsiForStatement || parent is PsiForeachStatement)) ||
                (element.textMatches(PsiKeyword.DO) && parent is PsiDoWhileStatement) ||
                (element.textMatches(PsiKeyword.TRY) && parent is PsiTryStatement) ||
                (element.textMatches(PsiKeyword.CATCH) && parent is PsiCatchSection)
            ) {
                return getComplexityMarkerInfo(element, parent)
            } else if (element.textMatches(PsiKeyword.IF) && parent is PsiIfStatement) {
                return if (parent.elseBranch == null)
                    getComplexityMarkerInfo(element, parent)
                else
                    getComplexityMarkerInfo(element, parent.condition ?: return null, parent.thenBranch ?: return null)
            } else if (element.textMatches(PsiKeyword.ELSE) && parent is PsiIfStatement && parent.elseBranch !is PsiIfStatement) {
                return getComplexityMarkerInfo(element, parent.elseBranch ?: return null)
            }
        }

        return null
    }

    abstract fun <E : PsiElement, P : PsiElement> getComplexityMarkerInfo(
        elementToMark: E,
        vararg blocksToMeasure: P
    ): LineMarkerInfo<*>?
}
