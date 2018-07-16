package com.paincker.lint.core;

import com.android.resources.ResourceType;
import com.android.tools.lint.detector.api.*;
import com.intellij.psi.*;
import com.paincker.lint.core.config.Config;
import com.paincker.lint.core.config.ConfigUtil;
import com.paincker.lint.core.config.LintConfig;

import java.util.List;

/**
 * Created by haiyang_tan on 2018/7/13.
 *
 * 自定义Lint规则，根据project目录下规则文件进行检查
 */
public class ConfigDetector extends Detector implements Detector.JavaPsiScanner {

    public static final Issue CONSTRUCTOR_ISSUE = Issue.create(
            "CustomConstructorError",
            "Custom check exception",
            "自定义检查异常，需要改正",
            Category.SECURITY,
            6,
            Severity.ERROR,
            new Implementation(ConfigDetector.class, Scope.JAVA_FILE_SCOPE)
    );

    public static final Issue SUPER_CLASS_ISSUE = Issue.create(
            "CustomSuperClassError",
            "Custom check exception",
            "自定义检查异常，需要改正",
            Category.SECURITY,
            6,
            Severity.ERROR,
            new Implementation(ConfigDetector.class, Scope.JAVA_FILE_SCOPE)
    );

    public static final Issue HANDLE_EXCEPTION_ISSUE = Issue.create(
            "CustomHandleExceptionError",
            "Custom check exception",
            "自定义检查异常，需要改正",
            Category.SECURITY,
            6,
            Severity.ERROR,
            new Implementation(ConfigDetector.class, Scope.JAVA_FILE_SCOPE)
    );

    LintConfig config;

    @Override
    public void beforeCheckProject(Context context) {
        // 读取配置
        config = new LintConfig(context);
    }

    @Override
    public List<String> getApplicableConstructorTypes() {
        return ConfigUtil.getConstructions(config.getConfigs());
    }

    @Override
    public void visitConstructor(JavaContext context, JavaElementVisitor visitor, PsiNewExpression node, PsiMethod constructor) {
        PsiClass containingClass = constructor.getContainingClass();
        if (containingClass != null ) {
            String className = containingClass.getQualifiedName();
            if (className != null) {
                Config config = ConfigUtil.getConfigByConstruction(className, this.config.getConfigs());
                if (config == null) {
                    return;
                }
                if (context.getEvaluator().isMemberInClass(constructor, className))
                    context.report(CONSTRUCTOR_ISSUE, node, context.getLocation(node), config.message);
            }
        }
    }

    @Override
    public List<String> getApplicableMethodNames() {
        return super.getApplicableMethodNames();
    }

    @Override
    public void visitMethod(JavaContext context, JavaElementVisitor visitor, PsiMethodCallExpression call, PsiMethod method) {
        super.visitMethod(context, visitor, call, method);
    }

    @Override
    public List<String> getApplicableReferenceNames() {
        return super.getApplicableReferenceNames();
    }

    @Override
    public void visitReference(JavaContext context, JavaElementVisitor visitor, PsiJavaCodeReferenceElement reference, PsiElement referenced) {
        super.visitReference(context, visitor, reference, referenced);
    }

    @Override
    public boolean appliesToResourceRefs() {
        return super.appliesToResourceRefs();
    }

    @Override
    public void visitResourceReference(JavaContext context, JavaElementVisitor visitor, PsiElement node, ResourceType type, String name, boolean isFramework) {
        super.visitResourceReference(context, visitor, node, type, name, isFramework);
    }

    @Override
    public List<String> applicableSuperClasses() {
        return ConfigUtil.getSuperClasses(config.getConfigs());
    }

    @Override
    public void checkClass(JavaContext context, PsiClass declaration) {
        List<Config> configs = this.config.getConfigs();
        for (Config config : configs) {
            if (config.superClass != null && context.getEvaluator().extendsClass(declaration, config.superClass, true)) {
                PsiElement locationNode = JavaContext.findNameElement(declaration);
                if (locationNode == null) {
                    locationNode = declaration;
                }
                Location location = context.getLocation(locationNode);
                if (config.exception != null) {
                    context.report(HANDLE_EXCEPTION_ISSUE,  locationNode, location, config.message);
                    return;
                }
                context.report(SUPER_CLASS_ISSUE,  locationNode, location, config.message);
                return;
            }
        }
    }
}
