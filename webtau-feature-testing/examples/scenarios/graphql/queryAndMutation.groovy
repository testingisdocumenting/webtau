package scenarios.graphql

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

def listAllQuery = '''
query {
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
    graphql.execute(listAllQuery) {
        errors.should == null
        allTasks.id.should == ["a", "b", "c"]
    }
}

scenario("complete a task") {
    graphql.execute(completeMutation, [id: "a"]) {
        errors.should == null
        complete.should == true
    }

    graphql.execute(taskByIdQuery, [id: "a"]) {
        errors.should == null
        taskById.id.should == "a"
        taskById.completed.should == true
    }
}
