package personas

import static org.testingisdocumenting.webtau.WebTauCore.persona

class Personas {
    public static def Alice = persona("Alice", [authId: "alice-user-id"])
    public static def Bob = persona("Bob", [authId: "bob-user-id"])
}
