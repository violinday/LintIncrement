package com.lewin.lint.core

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.Issue
import com.lewin.lint.core.detector.ConfigDetector

import java.util.Arrays

class CustomIssueRegistry : IssueRegistry() {

    @Synchronized
    override fun getIssues(): List<Issue> {
        println("==== my lint start ====")
        return Arrays.asList(ConfigDetector.CONSTRUCTOR_ISSUE, ConfigDetector.SUPER_CLASS_ISSUE)
    }
}
