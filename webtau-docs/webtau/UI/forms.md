# Universal Set Value

Use `setValue` on a declared page element to set its value.
It will work on all the standard input types out of the box. 
 
Define all the input fields inside a page object. In combination with universal `setValue` it will make your tests robust. 

Given a html snippet 

:include-file: src/test/resources/forms.html {title: "HTML form"}

Page object can be defined as 

:include-file: examples/pages/FormPage.groovy {title: "Form page object"}


# Text Input

:include-file: doc-artifacts/snippets/forms/input.groovy {title: "set value to input"}


# Select

:include-file: doc-artifacts/snippets/forms/selectOptions.groovy {title: "set value to select"}
 
# Universal Assert

Form element value can be asserted the same way as any regular element. 
Underlying value will be extracted based on the element type

:include-file: doc-artifacts/snippets/forms/validation.groovy {title: "universal assert"}
