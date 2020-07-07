# Universal Set Value

Use `setValue` on a declared page element to set its value.
It will work on all the standard input types out of the box. 
 
Define all the input fields inside a page object. In combination with universal `setValue` it will make your tests robust. 

Given a html snippet 

:include-file: src/test/resources/forms.html {title: "HTML form"}

Page object can be defined as 

:include-file: examples/pages/FormPage.groovy {title: "Form page object"}

# Default Input

:include-file: doc-artifacts/snippets/forms/inputDefault.groovy {title: "set value to input"}


# Date Input

:include-file: doc-artifacts/snippets/forms/inputDate.groovy {title: "set value to date input"}


# Select

:include-file: doc-artifacts/snippets/forms/selectOptions.groovy {title: "set value to select"}
 
# CheckBox

:include-file: doc-artifacts/snippets/forms/checkBox.groovy {title: "set value to checkbox"}
 
# Universal Assert

Form element value can be asserted the same way as any regular element. 
Underlying value will be extracted based on the element type

:include-file: doc-artifacts/snippets/forms/validation.groovy {title: "universal assert"}

# Custom Form Elements

One of the benefits of universal `set` and `assert` is that your test is focused on the data and not implementation details.
But what if you decided to use a custom component to enter the data? 

To hide implementation details from your test you should define a custom input handler for your UI component.

Let's consider a form component that you can start interacting with only after you clicked it. And after the value
is entered, the input box disappears again.

:include-file: doc-artifacts/snippets/special-forms/form-custom-element.html {title: "Custom input"}

Our test should still be written in terms of data entering and validation.

:include-file: doc-artifacts/snippets/special-forms/customGetSet.groovy {title: "set value to custom component"}

In order to achieve this we need to register a custom handler.  

:include-file: scenarios/ui/specialForms.cfg {title: "webtau.cfg", lang: "groovy"}

:include-file: formHandlers/CustomInput.groovy {title: "Custom input handler"}



