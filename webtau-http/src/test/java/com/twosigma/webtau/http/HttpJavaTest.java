/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

import com.twosigma.webtau.Ddjt;
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
import java.util.regex.Pattern;

import static com.twosigma.webtau.Ddjt.*;
import static com.twosigma.webtau.http.Http.http;
import static com.twosigma.webtau.utils.CollectionUtils.createMap;

public class HttpJavaTest implements HttpConfiguration  {
    private static final HttpTestDataServer testServer = new HttpTestDataServer();

    private static final int PORT = 7824;

    @BeforeClass
    public static void startServer() {
        testServer.start(PORT);
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
    public void equalityMatcher() {
        http.get("/end-point", (header, body) -> {
            body.get("id").shouldNot(equal(0));
            body.get("amount").should(equal(30));

            body.get("list").should(equal(Arrays.asList(1, 2, 3)));

            body.get("object").get("k1").should(equal(
                    Pattern.compile("v\\d"))); // regular expression matching

            body.get("object").should(equal(createMap("k1", "v1", "k3", "v3"))); // matching only specified fields and can be nested multiple times

            body.get("complexList").should(equal(header("k1" , "k2").values( // matching only specified fields, but number of entries must be exact
                                                        "v1" ,  30,
                                                        "v11",  40)));
        });

        http.doc.capture("end-point-object-equality-matchers");
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
            body.should(contain(createMap("k1", "v1", "k2", "v2")));
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
        http.get("/end-point-mixed", (header, body) -> {
            body.get("list").should(contain(lessThanOrEqual(2))); // lessThanOrEqual will be matched against each value

            body.get("object").should(equal(createMap(
                    "k1", "v1",
                    "k3",  Pattern.compile("v\\d")))); // regular expression match against k3

            body.get("complexList").get(0).should(equal(createMap(
                    "k1", "v1",
                    "k2", lessThan(120)))); // lessThen match against k2

            body.get("complexList").get(1).should(equal(createMap(
                    "k1", notEqual("v1"), // any value but v1
                    "k2", greaterThanOrEqual(120))));

            body.get("complexList").should(equal(
                    header(                    "k1", "k2").values( // matching only specified fields, but number of entries must be exact
                            Pattern.compile("v\\d"),  lessThan(120),
                                              "v11",  greaterThan(150))));
        });

        http.doc.capture("end-point-mixing-matchers");
    }

    @Override
    public String fullUrl(String url) {
        if (UrlUtils.isFull(url)) {
            return url;
        }

        return UrlUtils.concat("http://localhost:" + PORT, url);
    }

    @Override
    public HttpHeader fullHeader(String fullUrl, String passedUrl, HttpHeader given) {
        return given;
    }
}
