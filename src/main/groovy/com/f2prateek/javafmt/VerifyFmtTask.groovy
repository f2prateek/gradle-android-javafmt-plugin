package com.f2prateek.javafmt

import com.google.common.base.Charsets
import com.google.common.io.Files
import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.google.googlejavaformat.java.Formatter
import difflib.DiffUtils
import org.gradle.api.GradleException
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskExecutionException
import org.gradle.api.tasks.VerificationTask

import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors

class VerifyFmtTask extends SourceTask implements VerificationTask {
  @TaskAction
  def fmt() {
    def formatter = new Formatter();
    def executor = Executors.newFixedThreadPool(2,
            new ThreadFactoryBuilder().setNameFormat("verify-javafmt-pool-%d").build())

    def tasks = getSource().collect { file ->
      return {
        def source = Files.asCharSource(file, Charsets.UTF_8).read()
        def formatted = formatter.formatSource(source)

        if (!source.equals(formatted)) {
          def patch = DiffUtils.diff(source.readLines(), formatted.readLines());
          def message = patch.deltas.collect { delta ->
            def original = delta.getOriginal()
            def originalLines = original.lines.join("\n")
            def revised = delta.getRevised()
            def revisedLines = revised.lines.join("\n")
            return "\nexpected at line $revised.position:\n$revisedLines\ngot at line $original.position:\n$originalLines\n"
          }.join('\n')
          if (getIgnoreFailures()) {
            logger.warn(message)
          } else {
            logger.error(message)
            throw new VerifyFmtException()
          }
        }

        return file
      }
    }

    List<Throwable> errors = []
    def results = executor.invokeAll(tasks)
    results.each { result ->
      try {
        result.get()
      } catch (ExecutionException e) {
        if (e.getCause() instanceof VerifyFmtException) {
          errors.add e.getCause()
        } else {
          throw e
        }
      }
    }

    if (!errors.isEmpty()) {
      throw new TaskExecutionException(this, errors.get(0))
    }
  }

  /**
   * Whether or not this task will ignore failures and continue running the build.
   */
  boolean ignoreFailures

  public class VerifyFmtException extends GradleException {
    public VerifyFmtException() {
      super("Code style violations found. Run `./gradlew fmt` to format.");
    }
  }
}
