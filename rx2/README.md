# Rx2 Preferences

### Download

```groovy
implementation 'com.frybits.reactive-preferences:rx2:0.0.1'
```

### Usage

Create a `Rx2SharedPreferences` instance which wraps a `SharedPreferences`:

```kotlin
val preferences = PreferenceManager.getDefaultSharedPreferences(context)
val rx2SharedPreferences = Rx2SharedPreferences.create(preferences)
```

*Hint: Keep a strong reference on your `Rx2SharedPreferences` instance for as long as you want to observe them to prevent listeners from being GCed.*

Create individual `Rx2Preference` objects:

```kotlin
val username = rx2SharedPreferences.getString("username")
val showWhatsNew = rx2SharedPreferences.getBoolean("show-whats-new", true)
```

Observe changes to individual preferences:

```kotlin
username.asObservable().subscribe { username ->
  Log.d(TAG, "Username: $username")
}
```

Subscribe preferences to streams to store values:

```kotlin
RxCompoundButton.checks(showWhatsNewView)
    .subscribe(showWhatsNew.asConsumer())
```
*(Note: `RxCompoundButton` is from [RxBinding](https://github.com/JakeWharton/RxBinding))*
