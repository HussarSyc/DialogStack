# DialogStack

A lightweight, priority-based dialog management library for Android. Manage dialogs with priorities, ensuring high-priority dialogs always show first.

[![](https://jitpack.io/v/dialogstack/dialogstack.svg)](https://jitpack.io/#dialogstack/dialogstack)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Features

- **Priority-based Dialog Management**: High-priority dialogs are displayed first
- **LIFO for Same Priority**: Last pushed dialog is shown first among same priorities
- **Thread Safe**: Uses single-threaded coroutine for concurrent access safety
- **StateFlow Integration**: Observe dialog state changes reactively
- **Minimal Dependencies**: Only depends on kotlinx-coroutines-core

## Important Note

**DialogStack should NOT be used as a singleton.** Each component should create its own instance to avoid memory leaks and ensure proper lifecycle management.

```kotlin
// ✅ Correct - Register as factory with Koin
val appModule = module {
    factoryOf(::DialogStack) // 每次注入创建新实例
}

// ✅ Correct - Create instance directly
class MyViewModel(
    private val dialogStack: DialogStack // 通过 factory 注入
) : ViewModel()

// ❌ Wrong - Singleton usage causes memory leaks
val appModule = module {
    singleOf(::DialogStack) // 不要使用单例!
}
```

## Installation

### Gradle

Add JitPack to your root `build.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}
```

Add the dependency to your module `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.dialogstack:dialogstack:1.0.0")
}
```

## Usage

### 1. Define Dialog Items

Implement the `DialogItem` interface for your dialog types:

```kotlin
import io.github.dialogstack.DialogItem
import io.github.dialogstack.DialogStack

data class AlertDialog(
    override val priority: Int,
    val title: String,
    val message: String
) : DialogItem

data class ConfirmDialog(
    override val priority: Int,
    val title: String,
    val message: String,
    val onConfirm: () -> Unit
) : DialogItem
```

### 2. Use DialogStack

```kotlin
val dialogStack = DialogStack()

// Push dialogs
dialogStack.push(AlertDialog(
    priority = DialogStack.DEFAULT_PRIORITY,
    title = "Info",
    message = "This is an info dialog"
))

dialogStack.push(ConfirmDialog(
    priority = DialogStack.HIGH_PRIORITY,
    title = "Important",
    message = "This dialog has higher priority",
    onConfirm = { /* Handle confirm */ }
))

// Pop the current dialog
dialogStack.pop()

// Clear all dialogs
dialogStack.clear()
```

### 3. Observe Dialog State

```kotlin
// Observe dialog state in Compose
val currentDialog by dialogStack.dialogState.collectAsState()

currentDialog?.let { dialog ->
    when (dialog) {
        is AlertDialog -> AlertDialog(
            title = { Text(dialog.title) },
            text = { Text(dialog.message) },
            onDismissRequest = { dialogStack.pop() }
        )
        is ConfirmDialog -> ConfirmDialog(
            title = { Text(dialog.title) },
            text = { Text(dialog.message) },
            onConfirm = {
                dialog.onConfirm()
                dialogStack.pop()
            },
            onDismissRequest = { dialogStack.pop() }
        )
    }
}
```

### 4. Priority Constants

```kotlin
DialogStack.HIGH_PRIORITY   // 100
DialogStack.DEFAULT_PRIORITY // 50
DialogStack.LOW_PRIORITY    // 10
```

## Architecture

```
┌─────────────────────────────────────────────────────┐
│                    Screen Layer                     │
│  - Observe dialogState via StateFlow                │
│  - Render dialog based on DialogItem type          │
└─────────────────────────────────────────────────────┘
                        │
                        ▼
┌─────────────────────────────────────────────────────┐
│                   DialogStack                       │
│  - push(dialog: DialogItem)                        │
│  - pop()                                           │
│  - clear()                                         │
│  - dialogState: StateFlow<DialogItem?>             │
└─────────────────────────────────────────────────────┘
                        │
                        ▼
┌─────────────────────────────────────────────────────┐
│              PriorityQueue (Internal)               │
│  - Higher priority first                           │
│  - LIFO for same priority (timestamp-based)        │
└─────────────────────────────────────────────────────┘
```

## Thread Safety

DialogStack uses `Dispatchers.Default.limitedParallelism(1)` to ensure all operations are executed on a single thread, preventing race conditions when multiple coroutines push/pop dialogs concurrently.

## API Reference

| Method | Description |
|--------|-------------|
| `push(dialog: DialogItem)` | Add a dialog to the stack |
| `pop()` | Remove and return the top dialog |
| `clear()` | Remove all dialogs from the stack |
| `isEmpty(): Boolean` | Check if the stack is empty |
| `size(): Int` | Get the number of dialogs in the stack |
| `dialogState: StateFlow<DialogItem?>` | StateFlow to observe current dialog |

## License

```
MIT License

Copyright (c) 2026 DialogStack

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