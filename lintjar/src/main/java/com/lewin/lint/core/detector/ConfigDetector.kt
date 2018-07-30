package com.lewin.lint.core.detector

import com.android.tools.lint.detector.api.*
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.lewin.lint.core.config.Config
import com.lewin.lint.core.config.ConfigUtil
import com.lewin.lint.core.config.LintConfig
import org.jetbrains.uast.*

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
                    context.report(CONSTRUCTOR_ERROR_ISSUE, node,
                            context.getCallLocation(node!!, false, false),
                            config.message)
                }
            }
        }
    }

    /** ================================== class method ============================================ **/

    override fun getApplicableMethodNames(): List<String>? {
        return ConfigUtil.getMethodClasses(config.configs)
    }

    override fun visitMethod(context: JavaContext?, visitor: JavaElementVisitor?, call: PsiMethodCallExpression?, method: PsiMethod?) {
        super.visitMethod(context, visitor, call, method)
    }

    override fun visitMethod(context: JavaContext?, node: UCallExpression?, method: PsiMethod?) {
        val findConfigs = ConfigUtil.getConfigByMethod(method!!.name, this.config.configs)
        if (findConfigs != null && !findConfigs.isEmpty()) {
            for (config in findConfigs) {
                if (context!!.evaluator.isMemberInClass(method, config.methodByClass)) {
                    if (config.exception != null) {
                        if (!inCatchConfigException(node, config.exception)) {
                            context.report(HANDLE_EXCEPTION_ERROR_ISSUE, node, context.getCallLocation(node!!, false,false), config.message)
                            return
                        }
                    } else {
                        context.report(METHOD_ERROR_ISSUE, node, context.getCallLocation(node!!, false, true), config.message)
                        return
                    }
                }
            }
        }
    }



    /** ================================== super class ============================================ **/

    override fun applicableSuperClasses(): List<String>? {
        return ConfigUtil.getSuperClasses(config.configs)
    }

    override fun visitClass(context: JavaContext?, declaration: UClass?) {
        val isAnonymous = declaration is UAnonymousClass

        val configs = this.config.configs
        for (config in configs) {
            if (config.superClass != null ) {
                if (context!!.evaluator.inheritsFrom(declaration, config.superClass, false)) {
                    val invocation =  declaration?.getParentOfType<UObjectLiteralExpression>(true)
                    val location = if (isAnonymous && invocation != null) {
                        context.getCallLocation(invocation, false, false)
                    } else {
                        context.getNameLocation(declaration!!)
                    }
                    reportSuperClassIssue(config, context, declaration, location)
                    return
                }
            }
        }
    }

    /** ================================== utils ============================================ **/

    private fun inCatchConfigException(scope: UExpression?, exception: String): Boolean {
        val surroundingCatchSection = scope!!.getParentOfType<UCatchClause>(true)
        if (surroundingCatchSection != null) {
            for (t in surroundingCatchSection.types) {
                if (t.equalsToText(exception)) {
                    return true
                }
            }
        }
        return false
    }

    private fun reportSuperClassIssue(config: Config,
                                      context: JavaContext?,
                                      declaration: UClass?,
                                      location: Location?) {
        if (LintConfig.WARNING.equals(config.severity))  {
            context!!.report(SUPER_CLASS_WARN_ISSUE, declaration, location!!, config.message)
        } else if (LintConfig.ERROR.equals(config.severity)) {
            context!!.report(SUPER_CLASS_ERROR_ISSUE, declaration, location!!, config.message)
        }
    }

    companion object {

        val CONSTRUCTOR_ERROR_ISSUE = Issue.create(
                "CustomConstructorError",
                "Custom check exception",
                "自定义检查异常，需要改正",
                Category.SECURITY,
                6,
                Severity.ERROR,
                Implementation(ConfigDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )

        val CONSTRUCTOR_WARN_ISSUE = Issue.create(
                "CustomConstructorError",
                "Custom check exception",
                "自定义检查异常，需要改正",
                Category.SECURITY,
                5,
                Severity.WARNING,
                Implementation(ConfigDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )

        val SUPER_CLASS_ERROR_ISSUE = Issue.create(
                "CustomSuperClassError",
                "Custom check exception",
                "自定义检查异常，需要改正",
                Category.SECURITY,
                6,
                Severity.WARNING,
                Implementation(ConfigDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )

        val SUPER_CLASS_WARN_ISSUE = Issue.create(
                "CustomSuperClassError",
                "Custom check exception",
                "自定义检查异常，需要改正",
                Category.SECURITY,
                5,
                Severity.WARNING,
                Implementation(ConfigDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )

        val HANDLE_EXCEPTION_ERROR_ISSUE = Issue.create(
                "CustomHandleExceptionError",
                "Custom check exception",
                "自定义检查异常，需要改正",
                Category.SECURITY,
                6,
                Severity.ERROR,
                Implementation(ConfigDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )

        val HANDLE_EXCEPTION_WARN_ISSUE = Issue.create(
                "CustomHandleExceptionError",
                "Custom check exception",
                "自定义检查异常，需要改正",
                Category.SECURITY,
                5,
                Severity.WARNING,
                Implementation(ConfigDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )

        val METHOD_ERROR_ISSUE = Issue.create(
                "CustomMethodError",
                "Custom check exception",
                "自定义检查异常，需要改正",
                Category.SECURITY,
                6,
                Severity.ERROR,
                Implementation(ConfigDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )

        val METHOD_WARN_ISSUE = Issue.create(
                "CustomMethodError",
                "Custom check exception",
                "自定义检查异常，需要改正",
                Category.SECURITY,
                5,
                Severity.WARNING,
                Implementation(ConfigDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }
}
