package com.twosigma.webtau.data

import org.junit.Test

import static com.twosigma.webtau.data.LazyTestResource.createLazyResource

class LazyTestResourceTest {
    @Test
    void "wraps a provided bean with a delayed initialization"() {
        def sequence = []

        sequence << 1
        def data = createLazyResource("resource name") {
            sequence << 2
            return new LazyData(firstName: 'first-name', score: 100)
        }
        sequence << 3

        data.firstName.should == 'first-name'
        data.score.should == 100

        sequence.should == [1, 3, 2]
    }
}

class LazyData {
    String firstName
    Integer score
}
