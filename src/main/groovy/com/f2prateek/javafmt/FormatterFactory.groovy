package com.f2prateek.javafmt

import org.gradle.api.Project

class FormatterFactory {
  static formatter(Project project) {
    def config = project.configurations.getByName(AndroidJavafmtPlugin.CONFIG_NAME)
    def urls = new ArrayList<URL>()
    config.files.each { File file ->
      urls += file.toURI().toURL()
    }

    // These classes cannot be loaded directly.
    def cl = new URLClassLoader(urls as URL[]);
    def optionsClass = cl.loadClass("com.google.googlejavaformat.java.JavaFormatterOptions")
    def styleClass = cl.loadClass("com.google.googlejavaformat.java.JavaFormatterOptions\$Style")
    def formatterClass = cl.loadClass("com.google.googlejavaformat.java.Formatter")

    // def optionsBuilder = JavaFormatterOptions.builder()
    def optionsBuilder = optionsClass.getMethod("builder").invoke(null)

    def extension = project.extensions.getByType(AndroidJavaFmtExtension)
    optionsBuilder.style(Enum.valueOf(styleClass, extension.style.toString()))

    def options = optionsBuilder.build()

    // return new Formatter(options)
    return formatterClass.getConstructor(optionsClass).newInstance(options)
  }
}
