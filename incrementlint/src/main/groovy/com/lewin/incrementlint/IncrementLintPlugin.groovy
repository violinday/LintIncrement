package com.lewin.incrementlint

import com.android.build.gradle.internal.LoggerWrapper
import com.android.build.gradle.internal.SdkHandler
import com.android.utils.ILogger
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.internal.reflect.Instantiator
import org.gradle.tooling.provider.model.ToolingModelBuilderRegistry

/**
 * Created by haiyang_tan on 2018/7/17.
 */
class IncrementLintPlugin extends BasePlugin{

    Project project

    def IncrementLintPlugin(Instantiator instantiator, ToolingModelBuilderRegistry registry) {
        super(instantiator, registry)
    }

    @Override
    void apply(Project project) {
        this.project = project
        project.tasks.create(IncrementTask.NAME, new IncrementTask())
    }


}
