//package com.lewin.incrementlint;
//
//import com.android.annotations.NonNull;
//import com.android.annotations.Nullable;
//import com.android.build.gradle.tasks.LintBaseTask;
//import com.android.builder.model.AndroidProject;
//import com.android.builder.model.LintOptions;
//import com.android.builder.model.Variant;
//import com.android.sdklib.BuildToolInfo;
//import com.android.tools.lint.LintCliClient;
//import com.android.tools.lint.LintCliFlags;
//import com.android.tools.lint.client.api.Configuration;
//import com.android.tools.lint.client.api.DefaultConfiguration;
//import com.android.tools.lint.client.api.IssueRegistry;
//import com.android.tools.lint.client.api.LintDriver;
//import com.android.tools.lint.detector.api.Issue;
//import com.android.tools.lint.detector.api.Project;
//import com.android.tools.lint.detector.api.Severity;
//
//import java.io.File;
//import java.util.Map;
//
//public class IncrementLintClient2 extends LintCliClient{
//
//    private static final String VERSION = "3.0.1-lint-26.0.1-gradle-4.4";
//
//    private final AndroidProject modelProject;
//
//    /**
//     * Variant to run the client on.
//     */
////    @NonNull private final Variant variant;
//
//    private final org.gradle.api.Project gradleProject;
//    private File sdkHome;
////    @NonNull private final LintBaseTask.VariantInputs variantInputs;
//    private final BuildToolInfo buildToolInfo;
//
//    public IncrementLintClient2(@NonNull IssueRegistry registry,
//                                @NonNull LintCliFlags flags,
//                                @NonNull org.gradle.api.Project gradleProject,
//                                @NonNull AndroidProject modelProject,
//                                @Nullable File sdkHome,
////                                @NonNull Variant variant,
////                                @NonNull LintBaseTask.VariantInputs variantInputs,
//                                @Nullable BuildToolInfo buildToolInfo) {
//
//        super(flags, CLIENT_GRADLE);
//        this.gradleProject = gradleProject;
//        this.modelProject = modelProject;
//        this.sdkHome = sdkHome;
////        this.variantInputs = variantInputs;
//        this.registry = registry;
//        this.buildToolInfo = buildToolInfo;
////        this.variant = variant;
//    }
//
//    @Override
//    public String getClientRevision() {
//        return VERSION;
//    }
//
//    @Override
//    protected Configuration getConfiguration() {
//        return super.getConfiguration();
//    }
//
//    @Override
//    public Configuration getConfiguration(Project project, LintDriver driver) {
//        AndroidProject gradleProjectModel = project.getGradleProjectModel();
//        if (gradleProjectModel != null) {
//            LintOptions lintOptions = gradleProjectModel.getLintOptions();
//            File lintXml = lintOptions.getLintConfig();
//            if (lintXml == null) {
//                lintXml = new File(project.getDir(), DefaultConfiguration.CONFIG_FILE_NAME);
//            }
//
//            Map<String, Integer> overrides = lintOptions.getSeverityOverrides();
//            if (overrides != null && !overrides.isEmpty()) {
//                return new CliConfiguration(lintXml, getConfiguration(), project,
//                        flags.isFatalOnly()) {
//                    @NonNull
//                    @Override
//                    public Severity getSeverity(@NonNull Issue issue) {
//                        Integer optionSeverity = overrides.get(issue.getId());
//                        if (optionSeverity != null) {
//                            Severity severity = Severity.fromLintOptionSeverity(optionSeverity);
//
//                            if (flags.isFatalOnly() && severity != Severity.FATAL) {
//                                return Severity.IGNORE;
//                            }
//
//                            return severity;
//                        }
//
//                        return super.getSeverity(issue);
//                    }
//                };
//            }
//        }
//
//        return super.getConfiguration(project, driver);
//    }
//}
