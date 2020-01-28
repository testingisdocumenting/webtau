# Test Listener Interface

To define a custom test listener you need to implement `TestListener` interface

:include-file: com/twosigma/webtau/reporter/TestListener.java {title: "TestListener.java"}

# Config

Once you have an implementation, use `testListeners` key in config file to use it

:include-file: scenarios/concept/testListener.cfg {title: "webtau.cfg"}

:include-file: listeners/MetaDataValidationTestListener.groovy {title: "listeners/MetaDataValidationTestListener.groovy"}