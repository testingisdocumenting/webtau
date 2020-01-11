package scenarios.concept

import static com.twosigma.webtau.WebTauGroovyDsl.*

attachTestMetaValue("owner", "team A")

scenario("one") {
}

scenario("two") {
}

scenario("three") {
    attachTestMetaValue("owner", "team B")
}