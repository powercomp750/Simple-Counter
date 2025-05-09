package comp.basic.simplecounter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import comp.basic.simplecounter.ui.theme.winInfo
import comp.basic.simplecounter.ui.theme.winLayout
import comp.basic.simplecounter.ui.theme.SimpleCounterTheme
import java.lang.Integer.parseInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleCounterTheme() {
                val windowInfo = winLayout()
                Surface(color = colorScheme.background, modifier = Modifier.fillMaxSize()) {
                    if (windowInfo.screenWidthInfo is winInfo.WindowType.Compact) {
                        MainPageVertical()
                    } else {
                        MainPageHorizontal()
                    }
                }

            }
        }
    }

    private var firstPress: Long = 0
    override fun onBackPressed() {
        if (firstPress+1000>System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            Toast.makeText(baseContext,"Press Back twice to EXIT",Toast.LENGTH_SHORT).show()
        }
        firstPress = System.currentTimeMillis()
    }
}

@SuppressLint("ServiceCast", "NewApi")
fun triggerVibration(context: Context) {
    val vibrator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    if (vibrator.hasVibrator()) {
        val vibrationEffect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE) // 100ms vibration
        vibrator.vibrate(vibrationEffect)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPageHorizontal() {
    val keyboardController = LocalSoftwareKeyboardController.current
    var context = LocalContext.current
    var n1 by rememberSaveable { mutableIntStateOf(0) }
    var n2 by rememberSaveable { mutableStateOf("") }
    if (n1<0) {
        n1 = 0
        Toast.makeText(context,"Value can't be a negative", Toast.LENGTH_SHORT).show()
    }

    if (n1 > 999999999) {
        Toast.makeText(context,"Counter has reached max limit!", Toast.LENGTH_SHORT).show()
        n1 = 999999999
    }
    val max = 10
    val maxValue = 999999999

    Column {
        Column(Modifier.fillMaxWidth().padding(bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly) {
            Text("Simple Counter", fontSize = 30.sp, modifier = Modifier.padding(top = 10.dp))
            Text(text = n1.toString(), fontSize = 60.sp, modifier = Modifier.padding(top = 20.dp))
            Row(Modifier.padding()) {
                DefaultBtn("Increment",{n1++;triggerVibration(context) },true,Modifier.padding(end = 200.dp).size(height = 45.dp, width = 200.dp))
                DefaultBtn("Decrement",{n1--; triggerVibration(context)},n1>0,Modifier.size(height = 45.dp, width = 200.dp))
            }
        }
        Row(Modifier.padding(top = 45.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
            , verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.padding()) {
                OutlinedTextField(value = n2, onValueChange = {
                    if (it.length < max) {
                        n2 = it
                    }

                }, label = { Text("Enter your custom value") }, keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFFF14A00),
                        unfocusedBorderColor = Color(0xFFF14A00),
                        unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
                        focusedLabelColor = Color(0xFFF14A00),
                        cursorColor = MaterialTheme.colorScheme.secondary,
                        focusedTextColor = MaterialTheme.colorScheme.secondary,
                    ),)

                Row(Modifier.padding(top = 20.dp, bottom = 20.dp), horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically) {
                    DefaultBtn("Increase",{
                        n2.toIntOrNull()?.let {
                            val newValue = n1 + it
                            if (newValue <= maxValue) {
                                n1 = newValue + n2.toInt()
                                triggerVibration(context)
                            } else {
                                Toast.makeText(context, "Counter has reached max limit!", Toast.LENGTH_SHORT).show()
                            }
                        }

                    },n2>0.toString(),Modifier.padding(start = 15.dp, end = 40.dp))
                    DefaultBtn("Decrease",{n1-= parseInt(n2) ;triggerVibration(context)},n2>0.toString() && n1>0)
                }
            }
            Column(
                Modifier.padding(start = 60.dp, end = 100.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.size(height = 100.dp, width = 1.5.dp).background(Color(0xFF4C585B)))

            }
            Column(
                Modifier.padding(end = 50.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                DefaultBtn("  Reset  ",{n1=0;n2 = "0"},true,Modifier.padding(bottom = 15.dp).size(width = 150.dp, height = 45.dp))
                DefaultBtn("  EXIT  ",{
                    if (context is android.app.Activity) {
                        context.finish()
                    }},true,Modifier.size(width = 150.dp, height = 40.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPageVertical() {
    val keyboardController = LocalSoftwareKeyboardController.current
    val maxValue = 999999999
    var context = LocalContext.current
    var n1 by rememberSaveable { mutableIntStateOf(0) }
    var n2 by rememberSaveable { mutableStateOf("") }
    if (n1<0) {
        n1 = 0
        Toast.makeText(context,"Value can't be a negative", Toast.LENGTH_SHORT).show()
    }
    if (n1 > 999999999) {
        Toast.makeText(context,"Counter has reached max limit!", Toast.LENGTH_SHORT).show()
        n1 = 999999999
    }
    val max = 10
    Column(
        Modifier
            .fillMaxWidth().padding(top = 150.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        keyboardController?.hide()
                    }
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly

    ) {
        Text("Simple Counter", fontSize = 30.sp)
        Text(text = n1.toString(), fontSize = 60.sp, modifier = Modifier.padding(20.dp))
        Row(Modifier.padding(all = 20.dp)) {
            DefaultBtn("Increment",{n1++;triggerVibration(context) },true,Modifier.padding(end = 25.dp))
            DefaultBtn("Decrement",{n1--; triggerVibration(context)},n1>0)
        }
        Row(Modifier.fillMaxWidth().padding(top = 30.dp, bottom = 30.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.size(width = 150.dp, height = 1.5.dp).background(Color(0xFF4C585B)))
            Text("OR")
            Spacer(Modifier.size(width = 150.dp, height = 1.5.dp).background(Color(0xFF4C585B)))
        }
        OutlinedTextField(value = n2, onValueChange = {
            if (it.length < max) {
                n2 = it
            }

        }, label = { Text("Enter your custom value") }, keyboardOptions =
        KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFFF14A00),
                unfocusedBorderColor = Color(0xFFF14A00),
                unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
                focusedLabelColor = Color(0xFFF14A00),
                cursorColor = MaterialTheme.colorScheme.secondary,
                focusedTextColor = MaterialTheme.colorScheme.secondary,
            ),

            )
        Row(Modifier.padding(30.dp)) {
            DefaultBtn("Increase",{
                n2.toIntOrNull()?.let {
                    val newValue = n1 + it
                    if (newValue <= maxValue) {
                        n1 = newValue + n2.toInt()
                        triggerVibration(context)
                    } else {
                        Toast.makeText(context, "Counter has reached max limit!", Toast.LENGTH_SHORT).show()
                    }
                }
            },n2>0.toString(),Modifier.padding(end = 25.dp))
            DefaultBtn("Decrease",{n1-= parseInt(n2) ;triggerVibration(context)},n2>0.toString() && n1>0)
        }

        Row(Modifier.padding(top = 20.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            DefaultBtn("  Reset  ",{n1=0;n2 = "0"},true,Modifier.size(width = 150.dp, height = 40.dp).padding())
            DefaultBtn("  EXIT  ",{
                if (context is android.app.Activity) {
                    context.finish()
                }},true,Modifier.size(width = 150.dp, height = 40.dp))
        }
    }
}

@Composable
fun DefaultBtn(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Button(modifier = modifier,
        onClick = onClick,
        elevation = ButtonDefaults.elevatedButtonElevation(
            focusedElevation = 20.dp,
            pressedElevation = 30.dp,
            hoveredElevation = 10.dp),
        enabled = enabled,
        shape = RoundedCornerShape(topStart = 15.dp, bottomEnd = 10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorScheme.primary,
            contentColor = Color.White,
            disabledContentColor = Color.LightGray,
            disabledContainerColor = Color(0xFFE07B39)
        )

    ) {
        Text(text)
    }
}

@Preview(showBackground = true, device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun MyPreview() {
    MainPageHorizontal()
}