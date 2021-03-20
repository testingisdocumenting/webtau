package scenarios.cli

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("sleep timeout") {
    cli.run("scripts/sleeps")
}