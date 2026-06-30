# Using and testing the latest SNAPSHOT (excuse the pun) from maven central

Gradle

```
repositories {
    // ...
    maven {
        url "https://central.sonatype.com/repository/maven-snapshots/"
    }
}

dependencies {
    // ...

    // Replace {FRAMEWORK} with you testing framework
    // Replace {X.X.X} with the version number from `/gradle.properties`
    testCompile "io.github.codedabble-dev:java-snapshot-testing-{FRAMEWORK}:{X.X.X}-SNAPSHOT"
}
```
