# Cached Value

When you develop tests, you don't have to restart the whole flow from the beginning.

Imagine you have a multistep test that includes running Command Line tool and then validating REST API response, and 
then opening a browser to assert UI values.

Cache long steps results like created entities `ids` to speed up tests development.

In the example below, you create first scenario that runs a heavy command line tool that generates an id. 
We then cache the value and you can write a second scenario and keep re-running it, without the need to re-run the first one.

:include-file: scenarios/cache/cachedValue.groovy {
  title: "cached value",
  surroundedBy: "example",
  commentsType: "inline"
}

Note: Use [Selective Run](groovy-standalone-runner/selective-run) or [REPL mode](REPL/test-runs) 
to run one scenario at a time.

# Cached Value With Expiration

Use get value with expiration time for scenarios like auth token 

:include-file: scenarios/cache/cachedExpirationValue.groovy {
  title: "cached value with expiration",
  surroundedBy: "example",
  commentsType: "inline"
}

# FS Path type

[File System module](utilities/file-system) return values of type `Path`. WebTau stores them as `String`.  
To retrieve as `Path` use `:identifier: getAsPath() {validationPath: "scenarios/cache/cachedPathValue.groovy"}`

:include-file: scenarios/cache/cachedPathValue.groovy {
  title: "cached path value",
  surroundedBy: "example",
  commentsType: "inline"
}

# Presence Check

Use `:identifier: exists {validationPath: "scenarios/cache/cachedValue.groovy"}` inside utilities methods 
to check if a cache value was set. 

:include-file: scenarios/cache/cachedValue.groovy {
  title: "cached value",
  surroundedBy: "exists-example"
}

Avoid: using cache check inside test methods to keep them clean
 
