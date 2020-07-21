# Data Focused Tests

Webtau simplifies writing REST and UI tests. But if the logic behind REST and UI is written in JVM based language,
webtau can also simplify testing the logic directly. 

Webtau makes tests to be focused on input and output by reducing boilerplate.  

# Simplified Input Preparation

```tabs
Groovy: :include-groovy: org/testingisdocumenting/webtau/data/PeopleManagementGroovyTest.groovy {entry: "diversified teams should have various levels and time at company"}
Java: :include-java: org/testingisdocumenting/webtau/data/PeopleManagementTest.java {entry: "diversifiedTeamsShouldHaveVariousLevelsAndTimeAtCompany"}
```

# Simplified Output Validation 

Complex data and assertions are first class citizens.

```tabs
Groovy: :include-groovy: org/testingisdocumenting/webtau/data/PeopleDaoGroovyTest.groovy {entry: "provides access to new joiners"}
Java:

:include-java: org/testingisdocumenting/webtau/data/PeopleDaoTest.java {entry: "providesAccessToNewJoiners"}

 Note: The examples above assumes `import static org.testingisdocumenting.webtau.WebTauCore.*` or `import static org.testingisdocumenting.webtau.WebTauDsl.*`.
```

For more `TableData` features, check [reference page](reference/table-data)