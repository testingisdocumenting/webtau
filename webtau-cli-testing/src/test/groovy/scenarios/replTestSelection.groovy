package scenarios

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*
import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg
import static webtau.CliCommands.getWebtauCli

def repl = createLazyResource {
    def command = webtauCli.runInBackground("repl --workingDir=${cfg.workingDir} " +
            "--failedReportPath=webtau.repl-test-selection-failed.report.html " +
            "testscripts/dataDownload.groovy " +
            "testscripts/downstreamValidation.groovy " +
            "testscripts/resourceCreation.groovy")

    command.output.waitTo contain('resourceCreation.groovy')

    return command
}

scenario('test listing') {
    repl.clearOutput()
    repl << "ls\n"
    repl.output.waitTo contain('resourceCreation.groovy')

    cli.doc.capture('repl-tests-listing')
}

scenario('report syntax error during test file parse') {
    repl.clearOutput()
    repl << "s 'downstreamValidation.groovy'\n"

    repl.output.waitTo contain('groovy.lang.MissingPropertyException: No such property: aaa')
    repl << "b\n"
    repl.output.waitTo contain('resourceCreation.groovy')
}

scenario('select test file by index') {
    repl.clearOutput()
    repl << "s 0\n"
    repl.output.waitTo contain('validate data')

    cli.doc.capture('repl-test-file-selection-by-index')
}

scenario('get back to files selection') {
    repl.clearOutput()
    repl << "b\n"
    repl.output.waitTo contain('resourceCreation.groovy')

    cli.doc.capture('repl-test-file-back')
}

scenario('select test file by text') {
    repl.clearOutput()
    repl << "s 'datadownload'\n"
    repl.output.waitTo contain('validate data')

    cli.doc.capture('repl-test-file-selection-by-text')
}

scenario('run scenario by index') {
    repl.clearOutput()
    repl << "r 0\n"
    repl.output.waitTo contain("cleaning...")

    cli.doc.capture('repl-test-scenario-ran-by-idx')
}

scenario('run scenario by negative index') {
    repl.clearOutput()
    repl << "r -1\n"
    repl.output.waitTo contain("validating...")

    cli.doc.capture('repl-test-scenario-ran-by-negative-idx')
}

scenario('run scenario by text') {
    repl.clearOutput()
    repl << "r 'clean'\n"
    repl.output.waitTo contain("cleaning...")

    cli.doc.capture('repl-test-scenario-ran-by-text')
}

scenario('re-run last') {
    repl.clearOutput()
    repl << "r\n"
    repl.output.waitTo contain("cleaning...")

    cli.doc.capture('repl-test-scenario-re-run')
}

scenario('run scenario by multiple indexes') {
    repl.clearOutput()
    repl << "r 0, 1\n"
    repl.output.waitTo contain("cleaning...")
    repl.output.waitTo contain("downloading...")

    cli.doc.capture('repl-test-scenario-ran-by-multiple-indexes')
}

scenario('run scenario by multiple texts') {
    repl.clearOutput()
    repl << "r 'download', 'clean'\n"
    repl.output.waitTo contain("cleaning...")
    repl.output.waitTo contain("downloading...")

    // enforce order
    repl.output.should == ~/(?s).*downloading.*cleaning/

    cli.doc.capture('repl-test-scenario-ran-by-multiple-text-reverse')
}

scenario('run scenario by mixed range') {
    repl.clearOutput()
    repl << "r 0:'validate'\n"
    repl.output.waitTo contain("cleaning...")
    repl.output.waitTo contain("downloading...")
    repl.output.waitTo contain("validating...")

    cli.doc.capture('repl-test-scenario-ran-by-mixed-range')
}

scenario('run scenario by mixed range reverse') {
    repl.clearOutput()
    repl << "r 'validate':0\n"
    repl.output.waitTo contain("cleaning...")
    repl.output.waitTo contain("downloading...")
    repl.output.waitTo contain("validating...")

    // enforce order
    repl.output.should == ~/(?s).*validating.*downloading.*cleaning/

    cli.doc.capture('repl-test-scenario-ran-by-mixed-range-reverse')
}

scenario('display selected scenarios for re-run') {
    repl.clearOutput()
    repl << "s\n"

    repl.output.waitTo == ~/(?s).*validate.*download.*clean/

    cli.doc.capture('repl-test-scenario-selected')
}

scenario('select scenarios by indexes') {
    repl.clearOutput()
    repl << "s 0,2\n"

    repl.output.waitTo == ~/(?s).*clean.*validate/

    cli.doc.capture('repl-test-scenario-select-by-indexes')
}