package scenarios.concept

import static com.twosigma.webtau.WebTauGroovyDsl.*

data.csv('examples/use-cases.csv').each { row ->
    scenario("use case ${row.title}") {
        println row.input
        println row.output
    }
}