package scenarios.rest.headers.auth

import org.testingisdocumenting.webtau.http.HttpHeader

class Auth {
    static HttpHeader authHeader(String fullUrl, String url, HttpHeader original) {
        def token = step("generate auth token") {
            return "jwt-token"
        }

        return original.merge([Authorization: "Bearer $token"])
    }
}
