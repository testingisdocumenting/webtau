# Element Selection

Use `$('css-selector')` to select a page element.

```groovy {title: "select by id"}
$('#element-id')
```

```groovy {title: "select by class"}
$('.label')
```

```groovy {title: "select by attribute"}
$('[data-test-id="my-id"]')
```

[Read Finders And Filters](browser/finders-and-filters) to learn how to select elements using more advanced techniques. 

[Read W3Schools CSS selectors](https://www.w3schools.com/cssref/css_selectors.asp) to learn all kind of CSS selection techniques.   

# Lazy Element 

When you use `$('css-selector')` you create an instance of `PageElement`. 

`PageElement` represent an element that is present or *will be present* on a web page. It is safe to declare an element
before you open a browser or navigate to the page you need to test.

:include-file: scenarios/ui/basicDeclareFirst.groovy

# Lazy Value 

Consider a simple search page. Enter value, hit enter, see results. 

Here is simple test.

:include-file: scenarios/ui/searchInnerDeclarations.groovy

In the example `$('#results .result').count` represents the number of elements matching the css selector. 
Let's extract it.

:include-file: scenarios/ui/searchOuterDeclarations.groovy
 