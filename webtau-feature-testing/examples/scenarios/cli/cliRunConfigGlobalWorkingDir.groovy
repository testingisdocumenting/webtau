package scenarios.cli

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("propagates global working dir") {
    cli.run('./listing' ) {
        output.should contain('listing files')
        output.should contain('sleeps')
    }
}
