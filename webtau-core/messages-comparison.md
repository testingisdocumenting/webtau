# Equals failures
## Pure Junit
### Code
```java
assertEquals(1, 2);
```
### Result
```
java.lang.AssertionError: 
Expected :1
Actual   :2
```
## Hamcrest
### Code
```java
assertThat(1, equalTo(2));
```
### Result
```
java.lang.AssertionError: 
Expected: <2>
     but: was <1>
Expected :<2>
     
Actual   :<1>
```
## Google Truth
### Code
```java
assertThat(1).isEqualTo(2);
```
### Result
```
expected: 2
but was : 1
Expected :2
Actual   :1
```
## AssertJ
### Code
```java
assertThat(1).isEqualTo(2);
```
### Result
```
org.junit.ComparisonFailure: 
Expected :2
Actual   :1
```
## Webtau
### Code
```java
actual(1).should(equal(2));
```
### Result
```
java.lang.AssertionError: 
doesn't equal 2
mismatches:

[value]:   actual: 1 <java.lang.Integer>
         expected: 2 <java.lang.Integer>
```

# Not equals failures
## Pure Junit
### Code
```java
assertNotEquals(1, 1);
```
### Result
```
java.lang.AssertionError: Values should be different. Actual: 1
```
## Hamcrest
### Code
```java
assertThat(1, not(equalTo(1)));
```
### Result
```
java.lang.AssertionError: 
Expected: not <1>
     but: was <1>
Expected :not <1>
     
Actual   :<1>
```
## Google Truth
### Code
```java
Truth.assertThat(1).isNotEqualTo(1);
```
### Result
```
expected not to be: 1
```
## AssertJ
### Code
```java
Assertions.assertThat(1).isNotEqualTo(1);
```
### Result
```
java.lang.AssertionError: 
Expecting:
 <1>
not to be equal to:
 <1>
```
## Webtau
### Code
```java
actual(1).shouldNot(equal(1));
```
### Result
```
java.lang.AssertionError: 
equals 1, but shouldn't
mismatches:

[value]:   actual: 1 <java.lang.Integer>
         expected: not 1 <java.lang.Integer>
```

# Less than failures
## Pure Junit
NOT SUPPORTED
## Hamcrest
### Code
```java
assertThat(1, lessThan(1));
```
### Result
```
java.lang.AssertionError: 
Expected: a value less than <1>
     but: <1> was equal to <1>
```
## Google Truth
### Code
```java
Truth.assertThat(1).isLessThan(1);
```
### Result
```
expected to be less than: 1
but was                 : 1
```
## AssertJ
### Code
```java
Assertions.assertThat(1).isLessThan(1);
```
### Result
```
java.lang.AssertionError: 
Expecting:
 <1>
to be less than:
 <1> 
```
## Webtau
### Code
```java
actual(1).should(lessThan(1));
```
### Result
```
java.lang.AssertionError: 
greater then or equal to 1
mismatches:

[value]:   actual: 1 <java.lang.Integer>
         expected: less than 1 <java.lang.Integer>
```

# Not less than failures
## Pure Junit
NOT SUPPORTED
## Hamcrest
### Code
```java
assertThat(1, not(lessThan(2)));
```
### Result
```
java.lang.AssertionError: 
Expected: not a value less than <2>
     but: was <1>
Expected :not a value less than <2>
     
Actual   :<1>
```
## Google Truth
NOT SUPPORTED
## AssertJ
NOT SUPPORTED
## Webtau
### Code
```java
actual(1).shouldNot(lessThan(2));
```
### Result
```
java.lang.AssertionError: 
[value] is less than 2, but should be greater or equal to
mismatches:

[value]:   actual: 1 <java.lang.Integer>
         expected: greater than or equal to 2 <java.lang.Integer>

```
