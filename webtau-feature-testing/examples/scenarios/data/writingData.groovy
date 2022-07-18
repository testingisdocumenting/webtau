package scenarios.data

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario('csv table data') {
    // list-data
    def list = [
        ['colA': 1, 'colB': 'R1'],
        ['colA': 2, 'colB': 'R2']
    ]
    // list-data

    def path = data.csv.write('generated/from-list-maps.csv', list)
    path.should == cfg.workingDir.resolve('generated/from-list-maps.csv').toAbsolutePath()

    fs.textContent(path).should == 'colA,colB\r\n' +
            '1,R1\r\n' +
            '2,R2\r\n'
}
