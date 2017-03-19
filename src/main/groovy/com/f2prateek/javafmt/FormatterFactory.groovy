package com.f2prateek.javafmt

import com.google.googlejavaformat.java.Formatter
import org.gradle.api.Project

class FormatterFactory {
  static formatter(Project project) {
    return new Formatter()
  }
}