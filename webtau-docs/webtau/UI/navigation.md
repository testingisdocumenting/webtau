# Open

To open a page use `browser.open`. Browser will load a page only if the current url doesn't match the passed one. 

:include-file: doc-artifacts/snippets/navigation/open.groovy {commentsType: 'inline'}

Note: relative url will be automatically expanded to the full url based on the [configuration](UI/basic-configuration)

# Re-Open

Use `brower.reopen` to force open the page even if the page url already matches the passed one.

:include-file: doc-artifacts/snippets/navigation/reopen.groovy {commentsType: 'inline'}

# Re-Fresh

Use `browser.refresh` to refresh current page.

:include-file: doc-artifacts/snippets/navigation/refresh.groovy {commentsType: 'inline'}
