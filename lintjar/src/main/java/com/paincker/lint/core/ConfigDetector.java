package com.paincker.lint.core;

import com.android.resources.ResourceType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiNewExpression;
import com.paincker.lint.core.config.Config;
import com.paincker.lint.core.config.ConfigUtil;
import com.paincker.lint.core.config.LintConfig;

import java.util.List;

/**
 * Created by haiyang_tan on 2018/7/13.
 */
public class ConfigDetector extends Detector implements Detector.JavaPsiScanner {

    public static final Issue ISSUE = Issue.create(
            "CustomError",
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
                    context.report(ISSUE, node, context.getLocation(node), config.message);
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
        context.getEvaluator().extendsClass(declaration, "", true);
    }
}
