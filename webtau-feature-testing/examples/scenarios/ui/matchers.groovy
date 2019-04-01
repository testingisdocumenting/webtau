package scenarios.ui

import static com.twosigma.webtau.WebTauGroovyDsl.*

scenario('open browser') {
    browser.open('/matchers')
}

scenario('equal text') {
    def message = $('#message')
    message.should == 'Select option'
}

scenario('equal text regexp') {
    def message = $('#message')
    message.should == ~/option/
}

scenario('equal list of text') {
    def menu = $('#menu ul li')
    menu.should == ['Hello', 'Text', 'World']
}

scenario('equal list of text and regexp') {
    def menu = $('#menu ul li')
    menu.should == ['Hello', ~/T..t/, 'World']
}

scenario('equal number') {
    def total = $('#total')
    total.should == 300.6
}

scenario('equal list of numbers') {
    def split = $('#split ul li')
    split.should == [100, 28, 172.6]
}

scenario('greater number') {
    def total = $('#total')
    total.shouldBe > 200
}

scenario('greater equal number') {
    def total = $('#total')
    total.shouldBe >= 300
}

scenario('less equal list mix of numbers') {
    def split = $('#split ul li')
    split.should == [100, lessThan(100), greaterThanOrEqual(150)]
}

scenario('enable state') {
    def button = $("#action")
    button.should beDisabled()
    button.shouldNot beEnabled()
}

scenario('visible state') {
    def feedback = $("#feedback")
    feedback.should beHidden()
    feedback.shouldNot beVisible()
}