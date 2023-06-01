# Page Element Declaration

Use `$("css-selector")` or `browser.element("css-selector")` to lazily declare a page element:

```groovy {title: "select by id"}
$("#element-id")
```

```groovy {title: "select by class"}
$(".label")
```

```groovy {title: "select by attribute"}
$("[data-test-id='my-id']")
```

[Read Finders And Filters](browser/finders-and-filters) to learn how to select elements using more advanced techniques. 

[Read W3Schools CSS selectors](https://www.w3schools.com/cssref/css_selectors.asp) to learn all kind of CSS selection techniques.   

# Lazy Element 

When you use `$("css-selector")` you create an instance of `PageElement`. 

`PageElement` encapsulates actions that can be performed on a web page. It also represents values on a page.
It is safe to declare `PageElement` before element is actually present on a page. 

WebTau will try to locate element only when you query or action on it:

:include-file: scenarios/ui/basicDeclareFirst.groovy

# Lazy Value 

Consider a simple search page. Enter value, hit enter, see results:

:include-file: scenarios/ui/searchInnerDeclarations.groovy

In the example `$("#results .result").count` represents the number of elements matching the css selector. 
Let's extract it.

:include-file: scenarios/ui/searchOuterDeclarations.groovy
 