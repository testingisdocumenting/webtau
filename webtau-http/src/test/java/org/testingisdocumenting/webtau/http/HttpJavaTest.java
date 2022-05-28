/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.http;

import org.testingisdocumenting.webtau.cfg.ConfigValue;
import org.testingisdocumenting.webtau.data.table.TableData;
import org.testingisdocumenting.webtau.utils.CollectionUtils;
import org.junit.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg;
import static org.testingisdocumenting.webtau.http.Http.http;

public class HttpJavaTest extends HttpTestBase {
    private static final byte[] sampleFile = {1, 2, 3};

    @Test
    public void ping() {
        actual(http.ping("/end-point-simple-object")).should(equal(true));
        actual(http.ping("/end-point-simple-object", http.query("key", "value"))).should(equal(true));
        actual(http.ping("/end-point-simple-object",
                http.query("key", "value"),
                http.header("X-flag", "test"))).should(equal(true));

        actual(http.ping("/end-point-simple-object-non-existing-url")).should(equal(false));
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
    public void containAllMatcher() {
        http.get("/end-point-list", (header, body) -> {
            body.get(1).get("k2").should(containAll(10, 30));
            body.get(1).get("k2").shouldNot(containAll(40, 60, 80));
        });

        http.doc.capture("end-point-list-contain-all-matchers");
    }

    @Test
    public void containContainingAllMatcher() {
        http.get("/prices", (header, body) -> {
            body.get("prices").should(contain(containingAll(10, 30)));
        });

        http.doc.capture("prices-contain-containing-all");
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
    public void explicitHeaderPassingExample() {
        http.get("/end-point", http.header("Accept", "application/octet-stream"), (header, body) -> {
            // assertions go here
        });

        http.get("/end-point", http.query("queryParam1", "queryParamValue1"),
            http.header("Accept", "application/octet-stream"), (header, body) -> {
            // assertions go here
        });

        http.patch("/end-point", http.header("Accept", "application/octet-stream"),
                aMapOf("fileId", "myFile"), (header, body) -> {
            // assertions go here
        });

        http.post("/end-point", http.header("Accept", "application/octet-stream"),
                aMapOf("fileId", "myFile"), (header, body) -> {
            // assertions go here
        });

        http.put("/end-point", http.header("Accept", "application/octet-stream"),
                aMapOf("fileId", "myFile", "file", sampleFile), (header, body) -> {
            // assertions go here
        });

        http.delete("/end-point", http.header("Custom-Header", "special-value"));
    }

    @Test
    public void headerCreation() {
        HttpHeader varArgHeader = http.header(
                "My-Header1", "Value1",
                "My-Header2", "Value2");

        Map<CharSequence, CharSequence> headerValues = new HashMap<>();
        headerValues.put("My-Header1", "Value1");
        headerValues.put("My-Header2", "Value2");
        HttpHeader mapBasedHeader = http.header(headerValues);

        actual(varArgHeader).should(equal(mapBasedHeader)); // doc-exclude
    }

    @Test
    public void headerWith() {
        HttpHeader header = http.header(
                "My-Header1", "Value1",
                "My-Header2", "Value2");

        // example
        HttpHeader newHeaderVarArg = header.with(
                "Additional-1", "AdditionalValue1",
                "Additional-2", "AdditionalValue2");

        Map<CharSequence, CharSequence> additionalValues = new HashMap<>();
        additionalValues.put("Additional-1", "AdditionalValue1");
        additionalValues.put("Additional-2", "AdditionalValue2");
        HttpHeader newHeaderMap = header.with(additionalValues);
        // example

        actual(newHeaderVarArg).should(equal(newHeaderMap));
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

    @Test
    public void canQueryNodeByPath() {
        http.get("/end-point", (header, body) -> {
            body.get("object.k1").should(equal("v1"));
        });
    }

    @Test
    public void canQueryListByNodePath() {
        http.get("/end-point", (header, body) -> {
            body.get("complexList.k1").should(equal(Arrays.asList("v1", "v11")));
        });
    }

    @Test
    public void canQuerySpecificListElementByPath() {
        http.get("/end-point", (header, body) -> {
            body.get("complexList[0].k1").should(equal("v1"));
            body.get("complexList[-1].k1").should(equal("v11"));
        });
    }

    @Test
    public void explicitBinaryMimeTypesCombinedWithRequestBody() {
        byte[] content = binaryFileContent("path");
        http.post("/end-point", http.body("application/octet-stream", content), (header, body) -> {
            // assertions go here
        });
    }

    @Test
    public void postImplicitBinaryMimeTypesCombinedWithRequestBody() {
        byte[] content = binaryFileContent("path");
        http.post("/end-point", http.application.octetStream(content), (header, body) -> {
            // assertions go here
        });
    }

    @Test
    public void postImplicitTextMimeTypesCombinedWithRequestBody() {
        String content = "text content";

        http.post("/end-point", http.text.plain(content), (header, body) -> {
            // assertions go here
        });
    }

    private static byte[] binaryFileContent(String path) {
        return new byte[]{1, 2, 3};
    }
}
