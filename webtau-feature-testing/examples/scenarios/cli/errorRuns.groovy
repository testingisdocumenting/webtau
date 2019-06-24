package scenarios.cli

import static com.twosigma.webtau.WebTauGroovyDsl.*

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
