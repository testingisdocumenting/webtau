# Lazy Element 

When you use `$('.css')` you create instance of `PageElement`. 

`PageElement` represent an element that is present or *will be present* on a web page. It is safe to declare an element
before you open a browser or navigate to the page you need to test.

:include-file: examples/scenarios/ui/basicDeclareFirst.groovy

# Lazy Value 

Consider a simple search page. Enter value, hit enter, see results. 

Here is simple test.

:include-file: examples/scenarios/ui/searchInnerDeclarations.groovy

In the example `$('#results .result').count` represents the number of elements matching the css selector. 
Let's extract it.

:include-file: examples/scenarios/ui/searchOuterDeclarations.groovy
 