package com.lewin.incrementlint;

import com.android.annotations.NonNull;
import com.android.build.gradle.internal.scope.VariantScope;
import com.android.build.gradle.tasks.GroovyGradleDetector;
import com.android.build.gradle.tasks.LintBaseTask;
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
    private List<String> changeFiles;

    public IncrementLintTask() {
    }

    private VariantInputs variantInputs;

    @Override
    protected void setFatalOnly(boolean fatalOnly) {
        super.setFatalOnly(fatalOnly);
        this.fatalOnly = fatalOnly;
    }

    public static void logInfo(Project project, String message) {
        project.getLogger().info(NAME, message);
    }

    private static BuiltinIssueRegistry createIssueRegistry() {
        return new LintGradleIssueRegistry();
    }

    public void setChangeFiles(List<String> changeFiles) {
        this.changeFiles = changeFiles;
    }

    @TaskAction
    public void lint() throws IOException {
        AndroidProject modelProject = createAndroidProject(getProject());
        for (Variant variant : modelProject.getVariants()) {
            if (variant.getName().equals(getVariantName())) {
                lintSingleVariant(modelProject, variant);
                break;
            }
        }
    }

    public void lintSingleVariant(@NonNull AndroidProject modelProject, @NonNull Variant variant) {
        runLint(modelProject, variant, variantInputs, true);
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
                        getBuildTools(),
                        changeFiles);
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

    private static class LintGradleIssueRegistry extends BuiltinIssueRegistry {
        private boolean mInitialized;

        public LintGradleIssueRegistry() {}

        @NonNull
        @Override
        public List<Issue> getIssues() {
            List<Issue> issues = super.getIssues();
            if (!mInitialized) {
                mInitialized = true;
                for (Issue issue : issues) {
                    if (issue.getImplementation().getDetectorClass() == GradleDetector.class) {
                        issue.setImplementation(IMPLEMENTATION);
                    }
                }
            }

            return issues;
        }
    }

    static final Implementation IMPLEMENTATION = new Implementation(
            GroovyGradleDetector.class,
            Scope.GRADLE_SCOPE);

    public static class VitalConfigAction extends BaseConfigAction<IncrementLintTask> {

        private final VariantScope scope;
        private final Project project;
        private final List<String> changeFiles;

        public VitalConfigAction(@NonNull VariantScope scope, Project project, List<String> changeFiles) {
            super(scope.getGlobalScope());
            this.scope = scope;
            this.project = project;
            this.changeFiles = changeFiles;
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
            task.changeFiles = changeFiles;
            task.setFatalOnly(false);
            task.setDescription(
                    "Runs lint on just the fatal issues in the " + variantName + " build.");
            project.getTasks().add(task);
        }
    }
}
