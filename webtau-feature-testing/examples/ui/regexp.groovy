package ui

import static com.twosigma.webtau.WebTauGroovyDsl.scenario
import static pages.Pages.getSearch

scenario("""checks if an element or its value matches provided matcher""") {
    search.open()
    search.welcomeMessage.should == ~/welcome to \w+ search/
    search.welcomeMessage.shouldNot == ~/welcome to \w+ S.*/
    search.welcomeMessage.waitTo == ~/welcome to \w+ search/
    search.welcomeMessage.waitToNot == ~/welcome to \w+ S.*/
}
