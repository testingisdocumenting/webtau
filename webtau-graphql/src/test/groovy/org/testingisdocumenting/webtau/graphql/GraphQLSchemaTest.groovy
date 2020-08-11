package org.testingisdocumenting.webtau.graphql

import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.webtau.http.json.JsonRequestBody
import org.testingisdocumenting.webtau.utils.ResourceUtils

class GraphQLSchemaTest {
    private GraphQLSchema schema

    @Before
    void setUp() {
        def schemaUrl = ResourceUtils.resourceUrl('test-schema.graphql')
        schema = new GraphQLSchema(schemaUrl.file)
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
}
