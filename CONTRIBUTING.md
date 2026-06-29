# Contributing

We welcome contributions to this project by both internal and external parties

## How to contribute

1. Fork the repository into your own github account (external contributors) or create a new branch (internal
   contributions)
1. Make your code changes
1. Ensure you commit message is descriptive as it acts as the changelog. Mark any breaking changes with `BREAKING`.
   Include a rectification strategy if you introduce a `BREAKING` change.
1. Ensure `README.md` is updated if needed. All code samples in the README should have corresponding (actual sames) in the various docs/ folders
1. Submit a pull request back to `master` branch (or the branch you are contributing to)
1. Ensure Github Actions build passes
1. Await reviews
1. Once merged into `master` a `SNAPSHOT` build will be available for consumption
   immediately [here](https://oss.sonatype.org/content/repositories/snapshots/io/github/codedabble-dev/). Note that
   snapshots change regularly and cannot be relied upon.
1. Hard Releases will be made once enough features have been added.

## Building

```java
./gradlew spotlessApply shadowJar
```

## Deploying locally and deploying to `.m2/`

```
./gradlew publishToMavenLocal
```

Ensure you add `mavenLocal()` to your consuming project and the dependency verison matches that in your local `.m2`
folder

# Uploading to maven central

The project now uses [JReleaser](https://jreleaser.org/) to automate releases to Maven Central.

See [JRELEASER_SETUP.md](JRELEASER_SETUP.md) for complete setup and release instructions.

### Quick Release Steps

1. Ensure you have Maven Central Portal API credentials:
   - `JRELEASER_MAVENCENTRAL_USERNAME` 
   - `JRELEASER_MAVENCENTRAL_PASSWORD`

2. Setup GPG signing (see JRELEASER_SETUP.md)

3. Update version in `gradle.properties` (remove `-SNAPSHOT`)

4. Run full release:
   ```bash
   ./gradlew jreleaserFullRelease
   ```

Or use step-by-step approach:
```bash
./gradlew clean build publishToMavenLocal -Psign
./gradlew jreleaserPublish
```

JReleaser will automatically:
- Sign all artifacts with GPG
- Publish to Maven Central via Portal API
- Close and release the staging repository
- Create a GitHub release

### Previous Manual Process (No Longer Needed)

The old process involved:
1. Manual staging repository management via Sonatype UI
2. Direct OSSRH publishing
3. Manual close/release steps

All of this is now automated by JReleaser.
