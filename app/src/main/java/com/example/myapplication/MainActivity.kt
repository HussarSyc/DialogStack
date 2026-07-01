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
import com.example.myapplication.dialog.FirstDialog
import com.example.myapplication.dialog.SecondDialog
import com.example.myapplication.dialog.ThirdDialog
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.MainViewModel
import io.github.dialogstack.DialogStack
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
                            is FirstDialog -> {
                                AlertDialog(
                                    onDismissRequest = { viewModel.popDialog() },
                                    title = { Text("第一个对话框") },
                                    text = { Text("这是第一个对话框，默认优先级") },
                                    confirmButton = {
                                        Button(onClick = { viewModel.popDialog() }) {
                                            Text("确定")
                                        }
                                    }
                                )
                            }
                            is SecondDialog -> {
                                AlertDialog(
                                    onDismissRequest = { viewModel.popDialog() },
                                    title = { Text("第二个对话框") },
                                    text = { Text("这是第二个对话框，高优先级") },
                                    confirmButton = {
                                        Button(onClick = { viewModel.popDialog() }) {
                                            Text("确认")
                                        }
                                    }
                                )
                            }
                            is ThirdDialog -> {
                                AlertDialog(
                                    onDismissRequest = { viewModel.popDialog() },
                                    title = { Text("第三个对话框") },
                                    text = { Text("这是第三个对话框，低优先级") },
                                    confirmButton = {
                                        Button(onClick = { viewModel.popDialog() }) {
                                            Text("好的")
                                        }
                                    }
                                )
                            }
                        }
                    }

                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                        onFirstDialog = {
                            viewModel.pushDialog(
                                FirstDialog(
                                    priority = DialogStack.DEFAULT_PRIORITY
                                )
                            )
                        },
                        onSecondDialog = {
                            viewModel.pushDialog(
                                SecondDialog(
                                    priority = DialogStack.HIGH_PRIORITY
                                )
                            )
                        },
                        onThirdDialog = {
                            viewModel.pushDialog(
                                ThirdDialog(
                                    priority = DialogStack.LOW_PRIORITY
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

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
    onFirstDialog: () -> Unit = {},
    onSecondDialog: () -> Unit = {},
    onThirdDialog: () -> Unit = {},
    onClear: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Hello $name!")
        Button(onClick = onFirstDialog, modifier = Modifier.padding(top = 16.dp)) {
            Text("显示第一个对话框")
        }
        Button(onClick = onSecondDialog, modifier = Modifier.padding(top = 8.dp)) {
            Text("显示第二个对话框")
        }
        Button(onClick = onThirdDialog, modifier = Modifier.padding(top = 8.dp)) {
            Text("显示第三个对话框")
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