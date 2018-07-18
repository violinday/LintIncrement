package com.lewin.incrementlint

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.internal.api.ApplicationVariantImpl
import org.gradle.api.DomainObjectSet
import org.gradle.api.Project
import org.gradle.internal.reflect.Instantiator
import org.gradle.tooling.provider.model.ToolingModelBuilderRegistry

import javax.inject.Inject

/**
 * Created by haiyang_tan on 2018/7/17.
 */
class IncrementLintPlugin extends LibraryPlugin {

    Project project

    @Inject
    IncrementLintPlugin(Instantiator instantiator, ToolingModelBuilderRegistry registry) {
        super(instantiator, registry)
    }


    @Override
    void apply(Project project) {
        this.project = project
        AppExtension extension = project.extensions.getByName("android")
        DomainObjectSet<ApplicationVariant> variants = extension.getApplicationVariants()
        variants.all {
            if (it instanceof ApplicationVariantImpl) {
                ApplicationVariantImpl variantImpl = (ApplicationVariantImpl) it
                def globalScope = variantImpl.variantData.scope
                project.getTasks().create(globalScope.getTaskName(IncrementLintTask.NAME), IncrementLintTask.class, new IncrementLintTask.VitalConfigAction(globalScope, getProject()))
            }
        }
    }


}
