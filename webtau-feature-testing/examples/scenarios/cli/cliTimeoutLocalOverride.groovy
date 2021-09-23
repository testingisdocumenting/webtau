package scenarios.cli

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("sleep timeout override") {
    cli.run("scripts/sleeps", cli.timeout(300))
}