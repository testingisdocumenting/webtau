# Special Values

:include-groovy: com/twosigma/webtau/http/HttpExtensionsTest.groovy {entry: "use groovy closure as validation", bodyOnly: true}

Values that you access inside validation block are special values of `DataNode` type. When you assert them using `should` statement
they act as proxies that record every assertion you do. 


# Extracting Values

As you have seen in [CRUD example](REST/CRUD) you can return values back from a validation block.

:include-groovy: com/twosigma/webtau/http/HttpExtensionsTest.groovy {entry: "can return simple value from get", bodyOnly: true}

When you return a value from a validation block, it automatically gets converted to its correspondent primitive. 

Note: asserting that value after returning will not track and associated assertions with the call anymore. Use it only
to get values required for consequent test calls.  


# Find

:include-json: objectTestResponse.json

Special values inside assertion block have convenient methods

`find` to find a single value

:include-groovy: com/twosigma/webtau/http/HttpExtensionsTest.groovy {entry: "groovy find on list", bodyOnly: true}


and `findAll` to find all the values matching predicate

:include-groovy: com/twosigma/webtau/http/HttpExtensionsTest.groovy {entry: "groovy findAll on list", bodyOnly: true}

Note: While values inside a predicate are normal values, the result of `find` and `findAll` is still `DataNode`

:include-groovy: com/twosigma/webtau/http/HttpExtensionsTest.groovy {entry: "groovy find on list of objects", bodyOnly: true}

# Collect

Use `collect` to transform a collection of items

:include-groovy: com/twosigma/webtau/http/HttpExtensionsTest.groovy {entry: "groovy transform list", bodyOnly: true}
