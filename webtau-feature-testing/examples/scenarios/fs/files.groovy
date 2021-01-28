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
    def path = fs.writeText('another-test-file.txt', 'hello world')
    cfg.workingDir.resolve('another-test-file.txt').toAbsolutePath().toString().should == path.toString()

    fs.delete(path)
}