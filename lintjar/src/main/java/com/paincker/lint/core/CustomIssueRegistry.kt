package com.paincker.lint.core

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.Issue
import com.paincker.lint.core.detector.ConfigDetector
import java.util.*

/**
 * Created by jzj on 2017/7/4.
 */
class CustomIssueRegistry() : IssueRegistry() {

    override val issues: List<Issue> = Arrays.asList(ConfigDetector.CONSTRUCTOR_ISSUE, ConfigDetector.SUPER_CLASS_ISSUE)

}
