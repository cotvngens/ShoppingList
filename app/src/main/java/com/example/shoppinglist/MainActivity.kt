package com.example.shoppinglist

import android.graphics.BlurMaskFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppinglist.ui.theme.DarkBackground
import com.example.shoppinglist.ui.theme.GlowBlueDark
import com.example.shoppinglist.ui.theme.GlowBlueLight
import com.example.shoppinglist.ui.theme.GlowPinkDark
import com.example.shoppinglist.ui.theme.GlowPinkLight
import com.example.shoppinglist.ui.theme.GlowPurpleDark
import com.example.shoppinglist.ui.theme.GlowPurpleLight
import com.example.shoppinglist.ui.theme.LightBackground
import com.example.shoppinglist.ui.theme.ShoppingListTheme
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

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

data class ShoppingItem(
    val name: String,
    val checked: Boolean
)

val googleSansFamily = FontFamily(
    Font(R.font.googlesans_regular, FontWeight.Normal),
    Font(R.font.googlesans_bold, FontWeight.Bold)
)

@Composable
fun ShoppingList(
    modifier: Modifier = Modifier,
    isDark: Boolean = isSystemInDarkTheme()
) {
    val background = if (isDark) DarkBackground else LightBackground

    val glowPurple = if (isDark) GlowPurpleDark else GlowPurpleLight
    val glowBlue = if (isDark) GlowBlueDark else GlowBlueLight
    val glowPink = if (isDark) GlowPinkDark else GlowPinkLight

//    val items = remember {
//        mutableStateListOf<Pair<String, Boolean>>()
//    }

    val items = remember {
        mutableStateListOf(
            ShoppingItem("🥛 Milk", false),
            ShoppingItem("🍞 Bread", true),
            ShoppingItem("🥚 Eggs", false),
            ShoppingItem("🍓 Strawberries", false),
            ShoppingItem("☕ Coffee", false)
        )
    }

    var newItem by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        // Верхний фиолетовый
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {

            fun glow(
                center: Offset,
                color: Color,
                circleRadius: Float,
                blurRadius: Float
            ) {
                drawIntoCanvas { canvas ->

                    val paint = Paint()

                    paint.color = color

                    paint.asFrameworkPaint().maskFilter =
                        BlurMaskFilter(
                            blurRadius,
                            BlurMaskFilter.Blur.NORMAL
                        )

                    canvas.drawCircle(
                        center,
                        circleRadius,
                        paint
                    )
                }
            }

            glow(
                center = Offset(
                    size.width * 0.15f,
                    size.height * -0.1f
                ),
                color = glowPurple.copy(alpha = 0.55f),
                circleRadius = 800f,
                blurRadius = 260f
            )

            glow(
                center = Offset(
                    size.width * 0.90f,
                    size.height * 0.65f
                ),
                color = glowBlue.copy(alpha = 0.55f),
                circleRadius = 400f,
                blurRadius = 240f
            )

            glow(
                center = Offset(
                    size.width * 0.15f,
                    size.height * 0.95f
                ),
                color = glowPink.copy(alpha = 0.55f),
                circleRadius = 260f,
                blurRadius = 240f
            )
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp),
        ) {
            Text(
                text = "SHOPPING LIST",
                fontSize = 28.sp,
                fontFamily = googleSansFamily,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                TextField(
                    value = newItem,
                    onValueChange = { newItem = it },
                    placeholder = { Text("Enter the product...") },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(bottomStart = 12.dp, topStart = 12.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = if (isDark) Color.White.copy(alpha = 0.07f)
                        else Color.White.copy(alpha = 0.7f),
                        focusedContainerColor = if (isDark) Color.White.copy(alpha = 0.1f)
                        else Color.White.copy(alpha = 0.9f),
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    )
                )
                Button(
                    onClick = {
                        if (newItem.isNotBlank()) {
                            items.add(ShoppingItem(newItem, false))
                            newItem = ""
                        }
                    },
                    modifier = Modifier.height(56.dp),
                    shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                ) {
                    Text(text = "ADD", fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier.weight(1f) // занимает всё оставшееся место
            ) {
                items(
                    items = items,
                    key = { it.name }
                ) { item ->
                    SwipeToDeleteItem(
                        item = item,
                        onToggle = {
                            val index = items.indexOf(item)
                            items[index] = item.copy(checked = !item.checked)
                        },
                        onDelete = {
                            items.remove(item)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SwipeToDeleteItem(
    item: ShoppingItem,
    onDelete: () -> Unit,
    onToggle: () -> Unit,
) {
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val maxSwipe = -300f
    val triggerSwipe = -150f

    Box(modifier = Modifier.fillMaxWidth()) {

        val progress = (offsetX.value / maxSwipe).coerceIn(0f, 1f)

        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            tint = Color.White.copy(alpha = progress),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 24.dp)
        )

        Row(
            modifier = Modifier
                .offset { androidx.compose.ui.unit.IntOffset(offsetX.value.toInt(), 0) }
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (isSystemInDarkTheme())
                        Color.White.copy(alpha = 0.05f)
                    else
                        Color.White.copy(alpha = 0.6f)
                )
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()

                            scope.launch {
                                val newValue =
                                    (offsetX.value + dragAmount)
                                        .coerceIn(maxSwipe, 0f)

                                offsetX.snapTo(newValue)
                            }
                        },
                        onDragEnd = {
                            if (offsetX.value < triggerSwipe) {
                                scope.launch {
                                    offsetX.animateTo(
                                        targetValue = -1000f,
                                        animationSpec = tween(250)
                                    )
                                    onDelete()
                                }
                            } else {
                                scope.launch {
                                    offsetX.animateTo(0f, tween(200))
                                }
                            }
                        }
                    )
                }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.name,
                modifier = Modifier.weight(1f),
                textDecoration =
                    if (item.checked) TextDecoration.LineThrough
                    else TextDecoration.None,
                color = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = if (item.checked) 0.4f else 1f
                )
            )
        }
    }
}


@Preview(showBackground = true, name = "Light Theme")
@Composable
fun ShoppingListLightPreview() {
    ShoppingListTheme(darkTheme = false) {
        ShoppingList(isDark = false)
    }
}

@Preview(showBackground = true, name = "Dark Theme")
@Composable
fun ShoppingListDarkPreview() {
    ShoppingListTheme(darkTheme = true) {
        ShoppingList(isDark = true)
    }
}