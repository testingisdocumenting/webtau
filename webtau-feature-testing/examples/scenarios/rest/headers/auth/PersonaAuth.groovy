package scenarios.rest.headers.auth

import org.testingisdocumenting.webtau.http.HttpHeader
import static org.testingisdocumenting.webtau.WebTauDsl.*

class PersonaAuth {
    static HttpHeader authHeader(String fullUrl, String url, HttpHeader original) {
        def token = generateTokenBasedOnPersona()
        return original.merge([Authorization: "Bearer $token"])
    }

    static String generateTokenBasedOnPersona() {
        if (currentPersona.isDefault()) { // check if we are inside persona context or outside
            return generateDefaultToken()
        }

        return generateTokenForSystemUserId(currentPersona.payload.authId) // use persona payload to generate required token
    }

    static String generateTokenForSystemUserId(String systemUserId) {
        return "dummy:$systemUserId" // this is where you generate specific user auth token
    }

    static String generateDefaultToken() {
        return "dummy:default-user" // this is where you generate default user auth token
    }
}
