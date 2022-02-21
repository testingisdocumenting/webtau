package scenarios.cli.parallel

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("run one") {
    cli.run('scripts/multiple-lines-output') {
        output.should contain("hello")
        output.should contain("14")
    }
}

scenario("run two") {
    cli.run('scripts/multiple-lines-output') {
        output.should contain("24")
    }
}
