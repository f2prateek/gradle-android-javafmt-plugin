package com.f2prateek.javafmt

import org.gradle.api.Project

class FormatterFactory {
  static formatter(Project project) {
    def config = project.configurations.getByName(AndroidJavafmtPlugin.CONFIG_NAME)
    def urls = new ArrayList<URL>()
    config.files.each { File file ->
      urls += file.toURI().toURL()
    }
    def cl = new URLClassLoader(urls.toArray(new URL[0]));
    def optionsClass = cl.loadClass("com.google.googlejavaformat.java.JavaFormatterOptions")
    def styleClass = cl.loadClass("com.google.googlejavaformat.java.JavaFormatterOptions\$Style")
    def formatterClass = cl.loadClass("com.google.googlejavaformat.java.Formatter")

    def extension = project.extensions.getByType(AndroidJavaFmtExtension)
    def optionsBuilder = optionsClass.getMethod("builder").invoke(null)
    optionsBuilder.style(Enum.valueOf(styleClass, extension.style.value))
    return formatterClass.getConstructor(optionsClass).newInstance(optionsBuilder.build())
  }
}
