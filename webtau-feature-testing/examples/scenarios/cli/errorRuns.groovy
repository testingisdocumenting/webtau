package scenarios.cli

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("match error") {
    cli.run('scripts/simple') {
        output.should contain('versian:')
    }
}

scenario("run error") {
    cli.run('scripts/simplo') {
        output.should contain('version:')
    }
}
