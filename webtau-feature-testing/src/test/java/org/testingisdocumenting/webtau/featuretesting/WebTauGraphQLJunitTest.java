package org.testingisdocumenting.webtau.featuretesting;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testingisdocumenting.webtau.cfg.WebTauConfig;
import org.testingisdocumenting.webtau.graphql.GraphQLConfig;
import org.testingisdocumenting.webtau.graphql.GraphQLSchema;
import org.testingisdocumenting.webtau.http.HttpHeader;
import org.testingisdocumenting.webtau.http.config.HttpConfiguration;
import org.testingisdocumenting.webtau.http.config.HttpConfigurations;
import org.testingisdocumenting.webtau.http.testserver.GraphQLResponseHandler;
import org.testingisdocumenting.webtau.http.testserver.TestServer;

import java.util.Arrays;

import static org.testingisdocumenting.webtau.Matchers.equal;
import static org.testingisdocumenting.webtau.WebTauDsl.http;
import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg;
import static org.testingisdocumenting.webtau.featuretesting.WebTauGraphQLFeaturesTestData.AUTH_TOKEN;
import static org.testingisdocumenting.webtau.graphql.GraphQL.graphql;

public class WebTauGraphQLJunitTest {
    private static TestServer testServer;
    private static GraphQLResponseHandler handler;

    @BeforeClass
    public static void startServer() {
        handler = WebTauGraphQLFeaturesTestData.graphQLResponseHandler();

        testServer = new TestServer(handler);
        testServer.startRandomPort();
    }

    @AfterClass
    public static void stopServer() {
        testServer.stop();
    }

    @Before
    public void setUp() {
        WebTauConfig.resetConfigHandlers();
        getCfg().reset();

        getCfg().setBaseUrl(testServer.getUri().toString());
//        GraphQLConfig.graphQLEnabled.set("test", true);
    }

    @Test
    public void testStuff() {
        String listAllQuery = "{" +
                "    allTasks(uncompletedOnly: false) {" +
                "        id" +
                "        description" +
                "    }" +
                "}";
        withHttpHeaderProvider(() ->
                handler.withAuthEnabled(AUTH_TOKEN, () ->
                        graphql.execute(listAllQuery, (header, body) -> {
                            body.get("errors").should(equal(null));
                            body.get("data.allTasks.id").should(equal(Arrays.asList("a", "b", "c")));

                            //Remove this return once we have execute overrides with HttpResponseValidatorIgnoringReturn
                            return null;
                        })
                ));
    }

    private static void withHttpHeaderProvider(Runnable code) {
        HttpConfiguration testConfig = new HttpConfiguration() {
            @Override
            public String fullUrl(String url) {
                return url;
            }

            @Override
            public HttpHeader fullHeader(String fullUrl, String passedUrl, HttpHeader httpHeaders) {
                String token = http.get(testServer.getUri().toString() + "/auth", (header, body) -> {
                    header.statusCode().should(equal(200));
                    body.get("token").shouldNot(equal(null));
                    return body.get("token");
                });

                return httpHeaders.with("Authorization", token);
            }
        };

        HttpConfigurations.add(testConfig);
        try {
            code.run();
        } finally {
            HttpConfigurations.remove(testConfig);
        }
    }
}
