# Synchronization

Many actions in a modern web page are asynchronous. User presses a button and a moment later a result appears.
In modern web pages there is no full page reload and only a portion of a page will be changed. 

If a test will try to assert a value after a user action, chances are assertion will fail since it will take time 
for a result to appear on a page. 

Question: How do users know that their action is done and they can move on?  

# Visible/Hidden Element

One way to deal with asynchronous pages is to wait for a feedback to appear or disappear.

:include-file: doc-artifacts/snippets/wait-sync/waitForAppear.groovy {title: "visible matcher"}

# Enabled/Disabled Element

Disabled input box and buttons can be used as a user feedback as well.

:include-file: doc-artifacts/snippets/wait-sync/waitForEnabled.groovy {title: "enabled matcher"}

# Wait to match

If presence/absence of an element is not important, you can directly to wait for a matcher to match.

:include-file: doc-artifacts/snippets/wait-sync/waitForMatch.groovy {title: "waitTo any matcher"}

Note: any matcher that you can use with `should` and `shouldNot` can be used with `waitTo` and `waitToNot`

# Wait on url

Another cue to use could be a url change after an action.

:include-file: doc-artifacts/snippets/navigation/waitOnUrl.groovy

Note: url exposes other parts that you can [read more about here](browser/navigation#assert-url) 

