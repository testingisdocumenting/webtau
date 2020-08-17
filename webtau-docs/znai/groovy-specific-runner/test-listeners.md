# Test Listener Interface

To define a custom test listener you need to implement the `TestListener` interface

:include-file: org/testingisdocumenting/webtau/reporter/TestListener.java {title: "TestListener.java"}

# Config

Once you have an implementation, use the `testListeners` key in your config file to use it

:include-file: scenarios/concept/metaDataTestListenerCfg.groovy {title: "webtau.cfg.groovy"}

:include-file: listeners/MetadataValidationTestListener.groovy {title: "listeners/MetadataValidationTestListener.groovy"}