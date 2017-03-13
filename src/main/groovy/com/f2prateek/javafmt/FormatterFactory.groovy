package com.f2prateek.javafmt

import static com.google.googlejavaformat.java.JavaFormatterOptions.Style.AOSP;

import com.google.googlejavaformat.java.Formatter
import com.google.googlejavaformat.java.JavaFormatterOptions
import org.gradle.api.Project

class FormatterFactory {
  static formatter(Project project) {
    def extension = project.extensions.getByType(AndroidJavaFmtExtension)
    if (extension.aospStyle) {
      return new Formatter(JavaFormatterOptions.builder().style(AOSP).build())
    }
    return new Formatter()
  }
}