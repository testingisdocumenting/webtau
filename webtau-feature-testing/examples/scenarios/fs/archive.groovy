package scenarios.fs

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario('zip') {
    // zip
    def dest = fs.tempDir("for-zip").resolve("content.zip")
    fs.zip('data/staticcontent', dest)
    // zip

    def unzipped = fs.tempDir("zip-test")
    fs.unzip(dest, unzipped)

    fs.textContent(unzipped.resolve("hello.html")).should == "<body>\n" +
            "<p>hello</p>\n" +
            "</body>"
}

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
