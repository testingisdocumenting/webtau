package scenarios.fs

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("copy file to temp dir") {
    def dir = fs.tempDir("fs-copy")
    fs.copy("data/message.txt", dir)
    fs.textContent(dir.resolve("message.txt")).should == "message inside file"
}

scenario("copy file to a dir") {
    def dir = fs.createDir("my-dir")
    fs.copy("data/message.txt", dir)
    fs.textContent(dir.resolve("message.txt")).should == "message inside file"
    fs.delete(dir)
}

scenario("copy file to a different file") {
    fs.copy("data/message.txt", "data/new-message.txt")
    fs.textContent("data/new-message.txt").should == "message inside file"
    fs.delete("data/new-message.txt")
}