# Special Values

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "use groovy closure as validation", bodyOnly: true}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {entry: "useClosureAsValidation", bodyOnly: true}
```

Values that you access inside validation block are special values of `DataNode` type. 

Note: When you assert them using `should` statement they act as proxies that record every assertion that you do. Assertions
become visible in the generated reports.


# Extracting Values

As you have seen in [CRUD example](HTTP/CRUD) you can return values back from a validation block.

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "can return simple value from get", bodyOnly: true}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {entry: "canReturnSimpleValueFromGet", bodyOnly: true}
```

When you return a value from a validation block, it automatically gets converted to its correspondent primitive. 

Note: WebTau will not be able to track returned value assertion. 
Return values for consequent calls, but not assertion to generate more useful report information.  

# Properties On Lists

:include-json: objectTestResponse.json

If you have a list of objects like `complexList` above, you can access all its children property value with `complexList.k2`.

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "children key shortcut", bodyOnly: true}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {entry: "childrenKeyShortcut", bodyOnly: true}
```

# Path based properties access

Primarily for Java users, WebTau supports the ability to query properties of a `DataNode` via a path instead of chaining
`get(String name)` calls.  For example, to obtain a simple property:

:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {entry: "canQueryNodeByPath", bodyOnly: true}

It is also possible to query arrays, including the ability to query for the Nth element from the end:

:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {entry: "canQuerySpecificListElementByPath", bodyOnly: true}

Similarly to the Groovy example in [Properties On Lists](HTTP/data-node#properties-on-lists), it is possible to access 
all children property values:

:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {entry: "canQueryListByNodePath", bodyOnly: true}

# If-Else Logic

Even though values that you access inside validation block are special values of `DataNode` type, you can still
perform simple `if-else` like logic checks on them. 

In case of Groovy, accessing a value during if-else will mark the value as *accessed* for reporting.

In case of Java, use `.get()` to access underlying value. No marking of the value will during comparison. 

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
  entry: "if-else logic", 
  bodyOnly: true,
  excludeRegexp: "doc-exclude" 
}
  

 Warning: Comparison of complex values is not properly implemented due to current Groovy API implementation details

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
  entry: "ifElseLogic", 
  bodyOnly: true,
  excludeRegexp: "doc-exclude" 
}
```


# Iteration

`DataNode` have convenient methods to iterate over values and make assertions.

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
  entry: "each on simple list",
  bodyOnly: true,
  title: "List of simple values"
}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
  entry: "eachOnSimpleList", 
  bodyOnly: true,
  title: "List of simple values"
}
```

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
  entry: "each on complex list",
  bodyOnly: true, 
  title: "List of complex values"
}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
  entry: "eachOnComplexList", 
  bodyOnly: true,
  title: "List of complex values"
}
```


# Find

`find` to find a single value

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "find on list", bodyOnly: true}


and `findAll` to find all the values matching predicate

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "findAll on list", bodyOnly: true}

Note: While values inside a predicate are normal values, the result of `find` and `findAll` is still `DataNode`

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "find on list of objects", bodyOnly: true}

# Collect

Use `collect` to transform a collection of items

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "transform list", bodyOnly: true}

# Combine

Methods `find` and `collect` can be chained
 
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "findAll, collect, and sum", bodyOnly: true}
