package scenarios.concept

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

def lever = 10

def useCases = ['title'  | 'input'    | 'output'] {
               ____________________________________
                'hello'  | lever      | lever + 10
                'world'  | lever + 30 | lever + 40 }

useCases.each { row ->
    scenario("use case ${row.title}") {
        println row.input
        println row.output
    }
}