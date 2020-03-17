package scenarios.cli

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("simple cli run") {
    cli.run('scripts/simple') {
        output.should contain('version:')
    }
}
