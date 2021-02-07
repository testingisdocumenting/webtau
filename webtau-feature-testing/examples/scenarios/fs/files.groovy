package scenarios.fs

import java.nio.file.Paths

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario('create and delete file') {
    def path = Paths.get('test-file.txt')
    fs.writeText(path, 'hello world')

    fs.textContent(path).should == 'hello world'

    // relative path by default should be expanded against working dir
    cfg.workingDir.toAbsolutePath().toString().shouldNot == Paths.get("").toAbsolutePath().toString()
    fs.textContent(cfg.workingDir.resolve('test-file.txt').toAbsolutePath()).should == 'hello world'

    fs.exists(path).should == true
    fs.delete('test-file.txt')
    fs.exists(path).should == false
}

scenario('create file using string path') {
    def path = fs.writeText('my-test-file.txt', 'hello world')
    cfg.workingDir.resolve('my-test-file.txt').toAbsolutePath().toString().should == path.toString()

    fs.delete(path)
}

scenario('read file using string path') {
    def path = fs.writeText('my-test-file.txt', 'hello world\nid=15')
    // assert-file
    fs.textContent('my-test-file.txt').should == 'hello world\nid=15'
    // assert-file

    // wait-for-id
    def fileTextContent = fs.textContent('my-test-file.txt')
    fileTextContent.waitTo contain('id=15')
    // wait-for-id

    // actual-file-content
    def actualFileContent = fileTextContent.data
    // actual-file-content
    actualFileContent.should == 'hello world\nid=15'

    // extract-id
    def id = fileTextContent.extractByRegexp("id=(\\d+)")
    http.get("/customers/${id}") {
        // ...
        statusCode.should == 404
    }
    // extract-id

    fs.delete(path)
}