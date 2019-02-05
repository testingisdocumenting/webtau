# Special Values

:include-groovy: com/twosigma/webtau/http/HttpGroovyTest.groovy {entry: "use groovy closure as validation", bodyOnly: true}

Values that you access inside validation block are special values of `DataNode` type. When you assert them using `should` statement
they act as proxies that record every assertion that you do. 


# Extracting Values

As you have seen in [CRUD example](REST/CRUD) you can return values back from a validation block.

:include-groovy: com/twosigma/webtau/http/HttpGroovyTest.groovy {entry: "can return simple value from get", bodyOnly: true}

When you return a value from a validation block, it automatically gets converted to its correspondent primitive. 

Note: asserting that value after returning will not track and associated assertions with the call anymore. Use it only
to get values required for consequent test calls.  

# Properties On Lists

:include-json: objectTestResponse.json

If you have a list of objects like `complexList` above, you can access all its children property value with `complexList.k2`.

:include-groovy: com/twosigma/webtau/http/HttpGroovyTest.groovy {entry: "groovy children key shortcut", bodyOnly: true}

# Each

Special values inside assertion block have convenient methods

`each` to iterate over a list

:include-groovy: com/twosigma/webtau/http/HttpGroovyTest.groovy {entry: "groovy each on simple list", bodyOnly: true, title: "List of simple values"}

:include-groovy: com/twosigma/webtau/http/HttpGroovyTest.groovy {entry: "groovy each on complex list", bodyOnly: true, title: "List of complex values"}


# Find

`find` to find a single value

:include-groovy: com/twosigma/webtau/http/HttpGroovyTest.groovy {entry: "groovy find on list", bodyOnly: true}


and `findAll` to find all the values matching predicate

:include-groovy: com/twosigma/webtau/http/HttpGroovyTest.groovy {entry: "groovy findAll on list", bodyOnly: true}

Note: While values inside a predicate are normal values, the result of `find` and `findAll` is still `DataNode`

:include-groovy: com/twosigma/webtau/http/HttpGroovyTest.groovy {entry: "groovy find on list of objects", bodyOnly: true}

# Collect

Use `collect` to transform a collection of items

:include-groovy: com/twosigma/webtau/http/HttpGroovyTest.groovy {entry: "groovy transform list", bodyOnly: true}

# Combine

Methods `find` and `collect` can be chained
 
:include-groovy: com/twosigma/webtau/http/HttpGroovyTest.groovy {entry: "groovy findAll, collect, and sum", bodyOnly: true}
