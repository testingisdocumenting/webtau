package scenarios.concept

import static com.twosigma.webtau.WebTauGroovyDsl.*

scenario('step one') {
    http.post('/reset') {
        // ...
    }
}

sscenario('step two') {
    // test that you want to focus on
}

singleScenario('step three') {
    // test that you want to focus on
}

scenario('step four') {
    http.put('/extra') {
        // ...
    }
}
