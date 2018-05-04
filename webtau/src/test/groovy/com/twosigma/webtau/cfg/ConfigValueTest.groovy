package com.twosigma.webtau.cfg

import org.junit.Test

class ConfigValueTest {
    @Test
    void "takes the most recently set value"() {
        def configValue = ConfigValue.declare("vk", "description", { -> "dv" })
        configValue.set("config-file", "file value")
        configValue.set("command-line", "command line")

        configValue.getAsString().should == "command line"
    }

    @Test
    void "converts string value true to boolean true"() {
        def configValue = ConfigValue.declare("headless", "headless", { -> false })
        configValue.getAsBoolean().should == false

        configValue.set("command-line", "True")
        configValue.getAsBoolean().should == true

        configValue.set("another", "true")
        configValue.getAsBoolean().should == true
    }

    @Test
    void "initialize its value by looking through provided map of values using original and prefixed uppercase key"() {
        def configValue = ConfigValue.declare("path", "path", null)
        configValue.accept("env vars", [dummy: 1, WEBTAU_PATH: "path1"])
        configValue.getAsString().should == "path1"

        configValue.accept("command line", [dummy: 1, path: "path2"])
        configValue.getAsString().should == "path2"
    }
}
