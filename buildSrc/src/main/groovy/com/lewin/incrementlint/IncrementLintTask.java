package com.lewin.incrementlint;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.build.gradle.internal.scope.VariantScope;
import com.android.build.gradle.tasks.GroovyGradleDetector;
import com.android.build.gradle.tasks.LintBaseTask;
import com.android.build.gradle.tasks.LintPerVariantTask;
import com.android.builder.model.AndroidProject;
import com.android.builder.model.Variant;
import com.android.tools.lint.LintCliFlags;
import com.android.tools.lint.Warning;
import com.android.tools.lint.checks.BuiltinIssueRegistry;
import com.android.tools.lint.checks.GradleDetector;
import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.client.api.LintBaseline;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Scope;
import com.android.utils.Pair;

import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;
import java.util.List;

public class IncrementLintTask extends LintBaseTask{

    public static final String NAME = "LintIncrementTask";
    private boolean fatalOnly;

    public IncrementLintTask() {
    }

    private VariantInputs variantInputs;


    @TaskAction
    public void lint() throws IOException {
        runLint(new LintPerVariantTaskDescriptor());
    }

    private class LintPerVariantTaskDescriptor extends LintBaseTaskDescriptor {
        @Nullable
        @Override
        public String getVariantName() {
            return IncrementLintTask.this.getVariantName();
        }

        @Nullable
        @Override
        public VariantInputs getVariantInputs(@NonNull String variantName) {
            assert variantName.equals(getVariantName());
            return variantInputs;
        }

        @Override
        public boolean isFatalOnly() {
            return fatalOnly;
        }
    }

    public void lintSingleVariant(@NonNull AndroidProject modelProject, @NonNull Variant variant) {
        runLint(modelProject, variant, variantInputs, true);
    }

    @Override
    protected void runLint(LintBaseTaskDescriptor descriptor) {
        super.runLint(descriptor);
    }

    @Override
    protected Pair<List<Warning>, LintBaseline> runLint(AndroidProject modelProject, Variant variant, VariantInputs variantInputs, boolean report) {
        IssueRegistry registry = createIssueRegistry();
        LintCliFlags flags = new LintCliFlags();
        IncrementLintClient client =
                new IncrementLintClient(
                        registry,
                        flags,
                        getProject(),
                        modelProject,
                        sdkHome,
                        variant,
                        variantInputs,
                        getBuildTools());
        if (fatalOnly) {
            flags.setFatalOnly(true);
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
                    fatalOnly);
        }
        if (!report || fatalOnly) {
            flags.setQuiet(true);
        }
        flags.setWriteBaselineIfMissing(report && !fatalOnly);

        Pair<List<Warning>, LintBaseline> warnings;
        try {
            warnings = client.run(registry);
        } catch (IOException e) {
            throw new GradleException("Invalid arguments.", e);
        }

        if (report && client.haveErrors() && flags.isSetExitCode()) {
            abort();
        }

        return warnings;
    }

    @InputFiles
    @Optional
    public FileCollection getVariantInputs() {
        return variantInputs.getAllInputs();
    }

    public static class VitalConfigAction extends BaseConfigAction<IncrementLintTask> {

        private final VariantScope scope;
        private final Project project;

        public VitalConfigAction(@NonNull VariantScope scope, Project project) {
            super(scope.getGlobalScope());
            this.scope = scope;
            this.project = project;
        }

        @NonNull
        @Override
        public String getName() {
            return scope.getTaskName(NAME);
        }

        @NonNull
        @Override
        public Class<IncrementLintTask> getType() {
            return IncrementLintTask.class;
        }

        @Override
        public void execute(@NonNull IncrementLintTask task) {
            super.execute(task);

            String variantName = scope.getVariantData().getVariantConfiguration().getFullName();
            task.setVariantName(variantName);

            task.variantInputs = new VariantInputs(scope);

            task.fatalOnly = false;
            task.setDescription(
                    "Runs lint on just the fatal issues in the " + variantName + " build.");
            project.getTasks().add(task);
        }
    }
}
