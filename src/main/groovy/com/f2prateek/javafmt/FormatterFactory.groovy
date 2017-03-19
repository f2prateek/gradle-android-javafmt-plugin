package com.f2prateek.javafmt

import com.google.googlejavaformat.java.Formatter
import com.google.googlejavaformat.java.JavaFormatterOptions
import org.gradle.api.Project

class FormatterFactory {
  static formatter(Project project) {
    def extension = project.extensions.getByType(AndroidJavaFmtExtension)
    def options =  JavaFormatterOptions.builder().style(extension.style.value).build()
    return new Formatter(options)
  }
}