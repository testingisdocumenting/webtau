package scenarios.concept

import static com.twosigma.webtau.WebTauGroovyDsl.*

onlyForEnv('experimental') {
    scenario('this scenario will only be executed in "experimental" env') {
        http.get('/new-endpoint') {
            price.shouldBe > 0
        }
    }
}