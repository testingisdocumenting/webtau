package com.example.tests.junit4

import com.twosigma.webtau.junit4.WebTauRunner
import org.junit.Test
import org.junit.runner.RunWith

import static com.twosigma.webtau.WebTauDsl.*

@RunWith(WebTauRunner)
class TodoListGroovyIT {
    @Test
    void "fetch todo item"() {
        http.get('/todos/1') {
            title.should == 'delectus aut autem'
            completed.should == false
        }
    }
}