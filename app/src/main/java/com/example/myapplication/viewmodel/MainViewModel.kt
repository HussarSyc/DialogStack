package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import io.github.dialogstack.DialogItem
import io.github.dialogstack.DialogStack
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

class MainViewModel(
    private val dialogStack: DialogStack
) : ViewModel() {

    val dialogState = dialogStack.dialogState

    fun pushDialog(dialog: DialogItem) {
        dialogStack.push(dialog)
    }

    fun popDialog() {
        dialogStack.pop()
    }

    fun clearDialogs() {
        dialogStack.clear()
    }
}

