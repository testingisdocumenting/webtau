package scenarios

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*
import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg
import static webtau.CliCommands.getWebtauCli

def repl = createLazyResource {
    def command = webtauCli.runInBackground("repl --workingDir=${cfg.workingDir} " +
            "--failedReportPath=webtau.repl-test-disabled-single-selection-failed.report.html " +
            "testscripts/disabledAndSingleScenarios.groovy")

    command.output.waitTo contain('disabledAndSingleScenarios.groovy')

    return command
}

scenario('select disabled and single scenarios') {
    repl.clearOutput()
    repl << "s 'disabledAndSingleScenarios.groovy'\n"

    repl.output.waitTo contain('single scenario')
    repl.output.should contain('disabled scenario')
}

scenario('run disabled scenarios') {
    repl.clearOutput()
    repl << "r 0\n"

    repl.output.waitTo contain('executing disabled scenario')
}

scenario('run single scenarios') {
    repl.clearOutput()
    repl << "r 1\n"

    repl.output.waitTo contain('executing single scenario')
}