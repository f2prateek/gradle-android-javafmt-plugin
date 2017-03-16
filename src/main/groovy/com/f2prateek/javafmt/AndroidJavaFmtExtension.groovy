package com.f2prateek.javafmt

class AndroidJavaFmtExtension {
  Style style = Style.GOOGLE

  // Should match the enum `JavaFormatterOptions.Style`.
  enum Style {
    AOSP,
    GOOGLE
  }
}
