# Spring Data GCP

Support for Google Cloud Platform's Datastore via Objectify and the full-text search service running in an
AppEngine standard environment.

## Requirements

* Java 8+
* Gradle 4+ (if you don't want to use the wrapper)
* Appengine Standard Java 8+

This library has been designed to work with the new Java8 Appengine Standard Environment. It will not work
when deployed to the Java 7 standard environment.

## Limitations of the Google Search API

* Collections are not supported on NUMBER or DATE fields.
* Date/Time fields are stored and queried as DATE only, the time component is truncated.

## Getting Started

### Installing the Library
To install the library to your local maven repository (to use it in other projects), run the following:

```
./gradlew install
```

### Updating the Library Version

Update the `version` property in `gradle.properties`.

### TODO
* SearchService.
* `ExceptionTranslationPostProcessor`.
* `@Transactional` support.
* `QueryLookupStrategy`.