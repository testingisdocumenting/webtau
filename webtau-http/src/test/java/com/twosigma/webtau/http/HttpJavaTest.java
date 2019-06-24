/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.http;

import com.twosigma.webtau.cfg.ConfigValue;
import com.twosigma.webtau.data.table.TableData;
import com.twosigma.webtau.http.config.HttpConfiguration;
import com.twosigma.webtau.http.config.HttpConfigurations;
import com.twosigma.webtau.utils.CollectionUtils;
import com.twosigma.webtau.utils.UrlUtils;
import org.junit.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.twosigma.webtau.Ddjt.*;
import static com.twosigma.webtau.cfg.WebTauConfig.getCfg;
import static com.twosigma.webtau.http.Http.http;

public class HttpJavaTest implements HttpConfiguration  {
    private static final HttpTestDataServer testServer = new HttpTestDataServer();

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
        HttpConfigurations.add(this);
    }

    @After
    public void cleanCfg() {
        HttpConfigurations.remove(this);
    }

    @Test
    public void simpleObjectMappingExample() {
        http.get("/end-point-simple-object", (header, body) -> {
            body.get("k1").should(equal("v1"));
        });
    }

    @Test
    public void simpleListMappingExample() {
        http.get("/end-point-simple-list", (header, body) -> {
            body.get(0).get("k1").should(equal("v1"));
        });
    }

    @Test
    public void matchersBasicExample() {
        http.get("/example", (header, body) -> {
            body.get("year").shouldNot(equal(2000));
            body.get("genres").should(contain("RPG"));
            body.get("rating").shouldBe(greaterThan(7));
        });
    }

    @Test
    public void equalityMatcher() {
        http.get("/end-point", (header, body) -> {
            body.get("id").shouldNot(equal(0));
            body.get("amount").should(equal(30));

            body.get("list").should(equal(Arrays.asList(1, 2, 3)));

            body.get("object").get("k1").should(equal(
                    Pattern.compile("v\\d"))); // regular expression matching

            body.get("object").should(equal(aMapOf(
                    "k1", "v1",
                    "k3", "v3"))); // matching only specified fields and can be nested multiple times

            body.get("complexList").should(equal(table("k1" , "k2", // matching only specified fields, but number of entries must be exact
                                                      ________________,
                                                       "v1" ,  30,
                                                       "v11",  40)));
        });

        http.doc.capture("end-point-object-equality-matchers");
    }

    @Test
    public void equalityMatcherTableKey() {
        http.get("/end-point", (header, body) -> {
            body.get("complexList").should(equal(table("*id", "k1" , "k2", // order agnostic key based match
                                                       ________________,
                                                       "id2", "v11", 40,
                                                       "id1", "v1" , 30)));
        });
    }

    @Test
    public void compareNumbersWithGreaterLessMatchers() {
        http.get("/end-point-numbers", (header, body) -> {
            body.get("id").shouldBe(greaterThan(0));
            body.get("price").shouldBe(greaterThanOrEqual(100));
            body.get("amount").shouldBe(lessThan(150));
            body.get("list").get(1).shouldBe(lessThanOrEqual(2));

            body.get("id").shouldNotBe(lessThanOrEqual(0));
            body.get("price").shouldNotBe(lessThan(100));
            body.get("amount").shouldNotBe(greaterThanOrEqual(150));
            body.get("list").get(1).shouldNotBe(greaterThan(2));
        });

        http.doc.capture("end-point-numbers-matchers");
    }

    @Test
    public void containMatcher() {
        http.get("/end-point-list", (header, body) -> {
            body.should(contain(aMapOf(
                    "k1", "v1",
                    "k2", "v2")));
            body.get(1).get("k2").shouldNot(contain(22));
        });

        http.doc.capture("end-point-list-contain-matchers");
    }

    @Test
    public void workingWithDates() {
        http.get("/end-point-dates", (header, body) -> {
            LocalDate expectedDate = LocalDate.of(2018, 6, 12);
            ZonedDateTime expectedTime = ZonedDateTime.of(expectedDate,
                    LocalTime.of(9, 0, 0),
                    ZoneId.of("UTC"));

            body.get("tradeDate").should(equal(expectedDate));
            body.get("transactionTime").should(equal(expectedTime));
            body.get("transactionTime").shouldBe(greaterThanOrEqual(expectedDate));

            body.get("paymentSchedule").should(contain(expectedDate));
        });

        http.doc.capture("end-point-dates-matchers");
    }

    @Test
    public void matchersCombo() {
        Pattern withNumber = Pattern.compile("v\\d");

        http.get("/end-point-mixed", (header, body) -> {
            body.get("list").should(contain(lessThanOrEqual(2))); // lessThanOrEqual will be matched against each value

            body.get("object").should(equal(aMapOf(
                    "k1", "v1",
                    "k3", withNumber))); // regular expression match against k3

            body.get("complexList").get(0).should(equal(aMapOf(
                    "k1", "v1",
                    "k2", lessThan(120)))); // lessThen match against k2

            body.get("complexList").get(1).should(equal(aMapOf(
                    "k1", notEqual("v1"), // any value but v1
                    "k2", greaterThanOrEqual(120))));

            TableData expected = table("k1"        , "k2", // matching only specified fields, but number of entries must be exact
                                       ________________________________,
                                        withNumber , lessThan(120),
                                        "v11"      , greaterThan(150));

            body.get("complexList").should(equal(expected));
        });

        http.doc.capture("end-point-mixing-matchers");
    }

    @Test
    public void compression() {
        http.get("/end-point", (header, body) -> {
            body.get("id").shouldNot(equal(0));
            body.get("amount").should(equal(30));

            header.get("content-encoding").shouldNot(equal("gzip"));
        });

        http.get("/end-point", http.header("Accept-Encoding", "gzip"), (header, body) -> {
            body.get("id").shouldNot(equal(0));
            body.get("amount").should(equal(30));

            header.get("content-encoding").should(equal("gzip"));
            header.get("ContentEncoding").should(equal("gzip"));
        });
    }

    @Test
    public void redirects() {
        http.get("/redirect", (header, body) -> {
            body.get("id").shouldNot(equal(0));
            body.get("amount").should(equal(30));
        });
    }

    @Test
    public void redirectLoop() {
        ConfigValue maxRedirects = getCfg().findConfigValue("maxRedirects");
        maxRedirects.set("test", 3);
        try {
            http.get("/recursive", (header, body) -> {
                header.statusCode().should(equal(302));
            });
        } finally {
            maxRedirects.reset();
        }
    }

    @Test
    public void redirectPost() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("hello", "world");
        http.post("/redirect", requestBody, (header, body) -> {
            body.should(equal(requestBody));
        });
    }

    @Test
    public void redirectPut() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("hello", "world");
        http.put("/redirect", requestBody, (header, body) -> {
            body.should(equal(requestBody));
        });
    }

    @Test
    public void queryParams() {
        http.get("/params?a=1&b=text", (header, body) -> {
            body.get("a").should(equal(1));
            body.get("b").should(equal("text"));
        });

        Map<String, String> queryParams = CollectionUtils.aMapOf("a", 1, "b", "text");
        http.get("/params", http.query(queryParams), (header, body) -> {
            body.get("a").should(equal(1));
            body.get("b").should(equal("text"));
        });

        http.get("/params", http.query("a", "1", "b", "text"), (header, body) -> {
            body.get("a").should(equal(1));
            body.get("b").should(equal("text"));
        });
    }

    @Test
    public void handlesIntegerJsonResponses() {
        Integer ret = http.get("/integer", (header, body) -> {
            body.should(equal(123));
            return body;
        });

        actual(ret).should(equal(123));
        actual(ret.getClass()).should(equal(Integer.class));
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
