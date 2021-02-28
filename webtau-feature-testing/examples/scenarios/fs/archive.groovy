package scenarios.fs

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario('unzip') {
    // unzip
    def dir = fs.tempDir('for-unzip')
    fs.unzip('data/data.zip', dir)
    // unzip

    fs.textContent(dir.resolve('message.txt')).should == 'message inside file'
}

scenario('untar') {
    // untar
    def dir = fs.tempDir('for-untar')
    fs.untar('data/data.tar', dir)
    // untar

    fs.textContent(dir.resolve('message.txt')).should == 'message inside file'
}
