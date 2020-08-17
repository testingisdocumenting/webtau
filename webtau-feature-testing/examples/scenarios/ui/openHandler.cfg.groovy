url = "http://localhost:8180"

def userNameKey = 'userName'
browserPageNavigationHandler = { passedUrl, fullUrl, currentUrl ->
    if (browser.localStorage.getItem(userNameKey)) {
        return
    }

    browser.localStorage.setItem(userNameKey, 'LoggedIn User')
    browser.reopen(fullUrl)
}