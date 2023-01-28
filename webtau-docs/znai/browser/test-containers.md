# Default WebDriver Creation

By default, WebTau creates [Selenium WebDriver](https://www.selenium.dev) by pointing to a local browser. WebTau also downloads a driver using [WebDriver Manager](https://github.com/bonigarcia/webdrivermanager).

To handle scenarios where local browsers are not an option, WebTau provides an integration with [Test Containers](https://www.testcontainers.org).

# Explicit Selenium Test Containers

To support [Test Containers](https://www.testcontainers.org) explicitly, WebTau expose `browser.setDriver` to force a specific driver to use: 

:include-java: com/example/tests/junit5/BrowserTestContainerJavaTest.java {
  title: "manually start selenium test container",
  entry: "BrowserTestContainerJavaTest",
  highlight: "setDriver"
}

# Implicit Selenium Test Container

WebTau provides an option to simplify running tests using both local and [Test Containers](https://www.testcontainers.org) based browser:

:include-file: src/test/resources/webtau.browser.testcontainers.properties {
  title: "src/test/resources/webtau.properties",
  highlight: "TestcontainersEnabled"
}

:include-java: com/example/tests/junit5/BrowserImplicitTestContainerJavaTest.java {
  title: "implicitly start selenium test container",
  entry: "BrowserImplicitTestContainerJavaTest"
}

Read [Configuration](getting-started/configuration) page to learn about how to override config values using different environments, system properties or environment variables.

# Dependency

By default, WebTau includes [Test Containers](https://www.testcontainers.org) into [Groovy Runner](groovy-standalone-runner/introduction) and into single catch-all dependency

```tabs
Groovy: 
:include-file: maven/groovy-dep.xml {title: "catch-all Groovy dependecy"}
Java: 
:include-file: maven/java-dep.xml {title: "catch-all Java dependency"}
```

If you cherry-pick WebTau dependencies, then you need to add this to your list:

:include-file: maven/browser-testcontainers-dep.xml {title: "maven dependency"}