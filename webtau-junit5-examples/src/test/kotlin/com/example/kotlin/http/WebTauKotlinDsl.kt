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

import org.testingisdocumenting.webtau.http.Http
import org.testingisdocumenting.webtau.http.datanode.DataNode
import org.testingisdocumenting.webtau.http.validation.HeaderDataNode
import org.testingisdocumenting.webtau.http.validation.HttpResponseValidator
import org.testingisdocumenting.webtau.http.validation.HttpResponseValidatorWithReturn

//private val http: Http = Http.http

//fun <R> Http.post(url: String, payload: Map<String, Any>, validator: (header: HeaderDataNode, body: DataNode) -> Any): R {
//    println("kotlin post return")
//    return Http.http.post(url, payload, HttpResponseValidatorWithReturn(validator))
//}
//
//fun Http.post(url: String, payload: Map<String, Any>, validator: (header: HeaderDataNode, body: DataNode) -> Unit) {
//    println("kotlin post")
//    Http.http.post(url, payload, HttpResponseValidator(validator))
//}
//
//fun Http.todo() {
//    println("todo extension external")
//}


//class WebTauKotlinDsl : WebTauDsl() {
//
//    fun Http.todoInternal() {
//        println("todo extension")
//    }
//
//    fun <R> Http.post(url: String, payload: Map<String, Any>, validator: (header: HeaderDataNode, body: DataNode) -> Any): R {
//        println("kotlin post return")
//        return http.post(url, payload, HttpResponseValidatorWithReturn(validator))
//    }
//
//    fun Http.post(url: String, payload: Map<String, Any>, validator: (header: HeaderDataNode, body: DataNode) -> Unit) {
//        println("kotlin post")
//        http.post(url, payload, HttpResponseValidator(validator))
//    }
//}