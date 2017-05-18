package com.f2prateek.javafmt

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.StopExecutionException

import java.util.regex.Pattern

class AndroidJavafmtPlugin implements Plugin<Project> {

  static String CONFIG_NAME = "javafmt"
  static String GOOGLE_JAVA_FORMAT = "com.google.googlejavaformat:google-java-format:1.3"
  static Map<Project, Pattern> regexCache = [:]

  static boolean isNotBuildFile(Project project, String path) {
    // TODO: do via excludes.
    def pattern = regexCache[project]
    if (!pattern) {
      pattern = regexCache[project] = ~$/${project.buildDir}[\\/].*\.java/$
    }
    !(path ==~ pattern)
  }

  @Override void apply(Project project) {
    project.extensions.create('javafmt', AndroidJavaFmtExtension)

    def config = project.configurations.create(CONFIG_NAME)
    config.defaultDependencies { dependencies ->
      dependencies.add(project.dependencies.create(GOOGLE_JAVA_FORMAT))
    }

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
      // TODO: Consolidate common code.

      def fmt = project.tasks.create "fmt${variant.name.capitalize()}", JavaFmtTask
      fmt.dependsOn variant.javaCompile
      fmt.source variant.javaCompile.source
      project.tasks.getByName("assemble").dependsOn fmt
      fmtTasks.add fmt

      def checkFmt = project.tasks.create "checkFmt${variant.name.capitalize()}", CheckFmtTask
      checkFmt.dependsOn variant.javaCompile
      checkFmt.source variant.javaCompile.source
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
