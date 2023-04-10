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

package org.testingisdocumenting.webtau.graphql;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.testingisdocumenting.webtau.http.HttpHeader;
import org.testingisdocumenting.webtau.http.config.WebTauHttpConfiguration;
import org.testingisdocumenting.webtau.http.config.WebTauHttpConfigurations;
import org.testingisdocumenting.webtau.data.datanode.DataNode;
import org.testingisdocumenting.webtau.http.validation.HttpResponseValidator;
import org.testingisdocumenting.webtau.http.validation.HttpResponseValidatorWithReturn;
import org.testingisdocumenting.webtau.utils.CollectionUtils;
import org.testingisdocumenting.webtau.utils.UrlUtils;

import java.util.Map;
import java.util.function.Consumer;

import static org.testingisdocumenting.webtau.Matchers.actual;
import static org.testingisdocumenting.webtau.Matchers.equal;

public class GraphQLTestBase implements WebTauHttpConfiguration {
    protected static final GraphQLTestDataServer testServer = new GraphQLTestDataServer();

    protected final static String QUERY = "{ taskById(id: \"a\") { id } }";

    protected final static String MULTI_OP_QUERY = "query task { taskById(id: \"a\") { id } } " +
            "query openTasks { allTasks(uncompletedOnly: true) { id } }";
    protected final static String OP_NAME = "task";

    protected final static String QUERY_WITH_VARS = "query task($id: ID!) { taskById(id: $id) { id } }";
    protected final static Map<String, Object> VARS = CollectionUtils.map("id", "a");

    protected final static String MULTI_OP_QUERY_WITH_VARS = "query task($id: ID!) { taskById(id: $id) { id } } " +
            "query openTasks { allTasks(uncompletedOnly: true) { id } }";

    protected final static String ERROR_QUERY = "query error($msg: String!) { error(msg: $msg) { msg } }";

    protected final static HttpResponseValidator VALIDATOR = (header, body) -> body.get("data.taskById.id").should(equal("a"));
    protected final static HttpResponseValidatorWithReturn VALIDATOR_WITH_RETURN = (header, body) -> {
        body.get("data.taskById.id").should(equal("a"));
        return body.get("data.taskById.id");
    };
    protected final static Consumer<String> ID_ASSERTION = id -> actual(id).should(equal("a"));
    protected final static Consumer<DataNode> BODY_ASSERTION = body -> body.get("data.taskById.id").should(equal("a"));

    protected final static String AUTH_HEADER_VALUE = "aSuperSecretToken";
    protected final static HttpHeader AUTH_HEADER = HttpHeader.EMPTY.with("Authorization", AUTH_HEADER_VALUE);

    @BeforeClass
    public static void startServer() {
        testServer.start();
    }

    @AfterClass
    public static void stopServer() {
        testServer.stop();
    }

    @Before
    public void initCfg() {
        WebTauHttpConfigurations.add(this);
    }

    @After
    public void cleanCfg() {
        WebTauHttpConfigurations.remove(this);
    }

    @Override
    public String fullUrl(String url) {
        if (UrlUtils.isFull(url)) {
            return url;
        }

        return UrlUtils.concat(testServer.getUri(), url);
    }

    @Override
    public HttpHeader fullHeader(String fullUrl, String passedUrl, HttpHeader given) {
        return given;
    }
}
