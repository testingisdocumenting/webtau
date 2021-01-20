package scenarios.fs

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario('create and delete directory') {
    def dir = fs.createDir('fs-new-dir')
    fs.copy('data/message.txt', dir)

    fs.textContent(dir.resolve('message.txt')).should == 'message inside file'

    fs.deleteDir('fs-new-dir')

    fs.exists(dir).should == false
    fs.exists(dir.resolve('message.txt')).should == false
}