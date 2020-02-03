# CoRx

[![Release](https://jitpack.io/v/MatrixDev/CoRx.svg)](https://jitpack.io/#MatrixDev/CoRx)

# Overview

This library aims at helping with migrations from RxJava to Flows and otherwise:
- **Flows** - convertion of `Flow` to `Observable` and otherwise
- **Channels** - convertion of `Channel`/`BroadcastChannel` to `Observable` and otherwise
- **Coroutines**
  - convertion of `Job`/`Deferred` to RxJava types (`Completable`, `Observable`, etc.)
  - wait for `Completable`, `Observable`, etc. completion in `susped` function

# Example

Waiting for `Observable` to complete in a `suspend` function without blocking coroutine thread (unlike `Observable.blockingGet()`):

```kotlin
suspend fun doSomeWork() {
  val observable = Observable.just(1, 2, 3)
  val results = observable.await() // <-- Observable to suspended await here
  println(results)
}

-> [1, 2, 3]
```

Simple `Single` to `Flow` convertion:

```kotlin
suspend fun doSomeWork() {
  val single = Single.just(1)

  single.toFlow() // <-- Single to Flow here
    .map { it * 2 }
    .collect { print(it) }
}

-> 2
```

# How to add dependencies?

To use this library in your project just add following lines:

### Step 1

Add JitPack repository in your root `build.gradle` file:

```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

### Step 2

Add actual library and compiler dependencies:

```gradle
dependencies {
    implementation 'com.github.MatrixDev:CoRx:1.0.1'
}
```

More info can be found at https://jitpack.io/#MatrixDev/CoRx

# License

```
MIT License

Copyright (c) 2018 Rostyslav Lesovyi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
