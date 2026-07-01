package com.example.myapplication.di

import com.example.dialogstack.DialogStack
import com.example.myapplication.viewmodel.viewModelModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::DialogStack)
}

val appModules = listOf(appModule, viewModelModule)