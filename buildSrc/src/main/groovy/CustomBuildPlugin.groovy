package custom.build.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class CustomBuildPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.extensions.create('coverageCheckConfig', CoverageCheckTaskConfig)
        project.tasks.create('coverageCheck', CoverageCheckTask)
    }
}
