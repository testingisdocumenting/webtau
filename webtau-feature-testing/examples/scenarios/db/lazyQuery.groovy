package scenarios.db

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

def query = db.query("select * from WRONG_TABLE")

scenario('no db access should not initialize data source') {
}
