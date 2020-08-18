import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario('fetch types from schema') {
    def query = '{ __schema { types { name } } }'
    graphql.execute(query) {
        __schema.types.numberOfElements().shouldBe > 0
    }
}
