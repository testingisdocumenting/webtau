# Setting Proxy

:include-file: scenarios/rest/proxy/webtau.proxy.cfg.groovy {
    title: "webtau.cfg.groovy",
    excludeRegexp: ["package", "noProxy"],
    commentsType: "inline"
}

# Hosts to Exclude

:include-file: scenarios/rest/proxy/webtau.proxy.cfg.groovy {
    title: "webtau.cfg.groovy",
    excludeRegexp: ["package", "http[s]*Proxy"],
    commentsType: "inline"
}

