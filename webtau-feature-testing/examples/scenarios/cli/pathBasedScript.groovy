package scenarios.cli

import static com.twosigma.webtau.WebTauGroovyDsl.*

scenario("cli run using path") {
    cli.run('path-simple') {
        output.should contain('version:')
    }
}

scenario("cli run with args") {
    cli.run('path-simple foo "hello world"') {
        output.should contain('version:')
        output.should contain('foo')
        output.should contain('hello world')
    }
}
