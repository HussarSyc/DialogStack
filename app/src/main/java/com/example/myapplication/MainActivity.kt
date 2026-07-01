package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.dialogstack.DialogItem
import com.example.dialogstack.DialogStack
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.MainViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: MainViewModel = koinViewModel()
                    val currentDialog by viewModel.dialogState.collectAsState()

                    currentDialog?.let { entry ->
                        when (entry) {
                            is AlertDialogItem -> {
                                AlertDialog(
                                    onDismissRequest = { viewModel.popDialog() },
                                    title = { Text(entry.title) },
                                    text = { Text(entry.message) },
                                    confirmButton = {
                                        Button(onClick = { viewModel.popDialog() }) {
                                            Text("确定")
                                        }
                                    }
                                )
                            }
                        }
                    }

                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                        onShowAlert = {
                            viewModel.pushDialog(
                                AlertDialogItem(
                                    title = "普通提示",
                                    message = "这是一个普通优先级的对话框",
                                    priority = DialogStack.DEFAULT_PRIORITY
                                )
                            )
                        },
                        onShowHighPriority = {
                            viewModel.pushDialog(
                                AlertDialogItem(
                                    title = "高优先级",
                                    message = "这是一个高优先级的对话框，会优先显示",
                                    priority = DialogStack.HIGH_PRIORITY
                                )
                            )
                        },
                        onClear = {
                            viewModel.clearDialogs()
                        }
                    )
                }
            }
        }
    }
}

data class AlertDialogItem(
    val title: String,
    val message: String,
    override val priority: Int
) : DialogItem

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
    onShowAlert: () -> Unit = {},
    onShowHighPriority: () -> Unit = {},
    onClear: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Hello $name!")
        Button(onClick = onShowAlert, modifier = Modifier.padding(top = 16.dp)) {
            Text("显示普通对话框")
        }
        Button(onClick = onShowHighPriority, modifier = Modifier.padding(top = 8.dp)) {
            Text("显示高优先级对话框")
        }
        Button(onClick = onClear, modifier = Modifier.padding(top = 8.dp)) {
            Text("清除所有对话框")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}