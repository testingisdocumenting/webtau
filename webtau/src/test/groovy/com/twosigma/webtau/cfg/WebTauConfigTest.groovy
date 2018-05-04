package com.twosigma.webtau.cfg

import org.junit.Test

class WebTauConfigTest {
    @Test
    void "inits config values from env vars and overrides them from system properties"() {
        System.setProperty('url', 'test-base-url')
        WebTauConfig cfg = new WebTauConfig()
        cfg.getBaseUrl().should == 'test-base-url'
    }

    @Test
    void "allows to manually override base url"() {
        System.setProperty('url', 'original-base-url')

        WebTauConfig cfg = new WebTauConfig()
        cfg.setBaseUrl('new-url')
        
        cfg.getBaseUrl().should == "new-url"
        cfg.baseUrlConfigValue.getSources().should == ["manual", "system property"]
    }

    @Test
    void "automatically registers user defined config values"() {
        System.setProperty('userDefined', 'user-1')
        WebTauConfig cfg = new WebTauConfig()

        cfg.get('userDefined').should == 'user-1'
    }
}
