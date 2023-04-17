# Autocompletion Dependency

If you use standalone runner outside of Maven/Gradle, your IDE won't know about packages and classes to make auto-completion work.

To mitigate that, manually register `webtau-all.jar` as a global library. Jar is located in the `lib` directory next to the `webtau` executable.

# Intellij IDEA

Follow [documentation](https://www.jetbrains.com/help/idea/libraries-and-global-libraries.html) to register `webtau-all.jar` as a global library.
