package scenarios.cli

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("set working dir") {
    cli.run('./listing', cli.workingDir('../scripts')) {
        output.should contain('listing files')
        output.should contain('sleeps')
    }
}
