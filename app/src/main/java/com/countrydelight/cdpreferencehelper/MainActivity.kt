package com.countrydelight.cdpreferencehelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.countrydelight.cdpreferencehelper.preference_helper.PreferenceFunctionHelper.getPreference1
import com.countrydelight.cdpreferencehelper.preference_helper.PreferenceFunctionHelper.getPreference2
import com.countrydelight.cdpreferencehelper.preference_helper.PreferenceFunctionHelper.removePreference1
import com.countrydelight.cdpreferencehelper.preference_helper.PreferenceFunctionHelper.removePreference2
import com.countrydelight.cdpreferencehelper.preference_helper.PreferenceFunctionHelper.setPreference1
import com.countrydelight.cdpreferencehelper.preference_helper.PreferenceFunctionHelper.setPreference2
import com.countrydelight.cdpreferencehelper.ui.theme.CDPreferenceHelperTheme
import com.countrydelight.preferencedatastorehelper.PreferenceDataStoreImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CDPreferenceHelperTheme {
                MainUI()
            }
        }
    }


}

@Composable
fun MainUI() {
    val context = LocalContext.current
    var preference: PreferenceDataStoreImpl? by remember {
        mutableStateOf(null)
    }
    var preference1Value by remember {
        mutableIntStateOf(-1)
    }
    var preference2Value by remember {
        mutableIntStateOf(-1)
    }

    LaunchedEffect(key1 = Unit) {
        launch {
            withContext(Dispatchers.IO) {
                preference =
                    PreferenceDataStoreImpl(context, "test_preference") { exception ->
                        exception.printStackTrace()
                    }
                preference1Value = preference?.getPreference1(-1) ?: -1
                preference2Value = preference?.getPreference2(-1) ?: -1
            }
        }
    }
    val coroutineScope = rememberCoroutineScope()
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Preference 1 Value:-  ${if (preference1Value == -1) "No Value" else preference1Value}")
                Text(text = "Preference 2 Value:-  ${if (preference2Value == -1) "No Value" else preference2Value}")
            }
            SpacerHeight16()
            SpacerHeight16()
            OutlinedButton(onClick = {
                coroutineScope.launch {
                    preference?.setPreference1((preference?.getPreference1(-1) ?: -1) + 1)
                    preference1Value = preference?.getPreference1(-1) ?: -1
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Add Value To Preference 1")
            }
            SpacerHeight16()
            OutlinedButton(onClick = {
                coroutineScope.launch {
                    preference?.setPreference2((preference?.getPreference2(-1) ?: -1) + 1)
                    preference2Value = preference?.getPreference2(-1) ?: -1
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Add Value To Preference 2")
            }
            SpacerHeight16()
            OutlinedButton(onClick = {
                coroutineScope.launch {
                    preference?.removePreference1()
                    preference1Value = preference?.getPreference1(-1) ?: -1
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Remove Value From Preference 1")
            }
            SpacerHeight16()
            OutlinedButton(onClick = {
                coroutineScope.launch {
                    preference?.removePreference2()
                    preference2Value = preference?.getPreference2(-1) ?: -1
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Remove Value From Preference 2")
            }
            SpacerHeight16()
            OutlinedButton(
                onClick = {
                    coroutineScope.launch {
                        preference?.clearAll()
                        preference1Value = preference?.getPreference1(-1) ?: -1
                        preference2Value = preference?.getPreference2(-1) ?: -1


                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Clear All Preferences")
            }
        }
    }
}

@Composable
fun SpacerHeight16() {
    Spacer(modifier = Modifier.height(16.dp))
}
