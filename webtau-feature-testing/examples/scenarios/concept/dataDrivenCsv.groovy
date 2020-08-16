package scenarios.concept

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

data.csv.table('use-cases.csv').each { row ->
    scenario("use case ${row.title}") {
        println row.input
        println row.output
    }
}