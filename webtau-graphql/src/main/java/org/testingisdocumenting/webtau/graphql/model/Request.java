package org.testingisdocumenting.webtau.graphql.model;

public class Request {
    public static final String INTROSPECTION_QUERY = "{\n" +
            "    __schema {\n" +
            "        queryType {\n" +
            "            fields {\n" +
            "                name\n" +
            "            }\n" +
            "        }\n" +
            "        mutationType {\n" +
            "            fields {\n" +
            "                name\n" +
            "            }\n" +
            "        }\n" +
            "        subscriptionType {\n" +
            "            fields {\n" +
            "                name\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}\n";
}
