/*
 * Copyright 2020 webtau maintainers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    graphql.execute(listAllQuery) {
        errors.should == null
        allTasks.id.should == ["a", "b", "c"]
        body.data.allTasks.id.should == ["a", "b", "c"]
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
