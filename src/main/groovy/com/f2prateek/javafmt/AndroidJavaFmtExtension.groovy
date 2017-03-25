package com.f2prateek.javafmt

class AndroidJavaFmtExtension {
  Style style = Style.GOOGLE

  enum Style {
    AOSP("AOSP"),
    GOOGLE("GOOGLE");

    def value;

    Style(def value) {
      this.value = value;
    }
  }
}
