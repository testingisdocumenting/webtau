package formHandlers

import org.openqa.selenium.Keys
import org.testingisdocumenting.webtau.browser.page.HtmlNode
import org.testingisdocumenting.webtau.browser.page.HtmlNodeAndWebElementList
import org.testingisdocumenting.webtau.browser.page.PageElement
import org.testingisdocumenting.webtau.browser.page.PageElementStepExecutor
import org.testingisdocumenting.webtau.browser.handlers.PageElementGetSetValueHandler
import org.testingisdocumenting.webtau.reporter.TokenizedMessage

class CustomInput implements PageElementGetSetValueHandler {
    @Override
    boolean handles(HtmlNodeAndWebElementList htmlNodeAndWebElements, PageElement pageElement) {
        def htmlNode = htmlNodeAndWebElements.firstHtmlNode()
        return htmlNode.attributes.class =~ /special-selector/
    }

    @Override
    void setValue(PageElementStepExecutor stepExecutor,
                  TokenizedMessage pathDescription,
                  HtmlNodeAndWebElementList htmlNodeAndWebElements,
                  PageElement pageElement,
                  Object value,
                  boolean noLog) {
        pageElement.click()
        pageElement.find('input').sendKeys("${value}" + Keys.TAB)
    }

    @Override
    Object getValue(HtmlNodeAndWebElementList htmlNodeAndWebElements, PageElement pageElement, int idx) {
        return pageElement.find('.current-value').getUnderlyingValue()
    }
}
