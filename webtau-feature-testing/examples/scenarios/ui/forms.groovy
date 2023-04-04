package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*
import static pages.Pages.form

scenario('open forms') {
    browser.open('/forms')
}

scenario('input type default') {
    form.name.setValue('Full Automation')
}

scenario('input type date') {
    form.startDate.setValue('2016-06-21')
}

scenario('select options') {
    form.rank.setValue('B')
}

scenario('select options by text') {
    form.rank.setValue('Full B')
}

scenario('select checkbox') {
    form.confirmation.setValue(true)
}

scenario('select radio button') {
    form.choice.setValue('value-two')
}

scenario('values validation') {
    form.name.should == 'Full Automation'
    form.rank.should == 'Full B'
    form.confirmation.should == true
    form.choice.should == 'value-two'
    form.startDate.should == '2016-06-21'
}

scenario('select checkbox multiple times') {
    form.confirmation.setValue(true)
    form.confirmation.setValue(true)
    form.confirmation.should == true
}

scenario('deselect checkbox') {
    form.confirmation.setValue(false)
    form.confirmation.should == false
}

scenario('deselect checkbox multiple times') {
    form.confirmation.setValue(false)
    form.confirmation.should == false
}

scenario('select options failed') {
    code {
        form.rank.setValue('Full Y')
    } should throwException(~/Cannot locate option with either value or visible text: Full Y/)
}

