# JReleaser Maven Central Setup

This project has been converted to use [JReleaser](https://jreleaser.org/) for automated releases to Maven Central via the Portal Publisher API.

## Overview

JReleaser simplifies the release process by:
- Automating version bumping and git tagging
- Building and signing artifacts
- Publishing to Maven Central using the new Portal API
- Creating GitHub releases with changelog

## Prerequisites

Before performing a release, ensure you have:

1. **Maven Central Portal API Credentials**
   - Set environment variable: `JRELEASER_MAVENCENTRAL_USERNAME` (your Sonatype token username)
   - Set environment variable: `JRELEASER_MAVENCENTRAL_PASSWORD` (your Sonatype token password)
   - Get these from: https://central.sonatype.com/account

2. **GPG Signing Setup**
   - Export your GPG secret key in base64:
     ```bash
     gpg --export-secret-keys <key-id> | base64
     ```
   - Set environment variable: `JRELEASER_GPG_SECRET_KEY` with the base64-encoded key
   - Set environment variable: `JRELEASER_GPG_PASSPHRASE` with your GPG passphrase
   
   OR use a key file:
   ```bash
   export JRELEASER_GPG_SECRET_KEY_FILE=~/.gnupg/secring.gpg
   export JRELEASER_GPG_PASSPHRASE=<your-passphrase>
   ```

3. **GitHub Token** (optional, for creating releases)
   - Set environment variable: `JRELEASER_GITHUB_TOKEN` with a GitHub personal access token

## Release Process

### 1. Prepare for Release
Update version in `gradle.properties`:
```properties
version=X.Y.Z
```

### 2. Build Artifacts
```bash
./gradlew clean build publishToMavenLocal -x test
```

### 3. Full Release Workflow
```bash
# Dry run (recommended first)
./gradlew jreleaserConfig
./gradlew jreleaserFullReleaseDryRun

# Perform actual release
./gradlew jreleaserFullRelease
```

### 4. Manual Step-by-Step Release
If you prefer to control each step:

```bash
# 1. Build and sign artifacts
./gradlew clean build publishToMavenLocal -Psign -PsigningKey="$JRELEASER_GPG_SECRET_KEY" -PsigningPassword="$JRELEASER_GPG_PASSPHRASE"

# 2. Copy artifacts to staging directory
./gradlew :java-snapshot-testing-core:publishToMavenLocal
./gradlew :java-snapshot-testing-junit4:publishToMavenLocal
./gradlew :java-snapshot-testing-junit5:publishToMavenLocal
./gradlew :java-snapshot-testing-plugin-jackson:publishToMavenLocal
./gradlew :java-snapshot-testing-plugin-jackson3:publishToMavenLocal
./gradlew :java-snapshot-testing-spock:publishToMavenLocal

# 3. Upload to Maven Central Portal
./gradlew jreleaserPublish
```

## JReleaser Configuration

The main configuration is in `jreleaser.yml`:

- **project**: Project metadata (name, description, URL, etc.)
- **signing**: GPG signing configuration
- **deploy.maven.mavenCentral**: Maven Central Portal API settings
  - `url`: Portal API endpoint
  - `closeRepository`: Auto-close staging repository
  - `releaseRepository`: Auto-release after publishing

## Environment Variables Reference

| Variable | Description |
|----------|-------------|
| `JRELEASER_MAVENCENTRAL_USERNAME` | Maven Central Portal API username |
| `JRELEASER_MAVENCENTRAL_PASSWORD` | Maven Central Portal API password |
| `JRELEASER_GPG_SECRET_KEY` | Base64-encoded GPG secret key |
| `JRELEASER_GPG_SECRET_KEY_FILE` | Path to GPG secret key file |
| `JRELEASER_GPG_PASSPHRASE` | GPG key passphrase |
| `JRELEASER_GITHUB_TOKEN` | GitHub token for creating releases |
| `JRELEASER_DRY_RUN` | Set to `true` to perform dry run |

## Useful JReleaser Tasks

```bash
# Show current configuration
./gradlew jreleaserConfig

# Validate configuration
./gradlew jreleaserValidate

# Full release (dry run)
./gradlew jreleaserFullReleaseDryRun

# Full release (execute)
./gradlew jreleaserFullRelease

# Just publish to Maven Central
./gradlew jreleaserPublish

# Create GitHub release
./gradlew jreleaserRelease

# Generate changelogs
./gradlew jreleaserChangelog
```

## Troubleshooting

### Publication Fails
- Ensure version in `gradle.properties` is not a SNAPSHOT
- Verify Maven Central credentials are correct
- Check that artifacts are properly signed

### GPG Signing Issues
- Verify GPG secret key is correctly base64 encoded
- Ensure passphrase is correct
- Test GPG locally: `gpg --list-secret-keys`

### Maven Central Portal Issues
- Login to https://central.sonatype.com and verify credentials
- Check portal API documentation: https://central.sonatype.com/publishing/publish-maven/

## Migration from Previous Setup

The previous setup used:
- `net.researchgate.release` for version management
- Direct OSSRH/Sonatype publishing
- Manual staging repository management with `nexus-staging`

The new JReleaser setup:
- Still supports version management (can integrate with `net.researchgate.release`)
- Automates the entire publish-to-Maven-Central flow
- Uses the modern Portal Publisher API
- Simplifies artifact signing and deployment

## Additional Resources

- [JReleaser Documentation](https://jreleaser.org/)
- [Maven Central Portal Publisher API](https://central.sonatype.org/publishing/publish-maven/)
- [JReleaser Gradle Plugin](https://jreleaser.org/guide/latest/reference/gradle.html)
- [Maven Central Requirements](https://central.sonatype.org/publish/requirements/)
