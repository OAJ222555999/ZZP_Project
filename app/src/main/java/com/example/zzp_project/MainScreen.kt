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
        "Ancient" to R.drawable.ancient_radar,
        "Anubis" to R.drawable.anubis_radar,
        "Dust2" to R.drawable.dust2_radar,
        "Inferno" to R.drawable.inferno_radar,
        "Mirage" to R.drawable.mirage_radar,
        "Nuke" to R.drawable.nuke_radar,
        "Train" to R.drawable.train_radar
    )

    val grenadeIcons = mapOf(
        "F" to R.drawable.flash,
        "S" to R.drawable.smoke,
        "N" to R.drawable.nade,
        "M" to R.drawable.molotov
    )
    val density = LocalDensity.current
    val context = LocalContext.current

    var selectedMap by rememberSaveable { mutableStateOf("Ancient") }
    var selectedTeam by rememberSaveable { mutableStateOf("T") }
    var selectedThrowType by rememberSaveable { mutableStateOf("S") }

    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    Scaffold(
        containerColor = Color.Black,
        bottomBar = {
            Column(modifier = Modifier
                .background(Color.Black)
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

                        val grenadePositions = getGrenadePositions(selectedMap, selectedTeam, selectedThrowType)
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

                                Image(
                                    painter = painterResource(id = grenadeIcons[selectedThrowType] ?: R.drawable.flash),
                                    contentDescription = "Grenade",
                                    modifier = Modifier.size(30.dp)
                                )
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

            item{Row(modifier = Modifier.width(150.dp)) {
                Button(
                    onClick = { selectedTeam = "CT" },
                    shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTeam == "CT") Color(0xFFd88b37) else Color(0xff222222)
                    ),
                    modifier = Modifier.weight(1f) // Każdy przycisk zajmuje równą część
                ) { Text(text = "CT") }

                Button(
                    onClick = { selectedTeam = "T" },
                    shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTeam == "T") Color(0xFFd88b37) else Color(0xff222222)
                    ),
                    modifier = Modifier.weight(1f)
                ) { Text(text = "T") }
            }}

            // Przyciski dla granatów

            item{Row(modifier = Modifier.padding(25.dp)) { // Zajmują więcej miejsca niż CT/T
                listOf("F", "S", "M", "N").forEach { throwType ->
                    Button(
                        onClick = { selectedThrowType = throwType },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedThrowType == throwType) Color(0xFFd88b37) else Color(0xff222222)
                        ),
                        shape = when (throwType) {
                            "F" -> RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)
                            "N" -> RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)
                            else -> RoundedCornerShape(0.dp)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.dp)
                        ,

                        ) {
                        Image(
                            painter = painterResource(id = grenadeIcons[throwType] ?: R.drawable.flash),
                            contentDescription = "Grenade",

                            modifier = Modifier
                                .scale(1.5f)
                                .height(30.dp)
                        )
                    }
                }
            }}

            item{LazyRow (Modifier.padding(5.dp)){
                maps.keys.forEach { mapName ->
                    item {
                        Button(
                            onClick = { selectedMap = mapName },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedMap == mapName) Color(0xFFd88b37) else Color(0xff222222)
                            ),
                            shape = if (mapName == "Ancient") RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)
                            else if (mapName == "Train") RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)
                            else RoundedCornerShape(0.dp),
                            modifier = Modifier.padding(horizontal = 1.dp)
                        ) {
                            val drawableId = context.resources.getIdentifier(
                                "${mapName.lowercase()}_logo", "drawable", context.packageName
                            )

                            if (drawableId != 0) {
                                Image(
                                    painter = painterResource(id = drawableId),
                                    contentDescription = null,
                                    modifier = Modifier.size(50.dp)
                                )
                            }
                        }
                    }
                }
            }}
            item {  Spacer(modifier = Modifier.height(120.dp))}

        }

    }

}



fun getGrenadePositions(map: String, team: String, throwType: String): List<Pair<Pair<Float, Float>, String>> {
    return when ("$map-$team-$throwType") {

        "Ancient-CT-F" -> listOf(
            (0.108f to 0.077f) to "grenade_detail/Ancient/CT/Flash1",

            )
        "Ancient-T-F" -> listOf(
            (0.064f to 0.151f) to "grenade_detail/Ancient/T/Flash1",

            )
        "Ancient-T-S" -> listOf(
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
        "Ancient-CT-S" -> listOf(
            (0.51f to 0.08f) to "grenade_detail/Ancient/CT/Smoke1",
            (0.545f to 0.17f) to "grenade_detail/Ancient/CT/Smoke2",
            (0.769f to 0.227f) to "grenade_detail/Ancient/CT/Smoke3",

            )
        "Ancient-CT-M" -> listOf(
            (0.142f to 0.088f) to "grenade_detail/Ancient/CT/Molotov1",

            )
        "Ancient-T-M" -> listOf(
            (0.047f to 0.109f) to "grenade_detail/Ancient/T/Molotov1",

            )
        "Mirage-CT-M" -> listOf(
            (0.170f to 0.180f) to "grenade_detail/Mirage/CT/Molotov1",

            )
        "Mirage-T-M" -> listOf(
            (0.670f to 0.580f) to "grenade_detail/Mirage/T/Molotov1",

            )
        "Mirage-T-S" -> listOf(
            (0.270f to 0.380f) to "grenade_detail/Mirage/T/Smoke1",

            )
        else -> emptyList()
    }
}





