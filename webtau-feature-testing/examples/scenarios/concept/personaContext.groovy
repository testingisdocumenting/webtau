package scenarios.concept

import org.testingisdocumenting.webtau.persona.Persona

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*
import static personas.Personas.*

scenario('context example') {
    Alice {
       step("do something in context of Alice") {
           customAction()
       }
    }

    Bob {
        step("do same thing in context of Bob") {
            customAction()
        }
    }
}

def customAction() {
    def id = Persona.currentPersona.id
    def authId = Persona.currentPersona.payload.authId // from persona payload
    def email = cfg.email // from persona associated config override
    step("custom action", [authId: authId, email: email]) {
        println "authenticating $id"
    }
}
