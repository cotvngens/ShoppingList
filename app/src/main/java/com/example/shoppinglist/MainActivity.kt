package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppinglist.ui.theme.ShoppingListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingListTheme {
                Scaffold { innerPadding ->
                    ShoppingList(
                        modifier = Modifier
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ShoppingList(modifier: Modifier = Modifier) {

    val items = remember {
        mutableStateListOf<Pair<String, Boolean>>()
    }

    var newItem by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(text = "Shopping List:", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            TextField(
                value = newItem,
                onValueChange = { newItem = it },
                placeholder = { Text("Enter the product") },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(bottomStart = 10.dp, topStart = 10.dp)
            )
            Button(
                onClick = {
                    if (newItem.isNotBlank()) {
                        items.add(Pair(newItem, false))
                        newItem = ""
                    }
                },
                modifier = Modifier.height(56.dp),
                shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)
            ) {
                Text(text = "ADD")
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        items.forEachIndexed { index, item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.first,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            items[index] = Pair(item.first, !item.second)
                        },
                    textDecoration = if (item.second) TextDecoration.LineThrough
                    else TextDecoration.None
                )
                Button(
                    onClick = { items.removeAt(index) }
                ) {
                    Text("X")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ShoppingListPreview() {
    ShoppingList()
}