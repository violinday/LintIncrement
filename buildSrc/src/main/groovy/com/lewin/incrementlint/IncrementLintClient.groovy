package com.lewin.incrementlint

import com.android.build.gradle.internal.LintGradleClient
import com.android.build.gradle.tasks.LintBaseTask
import com.android.builder.model.AndroidProject
import com.android.builder.model.Variant
import com.android.sdklib.BuildToolInfo
import com.android.tools.lint.LintCliFlags
import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.LintRequest
import com.android.tools.lint.detector.api.Project

class IncrementLintClient extends LintGradleClient {

    private org.gradle.api.Project gradleProject
    private AndroidProject modelProject
    private List<String> changeFiles

    IncrementLintClient(IssueRegistry registry,
                        LintCliFlags flags,
                        org.gradle.api.Project gradleProject,
                        AndroidProject modelProject,
                        File sdkHome, Variant variant,
                        LintBaseTask.VariantInputs variantInputs,
                        BuildToolInfo buildToolInfo, List<String> changeFiles) {
        super(registry, flags, gradleProject, modelProject, sdkHome, variant, variantInputs, buildToolInfo)
        this.gradleProject = gradleProject
        this.modelProject = modelProject
        this.changeFiles = changeFiles
    }

    @Override
    protected LintRequest createLintRequest(List<File> files) {
        LintRequest lintRequest = super.createLintRequest(files)
        addChangeFile(lintRequest)
        return lintRequest
    }



    private addChangeFile(LintRequest lintRequest) {
        List<String> commitChanges = changeFiles
        for (String commitChange : commitChanges) {
            for (Project project : lintRequest.getProjects()) {
                project.addFile(new File(commitChange))//加入要扫描的文件
            }
        }
    }
}