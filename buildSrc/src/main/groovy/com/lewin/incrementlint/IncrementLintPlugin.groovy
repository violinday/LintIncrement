package com.lewin.incrementlint

import com.android.build.gradle.LibraryPlugin
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
        project.pluginManager
        project.getTasks().create(IncrementLintTask.NAME, IncrementLintTask.class)
    }


}
