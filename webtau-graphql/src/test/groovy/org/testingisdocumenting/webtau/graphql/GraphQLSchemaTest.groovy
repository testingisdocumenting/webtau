package org.testingisdocumenting.webtau.graphql

import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.webtau.http.json.JsonRequestBody
import org.testingisdocumenting.webtau.utils.ResourceUtils

import java.nio.file.Paths

class GraphQLSchemaTest {
    private GraphQLSchema schema

    @Before
    void setUp() {
        def schemaUrl = Paths.get(ResourceUtils.resourceUrl('test-schema.graphql').toURI()).toString()
        schema = new GraphQLSchema(schemaUrl)
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
        Set<GraphQLOperation> operations = schema.findOperations(new JsonRequestBody(payload))
        operations.should == [
            new GraphQLOperation("allTasks", GraphQLOperationType.QUERY)
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
        Set<GraphQLOperation> operations = schema.findOperations(new JsonRequestBody(payload))
        operations.should == [
            new GraphQLOperation("allTasks", GraphQLOperationType.QUERY)
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
        Set<GraphQLOperation> operations = schema.findOperations(new JsonRequestBody(payload))
        operations.should == [
            new GraphQLOperation("allTasks", GraphQLOperationType.QUERY)
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
        Set<GraphQLOperation> operations = schema.findOperations(new JsonRequestBody(payload))
        operations.should == [
            new GraphQLOperation("allTasks", GraphQLOperationType.QUERY),
            new GraphQLOperation("taskById", GraphQLOperationType.QUERY)
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
        Set<GraphQLOperation> operations = schema.findOperations(new JsonRequestBody(payload))
        // Multiple named operations without specifying an operation name in request are not valid
        operations.should == []
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
        Set<GraphQLOperation> operations = schema.findOperations(new JsonRequestBody(payload))
        operations.should == []
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
        Set<GraphQLOperation> operations = schema.findOperations(new JsonRequestBody(payload))
        operations.should == [
            new GraphQLOperation("allTasks", GraphQLOperationType.QUERY)
        ]
    }
}
