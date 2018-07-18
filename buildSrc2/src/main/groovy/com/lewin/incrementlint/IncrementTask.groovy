package com.lewin.incrementlint

import com.android.annotations.NonNull
import com.android.build.gradle.tasks.LintBaseTask
import com.android.builder.model.AndroidProject
import com.android.builder.model.Variant
import com.android.tools.lint.LintCliFlags
import com.android.tools.lint.Warning
import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.LintBaseline
import com.android.utils.Pair
import org.gradle.api.GradleException
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

class IncrementTask extends LintBaseTask {

    public static final String GROUP = 'check'
    public static final String NAME = "LintIncrementTask"

    private static final String VERSION = "26.1.3"

    IncrementTask() {
        group = GROUP
    }

    @TaskAction
    private void lint() {
        AndroidProject modelProject = createAndroidProject(getProject());
        for (Variant variant : modelProject.getVariants()) {
            if (variant.getName().equals(getVariantName())) {
                lintSingleVariant(modelProject, variant);
                break
            }
        }
    }

    @InputFiles
    @Optional
    public FileCollection getVariantInputs() {
        return variantInputs.getAllInputs()
    }


    void lintSingleVariant(@NonNull AndroidProject modelProject, @NonNull Variant variant) {
        runLint(modelProject, variant, variantInputs, tru)
    }


    @Override
    protected Pair<List<Warning>, LintBaseline> runLint(AndroidProject modelProject, Variant variant, VariantInputs variantInputs, boolean report) {
        IssueRegistry registry = createIssueRegistry()
        LintCliFlags flags = new LintCliFlags()
        IncrementLintClient client =
                new IncrementLintClient(
                        registry,
                        flags,
                        getProject(),
                        modelProject,
                        sdkHome,
                        variant,
                        variantInputs,
                        getBuildTools())
        if (fatalOnly) {
            flags.setFatalOnly(true)
        }
        if (lintOptions != null) {
            syncOptions(
                    lintOptions,
                    client,
                    flags,
                    variant,
                    getProject(),
                    reportsDir,
                    report,
                    fatalOnly)
        }
        if (!report || fatalOnly) {
            flags.setQuiet(true)
        }
        flags.setWriteBaselineIfMissing(report && !fatalOnly)

        Pair<List<Warning>, LintBaseline> warnings
        try {
            warnings = client.run(registry)
        } catch (IOException e) {
            throw new GradleException("Invalid arguments.", e)
        }

        if (report && client.haveErrors() && flags.isSetExitCode()) {
            abort()
        }

        return warnings
    }
}