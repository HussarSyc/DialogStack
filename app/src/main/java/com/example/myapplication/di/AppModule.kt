package com.example.myapplication.di

import com.example.myapplication.viewmodel.MainViewModel
import io.github.dialogstack.DialogStack
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::DialogStack)
}

val viewModelModule = module {
    viewModelOf(::MainViewModel)
}

val appModules = listOf(appModule, viewModelModule)