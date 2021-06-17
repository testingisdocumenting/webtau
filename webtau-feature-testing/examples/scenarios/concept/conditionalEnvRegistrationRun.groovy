package scenarios.concept

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

onlyForEnv('experimental') {
    scenario('this scenario will only be executed in "experimental" env') {
        http.get('/new-endpoint') {
            price.shouldBe > 0
        }
    }
}

skipForEnv('experimental') {
    scenario('this scenario will not be executed in "experimental" env') {
        http.get('/new-endpoint') {
            price.shouldBe > 0
        }
    }
}
