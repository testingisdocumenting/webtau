# Data Focused Tests

Webtau simplifies writing REST and UI tests. But if the logic behind REST and UI is written in JVM based language,
webtau can also simplify testing the logic directly. 

Webtau makes tests to be focused on input and output by reducing boilerplate.  

# Simplified Input Preparation

```tabs
Groovy: :include-groovy: com/twosigma/webtau/data/PeopleManagementGroovyTest.groovy {entry: "diversified teams should have various levels and time at company"}
Java: :include-java: com/twosigma/webtau/data/PeopleManagementTest.java {entry: "diversifiedTeamsShouldHaveVariousLevelsAndTimeAtCompany"}
```

# Simplified Output Validation 

Complex data and assertions are first class citizens.

```tabs
Groovy: :include-groovy: com/twosigma/webtau/data/PeopleDaoGroovyTest.groovy {entry: "provides access to new joiners"}
Java:

:include-java: com/twosigma/webtau/data/PeopleDaoTest.java {entry: "providesAccessToNewJoiners"}

 Note: The examples above assumes `import static com.twosigma.webtau.WebTauCore.*` or `import static com.twosigma.webtau.WebTauDsl.*`.
```

For more `TableData` features, check [reference page](reference/table-data)