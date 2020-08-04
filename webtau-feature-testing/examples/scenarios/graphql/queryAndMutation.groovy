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
    http.post("/graphql", [query: listAllQuery]) {
        statusCode.should == 200
        errors.should == null
        body.data.allTasks.id.should == ["a", "b", "c"]
    }
}

scenario("complete a task") {
    def completionRequest = [
        query: completeMutation,
        variables: [
            id: "a"
        ]
    ]
    http.post("/graphql", completionRequest) {
        statusCode.should == 200
        errors.should == null
        body.data.complete.should == true
    }

    def getTaskRequest = [
        query: taskByIdQuery,
        variables: [
            id: "a"
        ]
    ]
    http.post("/graphql", getTaskRequest) {
        statusCode.should == 200
        errors.should == null
        body.data.taskById.id.should == "a"
        body.data.taskById.completed.should == true
    }
}
