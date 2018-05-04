# Test Encapsulation

Robust tests should not depend on implementation details. 
UI has plenty of those:
* UI Elements placement
* Actions

UI test should not depend on any of them.
Move elements placement and available actions outside of UI test.
Multiple tests can then reuse that information. 
And more importantly you will have only one place to change if UI changes.

# Definition

`PageObject` is just a simple class.

:include-groovy: examples/pages/SearchPage.groovy

Usage of a special `action` method is optional. Used to add additional reporting information.
 
# Grouping
 
To make it easier to refer `PageObjects` from different tests combine them in one file

:include-groovy: examples/pages/Pages.groovy

later just static import all of them

:include-groovy: examples/Search.groovy


