package scenarios.cli

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("shell source and script run") {
    cli.run('bash -c "source scripts/setup.sh && scripts/post_setup.sh"') {
        output.should == "my process\n" +
                "1.23.2"
    }
}
