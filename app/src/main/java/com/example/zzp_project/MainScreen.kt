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
import androidx.compose.ui.focus.onFocusChanged
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
    var searchResults by remember { mutableStateOf(listOf<String>()) }


    val density = LocalDensity.current
    val context = LocalContext.current

    val keys = maps.keys.toList()              // lista pięter w kolejności mapy
    var selectedMap by rememberSaveable {
        mutableStateOf("Pietro_1")
    }

    val currentIndex = keys.indexOf(selectedMap)
    var searchNumber by rememberSaveable { mutableStateOf("") }

    var search by remember { mutableStateOf(false) }
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

                        if(search==true){
                            val grenadePositions = getGrenadePositions(searchNumber)
                            grenadePositions.forEach { (position, route) ->
                                selectedMap=route

                                val offsetX = with(density) { (position.first * imageSize.width).toDp() }
                                val offsetY = with(density) { (position.second * imageSize.height).toDp() }
                                Box(
                                    modifier = Modifier
                                        .offset(offsetX, offsetY)
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(Color.Red)
                                        .clickable {  },
                                    contentAlignment = Alignment.Center
                                ) {


                                }
                                search==false
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

                    Column {
                        // --- POLE TEKSTOWE ---
                        TextField(
                            value = searchNumber,
                            onValueChange = { input ->
                                searchNumber = input
                                searchResults = if (input.isNotEmpty()) {
                                    ALL_WEITI_ROOMS.filter { it.contains(input, ignoreCase = true) }
                                } else {
                                    emptyList()
                                }
                            },
                            placeholder = { Text("Wpisz numer sali...", color = Color.Gray) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()

                                .height(75.dp)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(10.dp))
                            ,
                            // Poprawny kod dla OutlinedTextField
                            colors = OutlinedTextFieldDefaults.colors(
                                // Kolor tła pola tekstowego
                                focusedContainerColor = Color(0xFF2E2E2E),
                                unfocusedContainerColor = Color(0xFF2E2E2E),
                                disabledContainerColor = Color(0xFF2E2E2E),

                                // Kolor tekstu w środku
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,

                                // Kolor kursora
                                cursorColor = Color.White,

                                // Kolor obramowania (indicator)
                                focusedBorderColor = Color.Transparent,  // Ustawiasz kolor obramowania, gdy pole jest aktywne
                                unfocusedBorderColor = Color.Transparent // Ustawiasz kolor obramowania, gdy pole nie jest aktywne
                            )

                        )
                        if (searchResults.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 200.dp)
                                .background(Color(0xFF2E2E2E))
                                .padding(8.dp)
                        ) {
                            items(searchResults.size) { index ->
                                val room = searchResults[index]
                                Text(
                                    text = room,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            searchNumber = room // wpisanie do pola tekstowego
                                            searchResults = emptyList() // ukrycie listy
                                            search = true
                                        }
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
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







