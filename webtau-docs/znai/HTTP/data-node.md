# Special Values

Values that you access inside the `http` call validation block are special values of `DataNode` type.
`DataNode` has methods to assert and traverse the response.

All assertions made on `DataNode` are tracked and are available as part of the generated report. 

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
  entry: "use groovy closure as validation",
  bodyOnly: true
}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
  entry: "useClosureAsValidation", 
  bodyOnly: true
}
```

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
Return values for consequent calls only. Make assertions on `DataNode` instance to generate useful report information such as data coverage.  

# Properties On Lists

If you have a list of objects like `complexList` below, you can access all its children property value with `complexList.k2`.

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
  entry: "children key shortcut", 
  bodyOnly: true,
  excludeRegexp: "doc-exclude"
}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {entry: "childrenKeyShortcut", bodyOnly: true}
```

:include-json: objectTestResponse.json {title: "response", pathsFile: "doc-artifacts/properties-on-list/paths.json"}

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

:include-json: addressResponse.json {title: "response"}

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

:include-json: objectTestResponse.json {title: "response", highlight: "amount"}

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
  entry: "each on simple list",
  bodyOnly: true,
  title: "List of simple values"
}

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
  entry: "each on complex list",
  bodyOnly: true, 
  title: "List of complex values"
}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
  entry: "eachOnSimpleList", 
  bodyOnly: true,
  title: "List of simple values"
}

:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
  entry: "eachOnComplexList", 
  bodyOnly: true,
  title: "List of complex values"
}
```

# Find

Use `find` to find a `DataNode` based on a provided predicate

:include-json: objectTestResponse.json {title: "response", pathsFile: "doc-artifacts/find-on-list-and-assert/paths.json"}

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
  title: "find and assert on value",
  entry: "find on list and assert",
  bodyOnly: true 
}

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
  title: "find and return a complex value",
  entry: "find on list and return",
  excludeRegexp: "doc-exclude",
  bodyOnly: true 
}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
  title: "find and assert on value",
  entry: "findOnListAndAssert", 
  bodyOnly: true
}
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
  title: "find and return a complex value",
  entry: "findOnListAndReturn", 
  excludeRegexp: "doc-exclude",
  bodyOnly: true
}
```

Note: WebTau will not track assertion on returned values and it will not show in reports and
will not participate in data coverage. Use returned values for further test logic.


# Find All 

Use `findAll` to find a list of `DataNode`s based on a provided predicate

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
  entry: "findAll on complex list",
  bodyOnly: true 
}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
  title: "find and assert on value",
  entry: "findAllOnComplexList", 
  bodyOnly: true
}
```


Note: The result of `findAll` is of type `DataNode` and you can leverage [Properties On Lists](#properties-on-lists)

# Collect

Use `collect` to transform a collection of items

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "transform list", bodyOnly: true}

# Combine

Methods `find` and `collect` can be chained
 
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "findAll, collect, and sum", bodyOnly: true}
