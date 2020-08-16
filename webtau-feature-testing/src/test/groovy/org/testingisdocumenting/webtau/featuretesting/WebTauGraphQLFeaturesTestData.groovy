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

package org.testingisdocumenting.webtau.featuretesting

import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeDefinitionRegistry
import graphql.schema.idl.TypeRuntimeWiring
import org.testingisdocumenting.webtau.utils.FileUtils

import java.nio.file.Paths

class WebTauGraphQLFeaturesTestData {
    static GraphQLSchema getSchema() {
        String sdl = FileUtils.fileTextContent(Paths.get("examples","scenarios", "graphql", "schema.graphql"))
        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(sdl)

        RuntimeWiring.Builder runtimeWiringBuilder = RuntimeWiring.newRuntimeWiring()
        setupTestData(runtimeWiringBuilder)
        RuntimeWiring runtimeWiring = runtimeWiringBuilder.build()

        SchemaGenerator schemaGenerator = new SchemaGenerator()
        return schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring)
    }

    private static void setupTestData(RuntimeWiring.Builder builder) {
        tasks.add(new Task("a", "first task", false))
        tasks.add(new Task("b", "second task", false))
        tasks.add(new Task("c", "already done", true))

        TypeRuntimeWiring.Builder queries = TypeRuntimeWiring.newTypeWiring("Query")
        queries.dataFetcher("allTasks") { e -> allTasks(e.getArgument("uncompletedOnly"))}
        queries.dataFetcher("taskById") { e -> taskById(e.getArgument("id")) }

        TypeRuntimeWiring.Builder mutations = TypeRuntimeWiring.newTypeWiring("Mutation")
        mutations.dataFetcher("complete") {e -> setCompleted(e.getArgument("id"), true) }
        mutations.dataFetcher("uncomplete") {e -> setCompleted(e.getArgument("id"), false) }

        builder.type(queries).type(mutations)
    }

    private static Boolean setCompleted(String id, Boolean completed) {
        Task task = taskById(id)
        if (task == null) {
            return false
        } else {
            task.completed = completed
            return true
        }
    }

    private static Task taskById(String id) {
        tasks.find { task -> task.id == id }
    }

    private static List<Task> allTasks(boolean uncompletedOnly) {
        return tasks.findAll {task -> !uncompletedOnly || !task.completed }
    }

    private static List<Task> tasks = new ArrayList<>()

    private static class Task {
        private final String id
        private final String description
        private boolean completed

        Task(String id, String description, boolean completed) {
            this.id = id
            this.description = description
            this.completed = completed
        }
    }
}
