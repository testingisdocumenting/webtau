package scenarios.rest.headers.auth

import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.http.HttpHeader

class Auth {
    static HttpHeader authHeader(String fullUrl, String url, HttpHeader original) {
        ConsoleOutputs.out('auth header injection point')
        return original.merge([Authorization: 'Bearer <token>'])
    }
}
