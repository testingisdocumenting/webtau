# Access Local Storage

To access local storage use `browser.localStorage`.

Lets consider a simple web page that displays a value from a local storage.

:include-file: doc-artifacts/snippets/local-storage/body-only.html {title: "Sample web page"}

:include-file: scenarios/ui/localStorage.groovy {title: "Setting storage after opening page"}

:include-java: org/testingisdocumenting/webtau/browser/BrowserLocalStorage.java {
    entry: ["getItem", "setItem", "removeItem", "clear", "size"],
    title: "Local Storage methods", 
    signatureOnly: true}
