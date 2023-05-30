package formHandlers

import org.testingisdocumenting.webtau.browser.page.HtmlNodeAndWebElementList
import org.testingisdocumenting.webtau.browser.page.PageElement
import org.testingisdocumenting.webtau.browser.page.PageElementStepExecutor
import org.testingisdocumenting.webtau.browser.handlers.PageElementGetSetValueHandler
import org.testingisdocumenting.webtau.reporter.TokenizedMessage

import static org.testingisdocumenting.webtau.WebTauDsl.browser

class CustomInput implements PageElementGetSetValueHandler {
    @Override
    boolean handles(HtmlNodeAndWebElementList htmlNodeAndWebElements, PageElement pageElement) {
        def htmlNode = htmlNodeAndWebElements.firstHtmlNode()
        return htmlNode.attributes().class =~ /special-selector/
    }

    @Override
    void setValue(PageElementStepExecutor stepExecutor,
                  TokenizedMessage pathDescription,
                  HtmlNodeAndWebElementList htmlNodeAndWebElements,
                  PageElement pageElement,
                  Object value,
                  boolean noLog) {
        pageElement.click()
        pageElement.find('input').sendKeys("${value}" + browser.keys.tab)
    }

    @Override
    Object getValue(HtmlNodeAndWebElementList htmlNodeAndWebElements, PageElement pageElement, int idx) {
        return pageElement.find('.current-value').getUnderlyingValue()
    }
}
