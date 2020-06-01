package scenarios.concept

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario('simple scenario') {
    step("dummy action", {})
}

scenario('another scenario') {
    metadata([owner: 'team A'])
    step("dummy action", {})
}

