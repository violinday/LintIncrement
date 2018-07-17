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


    IncrementLintClient(IssueRegistry registry, LintCliFlags flags, org.gradle.api.Project gradleProject, AndroidProject modelProject, File sdkHome, Variant variant, LintBaseTask.VariantInputs variantInputs, BuildToolInfo buildToolInfo) {
        super(registry, flags, gradleProject, modelProject, sdkHome, variant, variantInputs, buildToolInfo)
    }

    @Override
    protected LintRequest createLintRequest(List<File> files) {
        LintRequest lintRequest = super.createLintRequest(files);
        for (Project project : lintRequest.getProjects()) {
            project.addFile(changefile)//加入要扫描的文件
        }
        return lintRequest
    }

    private List<String> getPostCommitChange() {
        ArrayList<String> filterList = new ArrayList<String>()
        try {
            String projectDir = getProject().getProjectDir()
            String commond = "git diff --name-only --diff-filter=ACMRTUXB  HEAD~1 HEAD~0 $projectDir"
            String changeInfo = commond.execute(null, project.getRootDir()).text.trim()
            if (changeInfo == null || changeInfo.empty) {
                return filterList
            }
            String[] lines = changeInfo.split("\\n")
            return lines.toList()
        } catch (Exception e) {
            return filterList
        }
    }
}