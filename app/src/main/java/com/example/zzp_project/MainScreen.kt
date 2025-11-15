package com.example.zzp_project

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



import kotlin.math.roundToInt

@SuppressLint("UseOfNonLambdaOffsetOverload", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen() {
    val maps = mapOf(
        "Piwnica" to R.drawable.piwnica,
        "Parter" to R.drawable.parter_00,
        "Pietro_1" to R.drawable.pietro_01,
        "Pietro_2" to R.drawable.pietro_02,
        "Pietro_3" to R.drawable.pietro_03,
        "Pietro_4" to R.drawable.pietro_04,
        "Pietro_5" to R.drawable.pietro_05,
        "Dach" to R.drawable.dach_06


    )


    val density = LocalDensity.current
    val context = LocalContext.current

    val keys = maps.keys.toList()              // lista pięter w kolejności mapy
    var selectedMap by rememberSaveable {
        mutableStateOf("Pietro_1")
    }

    val currentIndex = keys.indexOf(selectedMap)
    var searchNumber by rememberSaveable { mutableStateOf("") }


    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            Column(modifier = Modifier
                .background(Color.White)
                .navigationBarsPadding()
            ) {


            }
        }
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Button(onClick = { navController.popBackStack() }) {
            //    Text(text = "Back")
            //}
            item {  Spacer(modifier = Modifier.height(75.dp*1f))}
            item{var imageSize by remember { mutableStateOf(IntSize.Zero) }


                Box(
                    modifier = Modifier
                        .clipToBounds()





                ) {Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offsetX,
                            translationY = offsetY
                        )
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                scale *= zoom // Zmieniamy skalę
                                offsetX += pan.x // Przesuwanie w osi X
                                offsetY += pan.y // Przesuwanie w osi Y
                            }

                        }
                ) {


                    Image(
                        painter = painterResource(id = maps[selectedMap] ?: R.drawable.ancient_radar),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)


                            .height(450.dp)

                            .onGloballyPositioned { layoutCoordinates ->
                                imageSize = layoutCoordinates.size

                            }
                    )


                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(420.dp)
                            .clipToBounds()
                    ) {

                        val grenadePositions = getGrenadePositions(selectedMap)
                        grenadePositions.forEach { (position, route) ->

                            val offsetX = with(density) { (position.first * imageSize.width).toDp() }
                            val offsetY = with(density) { (position.second * imageSize.height).toDp() }
                            Box(
                                modifier = Modifier
                                    .offset(offsetX, offsetY)
                                    .size(15.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red)
                                    .clickable {  },
                                contentAlignment = Alignment.Center
                            ) {


                            }
                        }
                    }
                }


                    Box(modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(5.dp)

                    ){
                        Row {
                            Button(
                                colors = ButtonDefaults.buttonColors(Color(0x80222222)),
                                shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp),
                                onClick = { scale = (scale + 0.25f).coerceAtMost(3f) }
                            )
                            {
                                Text(text = "+", fontSize = 25.sp)

                            }

                            Divider(
                                modifier = Modifier

                                    .width(2.dp)
                                    .align(Alignment.CenterVertically),
                                color = Color.Transparent
                            )
                            Button(
                                colors = ButtonDefaults.buttonColors(Color(0x80222222)),
                                shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp),
                                onClick = { scale = (scale - 0.25f).coerceAtLeast(1f) }

                            ){
                                Text(text = "-", fontSize = 25.sp)
                            }
                        }
                    }
                    Box(modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(5.dp)

                    ){
                        Row {
                            Button(
                                colors = ButtonDefaults.buttonColors(Color(0x80222222)),
                                shape = RoundedCornerShape(10.dp),
                                onClick = { scale = 1f
                                    offsetX= 0F
                                    offsetY= 0F
                                }
                            )
                            {
                                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)

                            }



                        }
                    }
                }}


            item {  Spacer(modifier = Modifier.height(20.dp))}


            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // --- POLE TEKSTOWE ---
                    TextField(
                        value = searchNumber,
                        onValueChange = { searchNumber = it },
                        placeholder = { Text("Wpisz numer sali...", color = Color.Gray) },
                        singleLine = true,

                        modifier = Modifier
                            .weight(1f)
                            .height(55.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // --- PRZYCISK Z IKONĄ SZUKANIA ---
                    Button(
                        onClick = {

                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2E2E2E)
                        ),
                        modifier = Modifier.size(55.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Szukaj",
                            tint = Color.White,
                            modifier = Modifier.scale(3f)
                        )
                    }
                }

            }

            item {  Spacer(modifier = Modifier.height(20.dp))}




            item{
                Row(
                    modifier = Modifier.padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // --- PRZYCISK MINUS ---
                    Button(
                        onClick = { if (currentIndex > 0) {
                            selectedMap = keys[currentIndex - 1]
                        }

                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xff222222)),
                        shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp),
                        modifier = Modifier.padding(horizontal = 2.dp)
                    ) {
                        Text("-", fontSize = 32.sp)
                    }

                    // --- NAZWA PIĘTRA ---
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF000000), RoundedCornerShape(1.dp))
                            .padding(horizontal = 20.dp, vertical = 15.dp)
                    ) {
                        Text(
                            text = selectedMap,
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    }

                    // --- PRZYCISK PLUS ---
                    Button(
                        onClick = { if (currentIndex < keys.size - 1) {
                            selectedMap = keys[currentIndex + 1]
                        }

                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xff222222)),
                        shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp),
                        modifier = Modifier.padding(horizontal = 2.dp)
                    ) {
                        Text("+", fontSize = 32.sp)
                    }
                }
            }

            item {  Spacer(modifier = Modifier.height(120.dp))}

        }

    }

}



fun getGrenadePositions(map: String): List<Pair<Pair<Float, Float>, String>> {
    return when ("$map") {

        "Ancient-F" -> listOf(
            (0.108f to 0.077f) to "grenade_detail/Ancient/CT/Flash1",

            )

        "Ancient-S" -> listOf(
            (0.44f to 0.835f) to "grenade_detail/Ancient/T/Smoke1",
            (0.44f to 0.802f) to "grenade_detail/Ancient/T/Smoke2",

            (0.41f to 0.844f) to "grenade_detail/Ancient/T/Smoke3",
            (0.425f to 0.885f) to "grenade_detail/Ancient/T/Smoke4",

            (0.497f to 0.83f) to "grenade_detail/Ancient/T/Smoke5",
            (0.496f to 0.8f) to "grenade_detail/Ancient/T/Smoke6",

            (0.31f to 0.67f) to "grenade_detail/Ancient/T/Smoke7",
            (0.345f to 0.69f) to "grenade_detail/Ancient/T/Smoke8",
            (0.325f to 0.72f) to "grenade_detail/Ancient/T/Smoke9",
            (0.33f to 0.625f) to "grenade_detail/Ancient/T/Smoke10",

            (0.157f to 0.607f) to "grenade_detail/Ancient/T/Smoke11",
            (0.195f to 0.57f) to "grenade_detail/Ancient/T/Smoke12",

            (0.18f to 0.355f) to "grenade_detail/Ancient/T/Smoke13",

            (0.512f to 0.58f) to "grenade_detail/Ancient/T/Smoke14",
            (0.395f to 0.573f) to "grenade_detail/Ancient/T/Smoke15",
            (0.375f to 0.46f) to "grenade_detail/Ancient/T/Smoke16",

            (0.714f to 0.743f) to "grenade_detail/Ancient/T/Smoke17",
            (0.806f to 0.705f) to "grenade_detail/Ancient/T/Smoke18",
            (0.78f to 0.687f) to "grenade_detail/Ancient/T/Smoke19",

            (0.77f to 0.605f) to "grenade_detail/Ancient/T/Smoke20",
            (0.662f to 0.587f) to "grenade_detail/Ancient/T/Smoke21",

            )

        "Ancient-M" -> listOf(
            (0.142f to 0.088f) to "grenade_detail/Ancient/CT/Molotov1",

            )

        "Mirage-M" -> listOf(
            (0.170f to 0.180f) to "grenade_detail/Mirage/CT/Molotov1",

            )

        "Mirage-S" -> listOf(
            (0.270f to 0.380f) to "grenade_detail/Mirage/T/Smoke1",

            )
        else -> emptyList()
    }
}





