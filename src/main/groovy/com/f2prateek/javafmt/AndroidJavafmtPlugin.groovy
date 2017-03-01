package com.f2prateek.javafmt

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.StopExecutionException

class AndroidJavafmtPlugin implements Plugin<Project> {
  @Override void apply(Project project) {
    def variants
    if (hasPlugin(project, AppPlugin)) {
      variants = project.android.applicationVariants
    } else if (hasPlugin(project, LibraryPlugin)) {
      variants = project.android.libraryVariants
    } else {
      throw new StopExecutionException(
              "Must be applied with 'android' or 'android-library' plugin.")
    }

    def fmtTasks = []
    def checkFmtTasks = []

    variants.all { variant ->
      def fmt = project.tasks.create "fmt${variant.name.capitalize()}", JavaFmtTask
      fmt.dependsOn variant.javaCompile
      fmt.source variant.javaCompile.source
      fmt.exclude('**/BuildConfig.java')
      fmt.exclude('**/R.java')
      project.tasks.getByName("assemble").dependsOn fmt
      fmtTasks.add fmt

      // todo: consolidate.
      def checkFmt = project.tasks.create "checkFmt${variant.name.capitalize()}", CheckFmtTask
      checkFmt.dependsOn variant.javaCompile
      checkFmt.source variant.javaCompile.source
      checkFmt.exclude('**/BuildConfig.java')
      checkFmt.exclude('**/R.java')
      project.tasks.getByName("check").dependsOn checkFmt
      checkFmtTasks.add checkFmt
    }

    def fmt = project.tasks.create "fmt"
    fmt.dependsOn fmtTasks

    def checkFmt = project.tasks.create "checkFmt"
    checkFmt.dependsOn checkFmtTasks
  }

  static def hasPlugin(Project project, Class<? extends Plugin> plugin) {
    return project.plugins.hasPlugin(plugin)
  }
}