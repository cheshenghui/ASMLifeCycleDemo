package com.csh.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

public class LifeCyclePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def android = project.extensions.getByType(AppExtension)

        LifeCycleTransform lt = new LifeCycleTransform()

        android.registerTransform(lt)

    }
}