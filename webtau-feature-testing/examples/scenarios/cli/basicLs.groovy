package scenarios.cli

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("example of ls run") {
    cli.run('ls') {
        output.should contain('webtau')
    }
}
