package com.paincker.lint.core

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.Issue
import com.paincker.lint.core.detector.ConfigDetector
import com.paincker.lint.core.detector.LogDetector

import java.util.Arrays

/**
 * Created by jzj on 2017/7/4.
 */
class CustomIssueRegistry : IssueRegistry() {

    @Synchronized
    override fun getIssues(): List<Issue> {
        println("==== my lint start ====")
        return Arrays.asList(LogDetector.ISSUE, ConfigDetector.CONSTRUCTOR_ISSUE, ConfigDetector.SUPER_CLASS_ISSUE)
    }
}
