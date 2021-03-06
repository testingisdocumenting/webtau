package scenarios.data

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario('csv table data') {
    def table = data.csv.table('data/table.csv')
    table.row(0).B.should == '2'
    table.row(0).B.class.canonicalName.should == 'java.lang.String'
}

scenario('csv table data using path') {
    def table = data.csv.table(cfg.workingDir.resolve('data/table.csv'))
    table.row(0).B.should == '2'
    table.row(0).B.class.canonicalName.should == 'java.lang.String'
}

scenario('csv table data auto converted') {
    def table = data.csv.tableAutoConverted('data/table.csv')
    table.row(0).B.should == 2
    table.row(0).B.class.canonicalName.should == 'java.lang.Long'
}

scenario('csv list of maps data') {
    def list = data.csv.listOfMaps('data/table.csv')
    list.get(0).B.should == '2'
    list.get(0).B.class.canonicalName.should == 'java.lang.String'
}

scenario('csv list of maps data auto converted') {
    def list = data.csv.listOfMapsAutoConverted('data/table.csv')
    list.get(0).B.should == 2
    list.get(0).B.class.canonicalName.should == 'java.lang.Long'
}

scenario('csv list of maps with header data') {
    def list = data.csv.listOfMaps(['C1', 'C2', 'C3'], 'data/table-no-header.csv')
    list.get(0).C2.should == '2'
    list.get(0).C2.class.canonicalName.should == 'java.lang.String'
}

scenario('csv list of maps data with header auto converted') {
    def list = data.csv.listOfMapsAutoConverted(['C1', 'C2', 'C3'],'data/table-no-header.csv')
    list.get(0).C2.should == 2
    list.get(0).C2.class.canonicalName.should == 'java.lang.Long'
}

scenario('json list') {
    def list = data.json.list('data/flat-list.json')
    list[0].name.should == 'hello'
    list[1].payload.info.should == ~/id2 payload/
}

scenario('json map') {
    def map = data.json.map('data/root-map.json')
    map.payload.info.should == "additional id1 payload"
}

scenario('fail to read csv') {
    data.csv.table('data/broken-table.csv')
}

scenario('fail to read json') {
    data.json.map('data/broken-map.json')
}