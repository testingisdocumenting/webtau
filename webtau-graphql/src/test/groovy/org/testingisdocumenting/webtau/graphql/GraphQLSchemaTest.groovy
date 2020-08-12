package org.testingisdocumenting.webtau.graphql

import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.webtau.http.json.JsonRequestBody
import org.testingisdocumenting.webtau.http.text.TextRequestBody

import static org.testingisdocumenting.webtau.graphql.TestUtils.getDeclaredOperations

class GraphQLSchemaTest {
    private GraphQLSchema schema

    @Before
    void setUp() {
        schema = new GraphQLSchema(declaredOperations)
    }

    @Test
    void "short hand syntax for queries"() {
        def query = '''
{
    allTasks(uncompletedOnly: false) {
        id
        description
    }
}
'''
        def payload = [
            query: query
        ]
        Set<GraphQLQuery> queries = schema.findQueries(new JsonRequestBody(payload))
        queries.should == [
            new GraphQLQuery("allTasks", GraphQLQueryType.QUERY)
        ]
    }

    @Test
    void "explicit type query"() {
        def query = '''
query {
    allTasks(uncompletedOnly: false) {
        id
        description
    }
}
'''
        def payload = [
            query: query
        ]
        Set<GraphQLQuery> queries = schema.findQueries(new JsonRequestBody(payload))
        queries.should == [
            new GraphQLQuery("allTasks", GraphQLQueryType.QUERY)
        ]
    }

    @Test
    void "named operation"() {
        def query = '''
query foobar {
    allTasks(uncompletedOnly: false) {
        id
        description
    }
}
'''
        def payload = [
            query: query
        ]
        Set<GraphQLQuery> queries = schema.findQueries(new JsonRequestBody(payload))
        queries.should == [
            new GraphQLQuery("allTasks", GraphQLQueryType.QUERY)
        ]
    }

    @Test
    void "multiple queries"() {
        def query = '''
{
    allTasks(uncompletedOnly: false) {
        id
        description
    }
    taskById(id: "abc") {
        id
        description
    }
    
}
'''
        def payload = [
            query: query
        ]
        Set<GraphQLQuery> queries = schema.findQueries(new JsonRequestBody(payload))
        queries.should == [
            new GraphQLQuery("allTasks", GraphQLQueryType.QUERY),
            new GraphQLQuery("taskById", GraphQLQueryType.QUERY)
        ] as Set
    }

    @Test
    void "multiple operations without name"() {
        def query = '''
query foobar {
    allTasks(uncompletedOnly: false) {
        id
        description
    }
}
query hello {
    taskById(id: "abc") {
        id
        description
    }
    
}
'''
        def payload = [
            query: query
        ]
        Set<GraphQLQuery> queries = schema.findQueries(new JsonRequestBody(payload))
        // Multiple named operations without specifying an operation name in request are not valid
        queries.should == []
    }

    @Test
    void "multiple operations with no matching name"() {
        def query = '''
query foobar {
    allTasks(uncompletedOnly: false) {
        id
        description
    }
}
query hello {
    taskById(id: "abc") {
        id
        description
    }
    
}
'''
        def payload = [
            query: query,
            operationName: 'does not exist'
        ]
        Set<GraphQLQuery> queries = schema.findQueries(new JsonRequestBody(payload))
        queries.should == []
    }

    @Test
    void "multiple operations"() {
        def query = '''
query foobar {
    allTasks(uncompletedOnly: false) {
        id
        description
    }
}
query hello {
    taskById(id: "abc") {
        id
        description
    }
    
}
'''
        def payload = [
            query: query,
            operationName: 'foobar'
        ]
        Set<GraphQLQuery> queries = schema.findQueries(new JsonRequestBody(payload))
        queries.should == [
            new GraphQLQuery("allTasks", GraphQLQueryType.QUERY)
        ]
    }

    @Test
    void "non json bodies are ignored"() {
        schema.findQueries(TextRequestBody.withType("text", "foobar")).should == []
    }

    @Test
    void "json bodies without a query are ignored"() {
        schema.findQueries(new JsonRequestBody([foo: 'bar'])).should == []
    }

    @Test
    void "json bodies with a non-string query are ignored"() {
        schema.findQueries(new JsonRequestBody([query: 123])).should == []
    }

    @Test
    void "json bodies with invalid query are ignored"() {
        schema.findQueries(new JsonRequestBody([query: "not valid graphql"])).should == []
    }
}
