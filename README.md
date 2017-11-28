# Spring Boot Google App Engine

Spring Boot support for Google App Engine Datastore via Objectify and the full-text search API.

#### Note: There are some minor breaking API changes coming in `1.1`
* `LoadRepository.findAll()` will now return a `List<E>` instead of a `Map<Key<E>, Optional<E>>`. 

Maven
```
<dependency>
  <groupId>com.github.n15g</groupId>
  <artifactId>spring-boot-gae</artifactId>
  <version>1.0.0</version>
</dependency>
```

Gradle
```
compile 'com.github.n15g:spring-boot-gae:1.0.0'
```

## Requirements

* Java 8+
* Appengine Standard Java 8+
* Gradle 4+ (if you don't want to use the wrapper)

This library has been designed to work with the new Java8 Appengine Standard Environment. It will not work
when deployed to the Java 7 standard environment.

## Getting Started

### Installing the Library
To install the library to your local maven repository (to use it in other projects), run the following:

```
./gradlew publishToMavenLocal
```

### SearchRepository
Repositories extending`SearchRepository` will automatically index saved entities in the `SearchService` using
the entity's `Key#toWebSafeKey()` as the `@SearchId`.

### Google Search API Limitations

There are some limitations of the the google Search API that impact what can and cannot be indexed or queried
by the `SearchService`:

* Collections are not supported on NUMBER or DATE fields.
* Date/Time fields are stored and queried as DATE only, the time component is truncated.
* Substring matching is not supported.
* GT, GTE, LT, LTE are not supported for STRING/HTML queries.

Not all of the limitations of the Search API are enforced by the framework, if in doubt see the
[Search API documentation](https://cloud.google.com/appengine/docs/standard/java/search/) for specifics.


## Misc

### Release to Central

```
gradle -Prelease uploadArchives closeAndReleaseRepository
```
