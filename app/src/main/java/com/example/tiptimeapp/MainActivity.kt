package com.example.tiptimeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiptimeapp.ui.theme.TipTimeAppTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  setContent {
   TipTimeAppTheme {
    // A surface container using the 'background' color from the theme
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
     TipTimeScreen();
    }
   }
  }
 }
}

@Composable
fun TipTimeScreen() {
 var amountInput by remember { mutableStateOf("") };
 var tipInput by remember { mutableStateOf("") };

 val amount = amountInput.toDoubleOrNull() ?: 0.0;
 val tipPercent = tipInput.toDoubleOrNull() ?: 0.0;


 val focusManager = LocalFocusManager.current;

 var roundUp by remember { mutableStateOf(false) };

 val tip = calculateTip(amount, tipPercent, roundUp );

 Column(
  modifier = Modifier
   .fillMaxSize()
   .padding(32.dp),
  verticalArrangement = Arrangement.spacedBy(8.dp)
 ) {
  Text(
   text = stringResource(id = R.string.calculate_tip),
   fontSize = 24.sp,
   modifier = Modifier.align(Alignment.CenterHorizontally),
  )
  Spacer(modifier = Modifier.height(16.dp))
  EditNumberField(
   modifier = Modifier.align(Alignment.CenterHorizontally),
   label = R.string.bill_amount,
   value = amountInput,
   onValueChange = { amountInput = it },
   keyBoardOptions = KeyboardOptions(
    keyboardType = KeyboardType.Number,
    imeAction = ImeAction.Next
   ),
   keyBoardActions = KeyboardActions(
    onNext = { focusManager.moveFocus(FocusDirection.Down) }
   )
  );
  EditNumberField(
   label = R.string.how_was_the_service,
   value = tipInput,
   onValueChange = { tipInput = it },
   keyBoardOptions = KeyboardOptions(
    keyboardType = KeyboardType.Number,
    imeAction = ImeAction.Done
   ),
   keyBoardActions = KeyboardActions(
    onDone = { focusManager.clearFocus() }
   )
  );
  RoundTipRow(roundUp = roundUp, onRoundUpChanged = { roundUp = it })
  Spacer(modifier = Modifier.height(24.dp));
  Text(
   text = stringResource(id = R.string.tip_amount, tip),
   modifier = Modifier.align(Alignment.CenterHorizontally),
   fontWeight = FontWeight.Bold,
   fontSize = 20.sp
  )

 }
}

@Composable
fun EditNumberField(
 modifier: Modifier = Modifier,
 @StringRes label: Int,
 value: String,
 onValueChange: (String) -> Unit,
 keyBoardOptions: KeyboardOptions,
 keyBoardActions: KeyboardActions
) {
 TextField(
  value = value,
  onValueChange = onValueChange,
  modifier = modifier.fillMaxWidth(),
  label = { Text(text = stringResource(label)) },
  singleLine = true,
  keyboardOptions = keyBoardOptions,
  keyboardActions = keyBoardActions
 )
}

@Composable
fun RoundTipRow(
 roundUp: Boolean,
 onRoundUpChanged: (Boolean) -> Unit,
 modifier: Modifier = Modifier
) {
 Row(
  modifier = Modifier
   .fillMaxWidth()
   .size(48.dp),
  verticalAlignment = Alignment.CenterVertically,
 ) {
  Text(text = stringResource(id = R.string.round_up_tip))
  Switch(
   checked = roundUp,
   onCheckedChange = onRoundUpChanged,
   modifier = modifier
    .fillMaxWidth()
    .wrapContentWidth(Alignment.End),
   colors = SwitchDefaults.colors(uncheckedThumbColor = Color.DarkGray)
  )
 }
}

private fun calculateTip(amount: Double, tipPercent: Double = 15.0, roundUp: Boolean): String {
 var tip = tipPercent / 100 * amount;
 if ( roundUp ) {
  tip = kotlin.math.ceil(tip);
 }
 return NumberFormat.getCurrencyInstance().format(tip);
}


@Preview(showSystemUi = true)
@Composable
fun TipTimeAppPreview() {
 TipTimeAppTheme {
  TipTimeScreen();
 }
}
