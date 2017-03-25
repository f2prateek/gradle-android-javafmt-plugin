package com.f2prateek.javafmt

import com.google.googlejavaformat.java.JavaFormatterOptions;

class AndroidJavaFmtExtension {
  Style style = Style.GOOGLE

  enum Style {
    AOSP(JavaFormatterOptions.Style.AOSP),
    GOOGLE(JavaFormatterOptions.Style.GOOGLE);

    def value;

    Style(JavaFormatterOptions.Style value) {
      this.value = value;
    }
  }
}
