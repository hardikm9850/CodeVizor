package com.github.hardikm9850.codevizor.marker

import com.github.hardikm9850.codevizor.util.CyclomaticComplexityVisitor
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.psi.PsiElement

class CyclomaticComplexityLineMarker :
    AbstractComplexityLineMarker("Cyclomatic Complexity") {

    override fun <E : PsiElement, P : PsiElement> getComplexityMarkerInfo(
        elementToMark: E,
        vararg blocksToMeasure: P
    ): LineMarkerInfo<E>? {
        val visitor = CyclomaticComplexityVisitor()
        blocksToMeasure.forEach { it.accept(visitor); }
        val complexity = visitor.complexityTrees().size
        return if (complexity <= 1) {
            null
        } else {
            ComplexityLineMarkerInfo(complexity, elementToMark, complexityType)
        }
    }
}
