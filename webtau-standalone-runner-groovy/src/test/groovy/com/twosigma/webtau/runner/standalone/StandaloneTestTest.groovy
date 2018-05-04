package com.twosigma.webtau.runner.standalone

import org.junit.Test

import java.nio.file.Paths

import static com.twosigma.webtau.Ddjt.equal

class StandaloneTestTest {
    @Test
    void "registered payloads should be merged into toMap test representation"() {
        def test = new StandaloneTest(Paths.get("."), Paths.get("test.groovy"), "my test description", {})
        test.addResultPayload({ return [screenshot: "base64" ]})
        test.addResultPayload({ return [steps: ["step1", "step2"] ]})

        test.toMap().should equal([id: 'test.groovy-1',
                                   scenario: 'my test description',
                                   fileName: 'test.groovy',
                                   assertion: null,
                                   contextDescription: null,
                                   shortStackTrace: null,
                                   fullStackTrace: null,
                                   exceptionMessage: null,
                                   failedCodeSnippets: null,
                                   status: 'Skipped',
                                   screenshot: 'base64', steps: ['step1', 'step2']])
    }
}
