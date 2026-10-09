<div align="center">

# JiLibs-Fabric

**The Fabric mod loader specific implementation of the JiLibs ecosystem.**

![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Frepo.repsy.io%2Fmvn%2Fthementor%2Fjilibs%2Fcom%2Fdynamero%2Fjilibs%2Fjilibs-fabric%2Fmaven-metadata.xml&style=for-the-badge&label=Latest%20Version&labelColor=Black&color=Lime)
[![MC Version](https://img.shields.io/badge/dynamic/regex?url=https%3A%2F%2Frepo.repsy.io%2Fmvn%2Fthementor%2Fjilibs%2Fcom%2Fdynamero%2Fjilibs%2Fjilibs-fabric%2Fmaven-metadata.xml&search=%3Crelease%3E(%5Cd%2B%5C.%5Cd%2B)&replace=%241&label=MC%20Version&color=brightgreen&style=for-the-badge)](https://repo.repsy.io/mvn/thementor/jilibs)
![Static Badge](https://img.shields.io/badge/ACTIVE-8A2BE2?style=for-the-badge)

[![Java](https://img.shields.io/badge/java-%239D8000.svg?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![GitHub license](https://img.shields.io/badge/license-LGPL--3.0-blue.svg?style=for-the-badge)](https://github.com/drkhodakarami/JiLibs-Fabric/blob/HEAD/LICENSE)
[![GitHub issues](https://img.shields.io/github/issues/drkhodakarami/JiLibs-Fabric?style=for-the-badge&color=orange)](https://github.com/drkhodakarami/JiLibs-Fabric/issues)

[![Website](https://img.shields.io/badge/Website-007ACC?style=for-the-badge&logo=googlechrome&logoColor=white)](https://dynamero.com)
[![YouTube](https://img.shields.io/badge/YouTube-%23FF0000.svg?style=for-the-badge&logo=YouTube&logoColor=white)](https://www.youtube.com/@TheMentorCodeLab)
[![Discord](https://img.shields.io/badge/Discord-%235865F2.svg?style=for-the-badge&logo=discord&logoColor=white)](https://discord.gg/pmM4emCbuH)
[![Modrinth](https://img.shields.io/badge/Modrinth-%231BD96A.svg?style=for-the-badge&logo=modrinth&logoColor=black)](https://modrinth.com/user/jiraiyah)

<p align="center">
  <a href="#installation">Installation</a> •
  <a href="#usage">Usage</a> •
  <a href="#repository-info">Repository Info</a> •
  <a href="#license">License</a>
</p>

</div>

---

## Overview

**JiLibs-Fabric** provides Fabric-specific bindings, utilities, and integrations for the JiLibs library ecosystem. It is distributed via Repsy Maven and packaged specifically for consumption by Fabric mods.

---

## Installation

Add the Repsy Maven repository and configure your dependencies inside your Fabric mod's `build.gradle` (or `build.gradle.kts`).

### 1. Add the Repository

Add the JiLibs Maven endpoint to your `repositories` block:

```groovy
repositories {
    mavenCentral()
    // JiLibs Repsy Repository
    maven {
        name = "JiLibs Repsy"
        url = 'https://repo.repsy.io/mvn/thementor/jilibs'
    }
}
```

### 2. Add the Dependency (Include / Jar-in-Jar)
In Fabric development, use Fabric Loom's include configuration alongside modImplementation (or implementation) to bundle the library inside your mod's JAR using Fabric's Jar-in-Jar (JiJ) mechanism:

```Groovy
dependencies {
// Replace ${jilibs_version} with your target version
def jilibs_version = "1.0.0"

    // Add as a mod dependency
    implementation "com.dynamero.jilibs:jilibs-fabric:${jilibs_version}"

    // Bundle directly into your mod jar
    include "com.dynamero.jilibs:jilibs-fabric:${jilibs_version}"
}
```

> Note: If you are using `gradle.properties` for version cataloging, define `jilibs_version = <version>` there and reference it as `implementation "com.dynamero:jilibs-fabric:${project.jilibs_version}"`.

## Fabric Mod Metadata
If you want the Fabric Loader to recognize JiLibs as an embedded or required dependency, define it in your `fabric.mod.json`:

```json
{
  "depends": {
    "fabricloader": ...,
    "minecraft": ...,
    "java": ...,
    "jilibs-fabric": "*"
  },
  "suggests": {
    "jilibs-fabric": "*"
  }
}
```
## Repository Info
| Parameter | Value |
|---|---|
| Repository URL | https://repo.repsy.io/mvn/thementor/jilibs |
| Group ID | com.dynamero (or your configured project group) |
| Artifact ID | jilibs-fabric |
| Distribution | Maven 2 / Repsy |

## Author & Maintainer
- TheMentor – Dynamero
- GitHub: @drkhodakarami


## License
This project is licensed under the GNU Lesser General Public License v3.0 - see the [LICENSE](LICENSE) file for details.