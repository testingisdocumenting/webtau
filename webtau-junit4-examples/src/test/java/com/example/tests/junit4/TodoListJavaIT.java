package com.example.tests.junit4;

import org.testingisdocumenting.webtau.junit4.WebTauRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.testingisdocumenting.webtau.WebTauDsl.*;

@RunWith(WebTauRunner.class)
public class TodoListJavaIT {
    @Test
    public void fetchTodoItem() {
        http.get("/todos/1", (header, body) -> {
            body.get("title").should(equal("delectus aut autem"));
            body.get("completed").should(equal(false));
        });
    }
}