package scenarios.ui

import static com.twosigma.webtau.WebTauGroovyDsl.*
import static pages.Pages.form

scenario('open forms') {
    browser.open('/forms')
}

scenario('input type default') {
    form.name.setValue('Full Automation')
}

scenario('input type date') {
    form.startDate.setValue('06/21/2016')
}

scenario('select options') {
    form.rank.setValue('B')
}

scenario('values validation') {
    form.name.should == 'Full Automation'
    form.rank.should == 'B'
    form.startDate.should == '2016-06-21'
}