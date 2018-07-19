package com.paincker.lint.core.detector

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import com.paincker.lint.core.config.ConfigUtil
import com.paincker.lint.core.config.LintConfig
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UClass

/**
 * Created by haiyang_tan on 2018/7/13.
 *
 * 自定义Lint规则，根据project目录下规则文件进行检查
 */
class ConfigDetector : Detector(), Detector.UastScanner, Detector.ClassScanner {

    lateinit var config: LintConfig

    override fun beforeCheckProject(context: Context?) {
        // 读取配置
        config = LintConfig(context!!)
    }

    /** ================================== Constructor class ============================================ **/

    override fun getApplicableConstructorTypes(): List<String>? {
        return ConfigUtil.getConstructions(config.configs)
    }

    override fun visitConstructor(context: JavaContext?, node: UCallExpression?, constructor: PsiMethod?) {
        val containingClass = constructor!!.containingClass
        if (containingClass != null) {
            val className = containingClass.qualifiedName
            if (className != null) {
                val config = ConfigUtil.getConfigByConstruction(className, this.config.configs)
                        ?: return
                if (context!!.evaluator.isMemberInClass(constructor, className)) {
                    context.report(CONSTRUCTOR_ISSUE, node, context.getLocation(node?.resolve()!!), config.message)
                }
            }
        }
    }

    /** ================================== class method ============================================ **/

    override fun getApplicableMethodNames(): List<String>? {
        return ConfigUtil.getMethodClasses(config.configs)
    }

    override fun visitMethod(context: JavaContext?, node: UCallExpression?, method: PsiMethod?) {
        val findConfigs = ConfigUtil.getConfigByMethod(method!!.name, this.config.configs)
        if (findConfigs != null && !findConfigs.isEmpty()) {
            for (config in findConfigs) {
                if (context!!.evaluator.isMemberInClass(method, config.methodByClass)) {
                    if (config.exception != null) {
                        context.report(HANDLE_EXCEPTION_ISSUE, node, context.getLocation(method), config.message)
                        return
                    }
                    context.report(METHOD_ISSUE, node, context.getLocation(method), config.message)
                    return
                }
            }
        }
    }

    /** ================================== super class ============================================ **/

    override fun applicableSuperClasses(): List<String>? {
        return ConfigUtil.getSuperClasses(config.configs)
    }

    override fun visitClass(context: JavaContext?, declaration: UClass?) {
        val configs = this.config.configs
        var isFindSuperClass = false
        for (config in configs) {
            if (config.superClass != null ) {
                val extendsList = declaration?.extendsList
                isFindSuperClass = false
                if (extendsList != null) {
                    val elements = extendsList.referenceElements
                    if (elements.isNotEmpty()) {
                        for (element in elements) {
                            if (element.qualifiedName == config.superClass) {
                                isFindSuperClass = true
                            }
                        }
                    }
                }
                if (isFindSuperClass) {
                    val location = context!!.getLocation(declaration?.uastAnchor!!)
                    if (config.exception != null) {
                        context.report(HANDLE_EXCEPTION_ISSUE, declaration, location, config.message)
                        return
                    }
                    context.report(SUPER_CLASS_ISSUE, declaration, location, config.message)
                }
                return
            }
        }
    }


    companion object {

        val CONSTRUCTOR_ISSUE = Issue.create(
                "CustomConstructorError",
                "Custom check exception",
                "自定义检查异常，需要改正",
                Category.SECURITY,
                6,
                Severity.ERROR,
                Implementation(ConfigDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )

        val SUPER_CLASS_ISSUE = Issue.create(
                "CustomSuperClassError",
                "Custom check exception",
                "自定义检查异常，需要改正",
                Category.SECURITY,
                6,
                Severity.ERROR,
                Implementation(ConfigDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )

        val HANDLE_EXCEPTION_ISSUE = Issue.create(
                "CustomHandleExceptionError",
                "Custom check exception",
                "自定义检查异常，需要改正",
                Category.SECURITY,
                6,
                Severity.ERROR,
                Implementation(ConfigDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )

        val METHOD_ISSUE = Issue.create(
                "CustomMethodError",
                "Custom check exception",
                "自定义检查异常，需要改正",
                Category.SECURITY,
                6,
                Severity.ERROR,
                Implementation(ConfigDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }
}
