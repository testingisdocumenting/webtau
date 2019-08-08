package formHandlers

import com.twosigma.webtau.browser.page.HtmlNode
import com.twosigma.webtau.browser.page.PageElement
import com.twosigma.webtau.browser.page.PageElementStepExecutor
import com.twosigma.webtau.browser.page.value.handlers.PageElementGetSetValueHandler
import com.twosigma.webtau.reporter.TokenizedMessage

class CustomInput implements PageElementGetSetValueHandler {
    @Override
    boolean handles(HtmlNode htmlNode, PageElement pageElement) {
        return htmlNode.attributes.class =~ /special-selector/
    }

    @Override
    void setValue(PageElementStepExecutor stepExecutor,
                  TokenizedMessage pathDescription,
                  HtmlNode htmlNode,
                  PageElement pageElement,
                  Object value) {
        pageElement.click()
        pageElement.find('input').sendKeys("${value}\t")
    }

    @Override
    String getValue(HtmlNode htmlNode, PageElement pageElement) {
        return pageElement.find('.current-value').getUnderlyingValue()
    }
}
