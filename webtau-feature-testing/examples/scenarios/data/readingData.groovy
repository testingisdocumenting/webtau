package scenarios.data

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario('csv table data') {
    data.csv.table('data/table.csv')
}
