package scenarios.rest.headers.auth

import com.twosigma.webtau.console.ConsoleOutputs
import com.twosigma.webtau.http.HttpHeader

class Auth {
    static HttpHeader authHeader(String fullUrl, String url, HttpHeader original) {
        ConsoleOutputs.out('auth header injection point')
        return original.merge([Authorization: 'Bearer <token>'])
    }
}
