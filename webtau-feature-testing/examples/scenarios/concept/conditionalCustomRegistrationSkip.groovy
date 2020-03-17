package scenarios.concept

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

String emailHost = cfg.emailHost

onlyWhen('email server is internal', { -> emailHost.contains('internal.server')}) {
    scenario('confirmation emails should be sent') {
        // ...
        http.get(emailHost) {
            subjects.should contain('my message')
        }
    }
}