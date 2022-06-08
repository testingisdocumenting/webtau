package scenarios.concept

import org.testingisdocumenting.webtau.persona.Persona

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*
import static personas.Personas.*

// context-example-snippet
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
// context-example-snippet

scenario('config validation') {
    cfg.email.should == 'default@email.send'
    cfg.customValue.should == 100

    Alice {
       step("do something in context of Alice") {
           cfg.email.should == 'alice@email.send'
           cfg.customValue.should == 105
       }
    }

    Bob {
        step("do same thing in context of Bob") {
            cfg.email.should == 'bob@email.send'
            cfg.customValue.should == 110
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
