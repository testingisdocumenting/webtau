package com.twosigma.webtau.data.converters;

import com.twosigma.webtau.page.PageElement;
import com.twosigma.webtau.utils.NumberUtils;

public class PageElementToNumberConverter implements ToNumberConverter {
    @Override
    public Number convert(Object v) {
        if (! (v instanceof PageElement)) {
            return null;
        }

        PageElement pageElement = (PageElement) v;
        return NumberUtils.convertStringToNumber(pageElement.elementValue().get().toString());
    }
}
