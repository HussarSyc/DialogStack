package com.example.myapplication.dialog

import io.github.dialogstack.DialogItem

data class FirstDialog(
    override val priority: Int
) : DialogItem

data class SecondDialog(
    override val priority: Int
) : DialogItem

data class ThirdDialog(
    override val priority: Int
) : DialogItem