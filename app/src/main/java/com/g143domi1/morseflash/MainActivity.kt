package com.g143domi1.morseflash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.g143domi1.morseflash.ui.theme.MorseFlashTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.selection.SelectionContainer
import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.Job

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                MorseFlashTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color= MaterialTheme.colorScheme.background
                        ) {
                    Main_func()
                } }

            }
        }
    }


@Composable
fun Main_func() {
    var flashJob by remember { mutableStateOf<Job?>(null)}
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context= LocalContext.current
    val cameraManager=context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    var text by remember { mutableStateOf("") }
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())


        ) {
            Text(text= stringResource(R.string.app_name), textAlign= TextAlign.Center, modifier= Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
                fontSize=32.sp )
            Text(text= stringResource(R.string.licence_note), textAlign= TextAlign.Center, modifier= Modifier.fillMaxWidth())
            Text(text=stringResource(R.string.licence), textAlign= TextAlign.Center, modifier= Modifier.fillMaxWidth() )
            Text(text=stringResource(R.string.beforetextfield), textAlign= TextAlign.Center,  modifier= Modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .padding(top=16.dp)
            )
            TextField(
                value=text,
                onValueChange = {text = it},
                label= {Text("Enter text: ")},
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 16.dp)
                    .height(256.dp)
            )
            //here will be morse text
            val morseText = buildString {
            for (character in text)
                if (character == 'A' || character == 'a') {
                    append(".- ")
                } else if (character == 'B' || character == 'b') {
                    append("-... ")
                } else if (character == 'C' || character == 'c') {
                    append("-.-. ")
                } else if (character == 'D' || character == 'd') {
                    append("-.. ")
                } else if (character == 'E' || character == 'e') {
                    append(". ")
                } else if (character == 'F' || character == 'f') {
                    append("..-. ")
                } else if (character == 'G' || character == 'g') {
                    append("--. ")
                } else if (character == 'H' || character == 'h') {
                    append(".... ")
                } else if (character == 'I' || character == 'i') {
                    append(".. ")
                } else if (character == 'J' || character == 'j') {
                    append(".--- ")
                } else if (character == 'K' || character == 'k') {
                    append("-.- ")
                } else if (character == 'L' || character == 'l') {
                    append(".-.. ")
                } else if (character == 'M' || character == 'm') {
                    append("-- ")
                } else if (character == 'N' || character == 'n') {
                    append("-. ")
                } else if (character == 'O' || character == 'o') {
                    append("--- ")
                } else if (character == 'P' || character == 'p') {
                    append(".--. ")
                } else if (character == 'Q' || character == 'q') {
                    append("--.- ")
                } else if (character == 'R' || character == 'r') {
                    append(".-. ")
                } else if (character == 'S' || character == 's') {
                    append("... ")
                } else if (character == 'T' || character == 't') {
                    append("- ")
                } else if (character == 'U' || character == 'u') {
                    append("..- ")
                } else if (character == 'V' || character == 'v') {
                    append("...- ")
                } else if (character == 'W' || character == 'w') {
                    append(".-- ")
                } else if (character == 'X' || character == 'x') {
                    append("-..- ")
                } else if (character == 'Y' || character == 'y') {
                    append("-.-- ")
                } else if (character == 'Z' || character == 'z') {
                    append("--.. ")
                } else if (character == ' ') {
                    append(" ")
                }}
            SelectionContainer {
                Text(text = stringResource(R.string.morse_code) + morseText, fontSize = 32.sp)
            }
            Row (modifier= Modifier
                .fillMaxWidth()
                //.padding(horizontal = 100.dp)
                //.padding(top=50.dp)
            ) {
                Button(
                    onClick = {
                        println("Play button clicked")
                        val cameraId = cameraManager.cameraIdList.firstOrNull { id ->
                            cameraManager.getCameraCharacteristics(id)
                                .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
                        }
                        if (cameraId != null) {
                            flashJob = scope.launch {
                                for (character_m in morseText)
                                    if (character_m == '.') {
                                        cameraManager.setTorchMode(cameraId, true)
                                        delay(100)
                                        cameraManager.setTorchMode(cameraId, false)
                                    } else if (character_m == '-') {
                                        cameraManager.setTorchMode(cameraId, true)
                                        delay(300)
                                        cameraManager.setTorchMode(cameraId, false)
                                    } else if (character_m == ' ') {
                                        delay(700)
                                    }

                            }
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Could not find a camera/flashlight!")
                            }
                        }
                    }
                ) {
                    Text("Play")
                }
                Button(
                    onClick = {
                        flashJob?.cancel()
                        flashJob=null
                        println("Stop button clicked.")
                        val cameraId = cameraManager.cameraIdList.firstOrNull { id ->
                            cameraManager.getCameraCharacteristics(id)
                                .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
                        }
                        if (cameraId != null) {
                            cameraManager.setTorchMode(cameraId, false)
                        }

                    }
                ) { Text("Stop") }


            Button(
                onClick = {
                    println("Clear button clicked.")
                    text=""
                }
            ) {Text("Clear")}

        }}}}



