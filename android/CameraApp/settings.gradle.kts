pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        flatDir {
            dirs("library") // Directory containing AAR file
        }
    }
}

rootProject.name = "Camera App"
include(":app")

// Unlink camera-sdk on release
// Use for build ../gradlew clean assembleRelease
// Copy aar file to /library/ folder
// Include aar file in sample app -> implementation(files("library/camera-sdk-release.aar"))
include(":camera-sdk")
