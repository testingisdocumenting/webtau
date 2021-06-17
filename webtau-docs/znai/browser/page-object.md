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

To define `PageObject` create a class.

:include-groovy: pages/SearchPage.groovy

# Grouping
 
To make it easier to refer `PageObjects` from different tests combine them in one file

:include-groovy: pages/Pages.groovy

Use static import to have seamless access to all of them

:include-groovy: scenarios/ui/searchWithPages.groovy


