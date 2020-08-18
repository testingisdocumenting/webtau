package scenarios.graphql

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

def listAllQuery = '''
{
    allTasks(uncompletedOnly: false) {
        id
        description
    }
}
'''

def taskByIdQuery = '''
query taskById($id: ID!) {
    taskById(id: $id) {
        id
        description
        completed
    }
}
'''

def completeMutation = '''
mutation complete($id: ID!) {
    complete(id: $id)
}
'''

scenario("list all tasks") {
    graphql.execute(listAllQuery) { // Execute a simple query with no variables
        errors.should == null // Validate there were no errors
        allTasks.id.should == ["a", "b", "c"] // Access response data via a shortcut allowing ommitting of `body.data`
        body.data.allTasks.id.should == ["a", "b", "c"]
    }
}

scenario("complete a task") {
    graphql.execute(completeMutation, [id: "a"]) { // Execute a mutation with a variables map
        errors.should == null
        complete.should == true
    }

    graphql.execute(taskByIdQuery, [id: "a"]) {
        errors.should == null
        taskById.id.should == "a"
        taskById.completed.should == true
    }
}
