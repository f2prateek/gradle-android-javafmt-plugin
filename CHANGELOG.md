Change Log
==========

Version 0.1.5 *(05 Apr 2017)*
-----------------------------

 * [New](https://github.com/f2prateek/gradle-android-javafmt-plugin/pull/16): Add ability to configure `google-java-format` version.

```
dependencies {
  javafmt "com.google.googlejavaformat:google-java-format:${version}"
}
```

 * [New](https://github.com/f2prateek/gradle-android-javafmt-plugin/pull/15): Add ability to select between `AOSP` and `GOOGLE` code styles.

```
javafmt {
  style 'AOSP'
}
```

Version 0.1.4 *(15 Mar 2017)*
-----------------------------

 * [Fix](https://github.com/f2prateek/gradle-android-javafmt-plugin/pull/13): Fix regression introduced in 0.1.3. 0.1.3 would incorrectly ignore all files, causing the fmt and checkFmt tasks to do nothing. Don't use 0.1.3 (still published but not listed here).



Version 0.1.2 *(01 Mar 2017)*
-----------------------------

 * New: Add a `checkFmt` task to verify files are formatted. This is also run as part of the `check` task.


Version 0.1.1 *(28 Feb 2017)*
----------------------------

 * [Fix](https://github.com/f2prateek/gradle-android-javafmt-plugin/pull/3): Support product flavors.


Version 0.1.0 *(27 Feb 2017)*
----------------------------

Initial Release.
