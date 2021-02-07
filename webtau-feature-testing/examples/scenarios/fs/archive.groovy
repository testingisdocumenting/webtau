package scenarios.fs

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario('unzip') {
    def dir = fs.tempDir('for-unzip')
    fs.unzip('data/data.zip', dir)
}
