# JReleaser Migration Summary

This document summarizes the conversion from manual Maven Central publishing via OSSRH to automated publishing using JReleaser.

## Changes Made

### 1. **build.gradle** 
- Added JReleaser Gradle plugin: `id 'org.jreleaser' version '1.13.1'`
- Applied jreleaser plugin to root project

### 2. **gradle/publishing.gradle**
- **Removed**: Direct Sonatype OSSRH repository configuration
- **Removed**: nexus-staging plugin configuration and usage
- **Added**: Local repository configuration (`build/repos/release`)
- **Updated**: Signing configuration to detect JReleaser environment variables
  - `JRELEASER_GPG_SECRET_KEY`
  - `JRELEASER_GPG_SECRET_KEY_FILE`
  - `JRELEASER_GPG_PASSPHRASE`

### 3. **New File: jreleaser.yml**
Complete JReleaser configuration including:
- Project metadata (name, description, URL, license, authors)
- GPG signing configuration with verification
- Maven Central Portal Publisher API configuration
- Automatic repository closing and release

### 4. **New File: JRELEASER_SETUP.md**
Comprehensive guide covering:
- Prerequisites and credential setup
- Step-by-step release process
- Environment variable reference
- Troubleshooting tips
- JReleaser task reference

### 5. **CONTRIBUTING.md**
- Replaced old manual release instructions
- Added link to JRELEASER_SETUP.md
- Documented new quick release steps

## Key Improvements

| Aspect | Before | After |
|--------|--------|-------|
| **Repository Management** | Manual via Sonatype UI | Automated by JReleaser |
| **Staging** | Manual close/release steps | Auto close/release |
| **Publishing** | Direct OSSRH API | Maven Central Portal API |
| **GPG Signing** | Manual gradle parameters | Environment variables |
| **Release Workflow** | Multi-step manual process | Single `jreleaserFullRelease` command |
| **GitHub Releases** | Manual creation | Automated by JReleaser |

## Migration Path

### Old Workflow
```bash
# Manual staging
./gradlew publish -PossrhUsername=... -PossrhPassword=... -Psign=true

# Manual login to Sonatype UI
# - Locate staging repo
# - Close repo
# - Release repo
# - Wait for sync
```

### New Workflow
```bash
# Automated full release
./gradlew jreleaserFullRelease
```

## Backward Compatibility

- Old gradle properties can still be used if needed
- The `net.researchgate.release` plugin is retained for version management
- Manual publishing with `-PossrhUsername/-PossrhPassword` still works but not recommended

## Environment Setup for Releases

To perform releases, configure these environment variables:

```bash
export JRELEASER_MAVENCENTRAL_USERNAME="your-sonatype-token-username"
export JRELEASER_MAVENCENTRAL_PASSWORD="your-sonatype-token-password"
export JRELEASER_GPG_SECRET_KEY="base64-encoded-gpg-key"
export JRELEASER_GPG_PASSPHRASE="your-gpg-passphrase"
export JRELEASER_GITHUB_TOKEN="github-token-if-creating-releases"  # optional
```

Or use GPG key file:
```bash
export JRELEASER_GPG_SECRET_KEY_FILE="/path/to/gpg/key.gpg"
```

## Testing Before Production Release

1. **Dry Run**:
   ```bash
   ./gradlew jreleaserFullReleaseDryRun
   ```

2. **Validate Config**:
   ```bash
   ./gradlew jreleaserValidate
   ```

3. **View Config**:
   ```bash
   ./gradlew jreleaserConfig
   ```

## Resources

- [JReleaser Official Documentation](https://jreleaser.org/)
- [JReleaser Gradle Plugin Guide](https://jreleaser.org/guide/latest/reference/gradle.html)
- [Maven Central Portal API](https://central.sonatype.org/publishing/publish-maven/)
- [JRELEASER_SETUP.md](./JRELEASER_SETUP.md) - Detailed setup guide

## Next Steps

1. Store Maven Central Portal API credentials securely (e.g., GitHub Secrets)
2. Optionally update CI/CD workflows to use jreleaser
3. Test with a dry-run release: `./gradlew jreleaserFullReleaseDryRun`
4. Perform first production release when ready

## Questions?

See [JRELEASER_SETUP.md](./JRELEASER_SETUP.md) for detailed troubleshooting and additional information.
