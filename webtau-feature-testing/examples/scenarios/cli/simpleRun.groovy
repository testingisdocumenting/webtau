package scenarios.cli

import static com.twosigma.webtau.WebTauGroovyDsl.*

scenario("simple cli run") {
    cli.run('scripts/simple') {
        output.should contain('version:')
    }
}
