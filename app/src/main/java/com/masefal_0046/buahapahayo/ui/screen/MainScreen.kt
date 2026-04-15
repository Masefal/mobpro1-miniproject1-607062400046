package com.masefal_0046.buahapahayo.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masefal_0046.buahapahayo.R
import com.masefal_0046.buahapahayo.model.Buah
import com.masefal_0046.buahapahayo.ui.theme.BuahApaHayoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val listBuah = listOf(
        Buah(R.string.apple,listOf("apple", "apel"), R.drawable.apple, R.drawable.pertanyaan_apel),
        Buah(R.string.orange,listOf("orange", "jeruk"), R.drawable.orange, R.drawable.pertanyaan_jeruk),
        Buah(R.string.grape,listOf("grape", "anggur"), R.drawable.grape, R.drawable.pertanyaan_anggur),
        Buah(R.string.mango, listOf("mango", "mangga"), R.drawable.manggo, R.drawable.pertanyaan_mangga),
        Buah(R.string.avocado,listOf("avocado", "alpukat"), R.drawable.avocado, R.drawable.pertanyaan_alpukat),
        Buah(R.string.banana,listOf("banana", "pisang"), R.drawable.banana, R.drawable.pertanyaan_pisang),
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        ScreenContent(
            modifier = Modifier.padding(innerPadding),
            listBuah = listBuah
        )
    }
}

@Composable
fun ScreenContent(
    modifier: Modifier = Modifier,
    listBuah: List<Buah>
) {
    var buah by remember { mutableStateOf(listBuah.random()) }
    var jawaban by remember { mutableStateOf("") }
    var hasil by remember { mutableStateOf("") }
    var dijawab by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    val benar = stringResource(R.string.right)
    val salah = stringResource(R.string.wrong)

    val imageRes = if (dijawab) buah.imageResId else buah.imagePertanyaanResId

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = buah.nama.first(),
            modifier = Modifier.size(180.dp)
        )
        Text(
            text = stringResource(R.string.guess),
            style = MaterialTheme.typography.titleMedium
        )
        OutlinedTextField(
            value = jawaban,
            onValueChange = {
                jawaban = it
                isError = false
            },
            label = { Text(stringResource(R.string.input)) },
            isError = isError,
            trailingIcon = { IconPicker(isError = isError, unit = "") },
            supportingText = { ErrorHint(isError = isError) },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                if (jawaban.isEmpty()) {
                    isError = true
                    return@Button
                }
                isError = false
                dijawab = true

                isCorrect = cekJawaban(jawaban, buah.nama)
                hasil = if (isCorrect) benar else salah
            },
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text(stringResource(R.string.guess))
        }

        if (dijawab) {
            Text(
                text = hasil,
                color = if (hasil == benar) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                },
                style = MaterialTheme.typography.titleMedium
            )
            Button(
                onClick = {
                    buah = getRandomBuah(listBuah, buah)
                    jawaban = ""
                    hasil = ""
                    dijawab = false
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = stringResource(R.string.play_again)
                )
            }
        }
    }
}

fun cekJawaban(jawaban: String, key: List<String>): Boolean {
    val input = jawaban.lowercase().trim()
    return key.any { it == input }
}

fun getRandomBuah(buah: List<Buah>, current: Buah): Buah {
    var newBuah: Buah
    do {
        newBuah = buah.random()
    } while (newBuah == current)
    return newBuah
}

@Composable
fun IconPicker(isError: Boolean, unit: String) {
    if (isError) {
        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )
    } else {
        Text(text = unit)
    }
}

@Composable
fun ErrorHint(isError: Boolean) {
    if (isError) {
        Text(
            text = stringResource(R.string.input_invalid),
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    BuahApaHayoTheme {
        MainScreen()
    }
}