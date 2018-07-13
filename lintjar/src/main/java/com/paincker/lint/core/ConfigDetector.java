package com.paincker.lint.core;

import com.android.resources.ResourceType;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.*;

import java.util.List;

/**
 * Created by haiyang_tan on 2018/7/13.
 */
public class ConfigDetector extends Detector implements Detector.JavaPsiScanner {

    LintConfig config;

    @Override
    public void beforeCheckProject(Context context) {
        // 读取配置
        config = new LintConfig(context);
    }

    @Override
    public List<String> getApplicableConstructorTypes() {
        return super.getApplicableConstructorTypes();
    }

    @Override
    public void visitConstructor(JavaContext context, JavaElementVisitor visitor, PsiNewExpression node, PsiMethod constructor) {
        super.visitConstructor(context, visitor, node, constructor);
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
        return super.applicableSuperClasses();
    }

    @Override
    public void checkClass(JavaContext context, PsiClass declaration) {
        super.checkClass(context, declaration);
    }
}
