package scenarios.ui

import static com.twosigma.webtau.WebTauGroovyDsl.*
import static pages.Pages.form

scenario('open forms') {
    browser.open('/forms')
}

scenario('input') {
    form.name.setValue('Full Automation')
}

scenario('select options') {
    form.rank.setValue('B')
}

scenario('values validation') {
    form.name.should == 'Full Automation'
    form.rank.should == 'B'
}