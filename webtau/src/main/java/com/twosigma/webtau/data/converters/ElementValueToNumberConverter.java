package com.twosigma.webtau.data.converters;

import com.twosigma.webtau.data.converters.ToNumberConverter;
import com.twosigma.webtau.page.ElementValue;
import com.twosigma.webtau.page.PageElement;
import com.twosigma.webtau.utils.NumberUtils;

public class ElementValueToNumberConverter implements ToNumberConverter {
    @Override
    public Number convert(Object v) {
        if (! (v instanceof ElementValue)) {
            return null;
        }

        ElementValue elementValue = (ElementValue) v;
        return NumberUtils.convertStringToNumber(elementValue.get().toString());
    }
}
