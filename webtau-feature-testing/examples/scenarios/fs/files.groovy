package scenarios.fs

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario('create and delete file') {
    def path = cfg.workingDir.resolve('test-file.txt')
    fs.writeText(path, 'hello world')
    fs.textContent(path).should == 'hello world'

    fs.exists(path).should == true
    fs.delete('test-file.txt')
    fs.exists(path).should == false
}
