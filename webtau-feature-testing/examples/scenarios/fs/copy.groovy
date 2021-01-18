package scenarios.fs

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario('copy file') {
    def dir = fs.tempDir('fs-copy')
    fs.copy('data/message.txt', dir)
    fs.textContent(dir.resolve('message.txt')).should == 'message inside file'
}