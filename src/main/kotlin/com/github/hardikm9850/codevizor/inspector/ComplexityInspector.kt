package com.github.hardikm9850.codevizor.inspector

import com.intellij.codeInspection.*
import com.intellij.psi.PsiFile
import org.jetbrains.kotlin.psi.*

class ComplexityInspector : LocalInspectionTool() {

    override fun checkFile(file: PsiFile, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor> {
        val problemHolder = ProblemsHolder(manager, file, isOnTheFly)
        if (file is KtFile) {
            analyzeMethods(file, problemHolder)
        }
        return problemHolder.resultsArray
    }

    private fun analyzeMethods(file: KtFile, holder: ProblemsHolder) {
        file.accept(object : KtVisitorVoid() {
            override fun visitNamedFunction(function: KtNamedFunction) {
                val complexity = calculateCyclomaticComplexity(function)
                if (complexity > THRESHOLD) {
                    holder.registerProblem(
                        function,
                        "Method complexity is too high: $complexity",
                        ProblemHighlightType.WARNING,
                    )

                }
            }
        })
    }

    private fun calculateCyclomaticComplexity(function: KtNamedFunction): Int {
        // Logic to calculate cyclomatic complexity
        // E.g., count control flow statements (if, for, while, etc.)
        var complexity = 1 // Start with 1 for the method itself
        // Traverse the function body and count control flow statements
        function.bodyExpression?.accept(object : KtVisitor<Int, Unit>() {
            override fun visitIfExpression(expression: KtIfExpression, data: Unit): Int {
                complexity++
                return super.visitIfExpression(expression, data)
            }

            override fun visitWhileExpression(expression: KtWhileExpression, data: Unit): Int {
                complexity++
                return super.visitWhileExpression(expression, data)
            }

            // Add additional visits for other control flow structures
        })
        return complexity
    }

    companion object {
        private const val THRESHOLD = 5 // Example threshold
    }
}