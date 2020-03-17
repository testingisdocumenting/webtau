package scenarios.concept

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

class Support {
    static void owner(String name) {
        metadata([owner: name])
    }

    static void severity(int level) {
        metadata([severity: level])
    }

    static void teamA() {
        owner("team A")
    }

    static void teamB() {
        owner("team B")
    }

    static void critical() {
        severity(100)
    }

    static void low() {
        severity(1)
    }
}
