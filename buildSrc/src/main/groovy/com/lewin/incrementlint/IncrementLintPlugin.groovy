package com.lewin.incrementlint

import android.databinding.tool.DataBindingBuilder
import com.android.build.gradle.AndroidConfig
import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.BasePlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.TestExtension
import com.android.build.gradle.TestPlugin
import com.android.build.gradle.api.AndroidBasePlugin
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.BaseVariantOutput
import com.android.build.gradle.api.LibraryVariant
import com.android.build.gradle.internal.BuildCacheUtils
import com.android.build.gradle.internal.ExtraModelInfo
import com.android.build.gradle.internal.LoggerWrapper
import com.android.build.gradle.internal.SdkHandler
import com.android.build.gradle.internal.TaskManager
import com.android.build.gradle.internal.VariantManager
import com.android.build.gradle.internal.api.ApplicationVariantImpl
import com.android.build.gradle.internal.api.LibraryVariantImpl
import com.android.build.gradle.internal.dsl.BuildType
import com.android.build.gradle.internal.dsl.BuildTypeFactory
import com.android.build.gradle.internal.dsl.ProductFlavor
import com.android.build.gradle.internal.dsl.ProductFlavorFactory
import com.android.build.gradle.internal.dsl.SigningConfig
import com.android.build.gradle.internal.dsl.SigningConfigFactory
import com.android.build.gradle.internal.ndk.NdkHandler
import com.android.build.gradle.internal.process.GradleJavaProcessExecutor
import com.android.build.gradle.internal.process.GradleProcessExecutor
import com.android.build.gradle.internal.scope.GlobalScope
import com.android.build.gradle.internal.scope.VariantScope
import com.android.build.gradle.internal.variant.VariantFactory
import com.android.build.gradle.options.ProjectOptions
import com.android.builder.Version
import com.android.builder.core.AndroidBuilder
import com.android.builder.profile.Recorder
import com.android.builder.profile.ThreadRecorder
import com.android.builder.utils.FileCache
import com.android.utils.ILogger
import org.gradle.api.DomainObjectSet
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel
import org.gradle.api.plugins.PluginContainer
import org.gradle.internal.reflect.Instantiator
import org.gradle.tooling.provider.model.ToolingModelBuilderRegistry

import javax.inject.Inject

/**
 * Created by haiyang_tan on 2018/7/17.
 */
class IncrementLintPlugin extends TestPlugin {

//    Project project
//    GlobalScope globalScope
//    VariantManager variantManager
//    private ProjectOptions projectOptions;
//    private LoggerWrapper loggerWrapper;
//    private ExtraModelInfo extraModelInfo;
//    private String creator;
//    private AndroidBuilder androidBuilder;
//    private SdkHandler sdkHandler;
//    private BaseExtension extension;
//    private Instantiator instantiator;
//    private VariantFactory variantFactory;
//    private TaskManager taskManager;
//    private ToolingModelBuilderRegistry registry;
//    private NdkHandler ndkHandler;
//    private DataBindingBuilder dataBindingBuilder;
//    private Recorder threadRecorder;

    @Inject
    IncrementLintPlugin(Instantiator instantiator, ToolingModelBuilderRegistry registry) {
        super(instantiator, registry)
//        this.instantiator = instantiator;
//        creator = "Android Gradle " + Version.ANDROID_GRADLE_PLUGIN_VERSION;
    }

    @Override
    protected void registerModels(ToolingModelBuilderRegistry registry, GlobalScope globalScope, VariantManager variantManager, AndroidConfig config, ExtraModelInfo extraModelInfo) {
//        this.variantManager = variantManager
//        this.registry = registry;
        super.registerModels(registry, globalScope, variantManager, config, extraModelInfo)
    }

//    private ILogger getLogger() {
//        if (loggerWrapper == null) {
//            loggerWrapper = new LoggerWrapper(project.getLogger());
//        }
//
//        return loggerWrapper;
//    }
//
//    private boolean isVerbose() {
//        return project.getLogger().isEnabled(LogLevel.INFO);
//    }
//
    @Override
    void apply(Project project) {
        def baseExtension = project.extensions.getByName("android")
        if (baseExtension instanceof AppExtension) {
            AppExtension extension = (AppExtension) baseExtension
            DomainObjectSet<ApplicationVariant> variants = extension.getApplicationVariants()
            variants.all {
                if (it instanceof ApplicationVariantImpl) {
                    ApplicationVariantImpl variantImpl = (ApplicationVariantImpl) it
                    def globalScope = variantImpl.variantData.scope
                    project.getTasks().create(globalScope.getTaskName(IncrementLintTask.NAME), IncrementLintTask.class, new IncrementLintTask.VitalConfigAction(globalScope, getProject()))
                }
            }
        } else if (baseExtension instanceof LibraryExtension) {
            // 说明这个是library
//            project.getPluginManager().apply(AndroidBasePlugin.class);
//            this.projectOptions = new ProjectOptions(project)
//            extraModelInfo = new ExtraModelInfo(projectOptions, project.getLogger());
//
//            androidBuilder = new AndroidBuilder(
//                    project == project.getRootProject() ? project.getName() : project.getPath(),
//                    creator,
//                    new GradleProcessExecutor(project),
//                    new GradleJavaProcessExecutor(project),
//                    extraModelInfo,
//                    getLogger(),
//                    isVerbose());
//            sdkHandler = new SdkHandler(project, getLogger());
//            final NamedDomainObjectContainer<BuildType> buildTypeContainer =
//                    project.container(
//                            BuildType.class,
//                            new BuildTypeFactory(instantiator, project, extraModelInfo));
//            final NamedDomainObjectContainer<ProductFlavor> productFlavorContainer =
//                    project.container(
//                            ProductFlavor.class,
//                            new ProductFlavorFactory(
//                                    instantiator, project, project.getLogger(), extraModelInfo));
//            final NamedDomainObjectContainer<SigningConfig> signingConfigContainer =
//                    project.container(SigningConfig.class, new SigningConfigFactory(instantiator));
//
//            final NamedDomainObjectContainer<BaseVariantOutput> buildOutputs =
//                    project.container(BaseVariantOutput.class);
//            extension =
//                    project.getExtensions()
//                            .create(
//                            "androidtest",
//                            TestExtension.class,
//                            project,
//                            projectOptions,
//                            instantiator,
//                            androidBuilder,
//                            sdkHandler,
//                            buildTypeContainer,
//                            productFlavorContainer,
//                            signingConfigContainer,
//                            buildOutputs,
//                            extraModelInfo);
//            variantFactory = createVariantFactory(globalScope, instantiator, androidBuilder, extension);
//            dataBindingBuilder = new DataBindingBuilder();
//            ndkHandler =
//                    new NdkHandler(
//                            project.getRootDir(),
//                            null, /* compileSkdVersion, this will be set in afterEvaluate */
//                            "gcc",
//                            "" /*toolchainVersion*/,
//                            false /* useUnifiedHeaders */);
//            threadRecorder = ThreadRecorder.get();
//            FileCache buildCache = BuildCacheUtils.createBuildCacheIfEnabled(project, projectOptions);
//            globalScope =
//                    new GlobalScope(
//                            project,
//                            projectOptions,
//                            androidBuilder,
//                            extension,
//                            sdkHandler,
//                            ndkHandler,
//                            registry,
//                            buildCache);
//            taskManager =
//                    createTaskManager(
//                            globalScope,
//                            project,
//                            projectOptions,
//                            androidBuilder,
//                            dataBindingBuilder,
//                            extension,
//                            sdkHandler,
//                            ndkHandler,
//                            registry,
//                            threadRecorder);
//            variantManager =
//                    new VariantManager(
//                            globalScope,
//                            project,
//                            projectOptions,
//                            androidBuilder,
//                            extension,
//                            variantFactory,
//                            taskManager,
//                            threadRecorder);

            LibraryExtension extension = (LibraryExtension) baseExtension
            DomainObjectSet<LibraryVariant> variants = extension.getLibraryVariants()
            variants.all {
                if (it instanceof LibraryVariantImpl) {
                    LibraryVariantImpl variantImpl = (LibraryVariantImpl) it
                    variantImpl.getTestVariant().
//                    def globalScope = variantImpl.variantData.scope
//                    project.getTasks().create(globalScope.getTaskName(IncrementLintTask.NAME), IncrementLintTask.class, new IncrementLintTask.VitalConfigAction(globalScope, getProject()))
                    if (!project.getPlugins().isEmpty()) {
                        PluginContainer container = project.getPlugins();
                        LibraryPlugin plugin = container.findPlugin(LibraryPlugin.class)
                    }
                    List<VariantScope> scopes = variantManager.getVariantScopes()

                    for (VariantScope scope : scopes) {
                        project.getTasks().create(scope.getTaskName(IncrementLintTask.NAME), IncrementLintTask.class, new IncrementLintTask.VitalConfigAction(globalScope, getProject()))
                    }
                }
            }
        }

    }


}
