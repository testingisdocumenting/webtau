package scenarios.rest.headers.auth

import org.testingisdocumenting.webtau.http.HttpHeader

class Auth {
    static HttpHeader authHeader(String fullUrl, String url, HttpHeader original) {
        def token = generateToken()
        return original.merge([Authorization: "Bearer $token"])
    }

    private static String generateToken() {
        return "jwt-token"
    }
}
