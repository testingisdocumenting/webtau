# Open

To open a page use `browser.open`. Browser will load a page only if the current url doesn't match the passed one. 

:include-file: doc-artifacts/snippets/navigation/open.groovy {commentsType: 'inline'}

Note: relative url will be automatically expanded to the full url based on the [configuration](UI/basic-configuration)

# Reopen

Use `brower.reopen` to force open the page even if the page url already matches the passed one.

:include-file: doc-artifacts/snippets/navigation/reopen.groovy {commentsType: 'inline'}

# Refresh

Use `browser.refresh` to refresh current page.

:include-file: doc-artifacts/snippets/navigation/refresh.groovy {commentsType: 'inline'}

# Restart

Use `browser.restart` to restart a browser and open last opened url.  

:include-file: doc-artifacts/snippets/navigation/restart.groovy

Note: restarting creates a clean instance of a browser. Local storage is going to be reset. 

# Assert URL

Use `browser.url` to assert on or wait for url changes.

:include-file: sampleBrowserPageUrl.txt {title: "sample url"}

:include-groovy: com/twosigma/webtau/browser/page/PageUrlTest.groovy {title: "asserting url parts", entry: "should expose url parts with should", bodyOnly: true}

:include-groovy: com/twosigma/webtau/browser/page/PageUrlTest.groovy {title: "asserting full url", entry: "full part should be optional and default during assertion", bodyOnly: true}

:include-groovy: com/twosigma/webtau/browser/page/PageUrlTest.groovy {title: "waiting on url parts", entry: "should expose url parts with wait", bodyOnly: true}

# Persist URL

Use `browser.saveCurrentUrl` to save url in a local cache and `browser.openSavedUrl` to open a page later.

It can be handy in multipart tests where first part creates an entity and the second part 
updates the created entity. 

:include-file: doc-artifacts/snippets/navigation/saveUrl.groovy

In order to simplify tests development of a second part you can run first part once, save URL,
and iterate on a second part by opening a page using saved URL.   

:include-file: doc-artifacts/snippets/navigation/loadUrl.groovy

Note: url is stored in a local cache file and will survive tests restart.
