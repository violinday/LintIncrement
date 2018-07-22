package com.lewin.incrementlint

import com.android.build.gradle.AndroidConfig
import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestPlugin
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.LibraryVariant
import com.android.build.gradle.internal.ExtraModelInfo
import com.android.build.gradle.internal.VariantManager
import com.android.build.gradle.internal.api.ApplicationVariantImpl
import com.android.build.gradle.internal.api.LibraryVariantImpl
import com.android.build.gradle.internal.scope.GlobalScope
import com.android.build.gradle.internal.variant.LibraryVariantData
import org.gradle.api.DomainObjectSet
import org.gradle.api.Project
import org.gradle.internal.reflect.Instantiator
import org.gradle.tooling.provider.model.ToolingModelBuilderRegistry

import javax.inject.Inject
import java.lang.reflect.Field

/**
 * Created by haiyang_tan on 2018/7/17.
 */
class IncrementLintPlugin extends TestPlugin {

    @Inject
    IncrementLintPlugin(Instantiator instantiator, ToolingModelBuilderRegistry registry) {
        super(instantiator, registry)
    }

    @Override
    protected void registerModels(ToolingModelBuilderRegistry registry, GlobalScope globalScope, VariantManager variantManager, AndroidConfig config, ExtraModelInfo extraModelInfo) {
        super.registerModels(registry, globalScope, variantManager, config, extraModelInfo)
    }


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
            LibraryExtension extension = (LibraryExtension) baseExtension
            DomainObjectSet<LibraryVariant> variants = extension.getLibraryVariants()
            variants.all {
                if (it instanceof LibraryVariantImpl) {
                    LibraryVariantImpl variantImpl = (LibraryVariantImpl) it
                    def globalScope = getVariantDataByLibrary(variantImpl)
                    project.getTasks().create(globalScope.scope.getTaskName(IncrementLintTask.NAME), IncrementLintTask.class, new IncrementLintTask.VitalConfigAction(globalScope.scope, getProject()))
                }
            }
        }

    }

    /**
     * LibraryVariantImpl 中没有对外暴露接口，和Application中不一致，先用反射搞定
     * @param variant
     * @return
     */
    static LibraryVariantData getVariantDataByLibrary(LibraryVariantImpl variant) {
        Class<LibraryVariantImpl> libraryVariantClass =
                Class.forName("com.android.build.gradle.internal.api.LibraryVariantImpl")

        Field field = libraryVariantClass.getDeclaredField("variantData")
        field.setAccessible(true)
        return field.get(variant)
    }

}
