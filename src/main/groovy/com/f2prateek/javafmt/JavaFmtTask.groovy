package com.f2prateek.javafmt

import com.google.common.base.Charsets
import com.google.common.io.Files
import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.google.googlejavaformat.java.Formatter
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction

import java.util.concurrent.Executors

class JavaFmtTask extends SourceTask {
  @TaskAction
  def fmt() {
    def formatter = new Formatter();
    def executor = Executors.newFixedThreadPool(2,
            new ThreadFactoryBuilder().setNameFormat("javafmt-pool-%d").build())

    def tasks = getSource().collect { file ->
      return {
        def source = Files.asCharSource(file, Charsets.UTF_8).read()
        def formatted = formatter.formatSource(source)
        Files.write(formatted, file, Charsets.UTF_8)
        return file
      }
    }

    def results = executor.invokeAll(tasks)
    results.each { result ->
      result.get()
    }
  }
}
