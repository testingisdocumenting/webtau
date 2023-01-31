/*
 * Copyright 2023 webtau maintainers
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

package com.example.kotlin.http

import org.testingisdocumenting.webtau.data.Data
import org.testingisdocumenting.webtau.http.Http
import org.testingisdocumenting.webtau.http.datanode.DataNode
import org.testingisdocumenting.webtau.http.validation.HeaderDataNode
import org.testingisdocumenting.webtau.http.validation.HttpResponseValidator
import org.testingisdocumenting.webtau.http.validation.HttpResponseValidatorWithReturn

val http = HttpKotlin()

fun interface KotlinValidator {
    fun validate()
}

fun interface KotlinValidatorWithReturn {
    fun validateAndReturn(): Any
}

fun interface HttpKotlinResponseValidator<R, RR> {
    fun validate(header: HeaderDataNode, body: DataNode<R>): RR
}

//fun client(validator: KotlinValidator) {
//    println("validator NO return")
//    validator.validate()
//}

fun <E> client(validatorAndReturn: KotlinValidatorWithReturn<E>): E {
    println("validator WITH return")
    return validatorAndReturn.validateAndReturn()
}

fun test() {
//    val fromValidator = client {
//        100
//    }
//
//    val fromValidatorForced = client(KotlinValidatorWithReturn {
//        100
//    })
//
//    client {
//    }
}


//fun interface KotlinValidator {
//    fun validate(header: HeaderDataNode, body: DataNode)
//}
//
//fun interface KotlinValidatorWithReturn {
//    fun validateAndReturn(header: HeaderDataNode, body: DataNode): Any
//}

class HttpKotlin {
    fun <R> post(url: String, payload: Map<String, Any>, validator: HttpKotlinResponseValidator<R>): R {
        println("kotlin post return")
        return post(url, payload, validator)
    }

//    fun post(url: String, payload: Map<String, Any>, validator: KotlinValidator) {
//        println("kotlin post")
//        post(url, payload, validator)
//    }

//    fun <R> post(url: String, payload: Map<String, Any>, validator: (header: HeaderDataNode, body: DataNode) -> Any): R {
//        println("kotlin post return")
//        return post(url, payload, HttpResponseValidatorWithReturn(validator))
//    }

//    fun post(url: String, payload: Map<String, Any>, validator: (header: HeaderDataNode, body: DataNode) -> Unit) {
//        println("kotlin post")
//        post(url, payload, HttpResponseValidator(validator))
//    }
}

