package scenarios.cli

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("working dir") {
    cli.run('./listing', cli.workingDir('scripts')) {
        output.should contain('listing files')
        output.should contain('sleeps')
    }
}

scenario("environment var") {
    cli.run('scripts/hello-env-var', cli.env([MY_VAR: 'webtau'])) {
        output.should == 'hello webtau'
    }
}

scenario("env var and working dir") {
    cli.run('./hello-env-var',
            cli.workingDir('scripts').env([MY_VAR: 'webtau'])) {
        output.should == 'hello webtau'
    }
}
