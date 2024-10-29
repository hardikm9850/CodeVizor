package com.github.hardikm9850.codevizor.util

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtBinaryExpression
import org.jetbrains.kotlin.psi.KtIfExpression
import org.jetbrains.kotlin.psi.KtLoopExpression
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtTreeVisitorVoid
import org.jetbrains.kotlin.psi.KtWhenEntry

class CyclomaticComplexityVisitor : KtTreeVisitorVoid() {
    private val complexityTrees = mutableListOf<PsiElement>()

    fun complexityTrees() = complexityTrees.toList()

    override fun visitNamedFunction(function: KtNamedFunction) {
        if(function.hasBody() && function.name != null) {
            complexityTrees.add(function)
        }
        super.visitNamedFunction(function)
    }

    override fun visitIfExpression(expression: KtIfExpression) {
        complexityTrees.add(expression.ifKeyword)
        super.visitIfExpression(expression)
    }

    override fun visitLoopExpression(loopExpression: KtLoopExpression) {
        complexityTrees.add(loopExpression)
        super.visitLoopExpression(loopExpression)
    }

    override fun visitWhenEntry(whenEntry: KtWhenEntry) {
        complexityTrees.add(whenEntry)
        super.visitWhenEntry(whenEntry)
    }

    override fun visitBinaryExpression(expression: KtBinaryExpression) {
        if (expression.operationToken == KtTokens.ANDAND || expression.operationToken == KtTokens.OROR) {
            complexityTrees.add(expression)
        }
        super.visitBinaryExpression(expression)
    }
}