# CDPreferenceHelper

**CDPreferenceHelper** is a lightweight library designed to simplify working with **DataStore** for storing preferences in Android applications. It provides an easy-to-use API to manage preferences using **DataStore** while supporting features like error handling and migration.

![Platform](https://img.shields.io/badge/Platform-Android-green.svg)
![API Level](https://img.shields.io/badge/API-21+-blue.svg)
![Language](https://img.shields.io/badge/Language-Kotlin-orange.svg)

## 🚀 Features

- **DataStore Management**: Leverages Android's DataStore for storing preferences in a more efficient and modern way.
- **Type-Safe**: Supports storing and retrieving various types of data (String, Int, Boolean, etc.) in a type-safe manner.
- **Error Handling**: Provides a mechanism to handle errors gracefully through a customizable callback.
- **Custom Migrations**: Supports custom migrations for DataStore to help with backward compatibility.
- **Corruption Handling**: Optionally adds corruption handling to keep preferences safe.

## 📦 Installation

### Gradle (JitPack)

1. Add it in your `settings.gradle` file at the end of repositories:

    ```gradle
    dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
            google()
            mavenCentral()
            maven(url = "https://jitpack.io")
        }
    }
    ```

2. Add the dependency to your `build.gradle` file (module-level):

    ```gradle
    dependencies {
        implementation 'com.github.YOUR_GITHUB_USERNAME:CDPreferenceHelper:VERSION'
    }
    ```

## 🚀 Usage Examples

### Initialize PreferenceHelper


To use the `PreferenceHelper` with default settings, you can create an instance as follows:

```kotlin
// Create a PreferenceHelper instance using default settings
val preferenceHelper = PreferenceHelper(applicationContext)
```
You can also customize the behavior of PreferenceHelper by providing optional parameters:

```kotlin
val preferenceHelper = PreferenceHelper(
   context = applicationContext,
   preferenceName = "your_preference_name", // Optional: Default is based on package name
   corruptionHandler = ReplaceFileCorruptionHandler { exception:Exception ->
      // Handle corruption here (optional)
   },
   produceMigrations = { context:Context ->
      listOf(
         // Add migrations here if needed (optional)
      )
   },
   onPreferenceErrorOccurred = { exception:Exception ->
      // Handle preference errors here (optional)
   })
```
### Creating Preference Keys

```kotlin
object PreferencesKeys {
   val USER_NAME = preferencesKey<String>("user_name") // Define key for user name
   val USER_AGE = preferencesKey<Int>("user_age") // Define key for user age
}
```
### Saving Preferences

```kotlin
// Save a String value
preferenceHelper.put(PreferencesKeys.USER_NAME, "JohnDoe")

// Save an Int value
preferenceHelper.put(PreferencesKeys.USER_AGE, 30)

```

### Retrieving Preferences

```kotlin
// Retrieve a String value with a default value
val userName: String = preferenceHelper.get(PreferencesKeys.USER_NAME, "Default Name")

// Retrieve an Int value with a default value
val userAge: Int = preferenceHelper.get(PreferencesKeys.USER_AGE, 18)

```
### Removing Preferences

```kotlin
// Remove a single preference
preferenceHelper.remove(PreferencesKeys.USER_NAME)

```

### Clearing All Preferences

```kotlin
// Clear all preferences
preferenceHelper.clearAll()

```

### Accessing DataStore Object Directly

In addition to the methods provided by the `PreferenceHelper`, you can directly access the underlying `DataStore` object using the `getDataStore()` function. This provides full access to the `DataStore` object for advanced use cases, allowing you to interact with the preferences directly if needed.