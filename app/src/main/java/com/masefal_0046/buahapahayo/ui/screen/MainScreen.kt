package com.masefal_0046.buahapahayo.ui.screen

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
        Buah("apple", R.drawable.apple, R.drawable.pertanyaan_apel),
        Buah("orange", R.drawable.orange, R.drawable.pertanyaan_jeruk),
        Buah("grape", R.drawable.grape, R.drawable.pertanyaan_anggur),
        Buah("mango", R.drawable.manggo, R.drawable.pertanyaan_mangga),
        Buah("avocado", R.drawable.avocado, R.drawable.pertanyaan_alpukat),
        Buah("banana", R.drawable.banana, R.drawable.pertanyaan_pisang),
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
fun ScreenContent(modifier: Modifier = Modifier, listBuah: List<Buah>) {

    var buah by remember { mutableStateOf(listBuah.random())}
    var jawaban by remember { mutableStateOf("") }
    var hasil by remember { mutableStateOf("") }
    var dijawab by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val right = stringResource(R.string.right)
        val wrong = stringResource(R.string.wrong)
        val imageRes = if (dijawab) {
            buah.imageResId
        } else {
            buah.imagePertanyaanResId
        }

        Image(
            painter = painterResource(id = imageRes),
            contentDescription = buah.name,
            modifier = modifier.size(180.dp)
        )
        Text(
            text = stringResource(R.string.guess),
            style = MaterialTheme.typography.titleMedium,
        )
        OutlinedTextField(
            value = jawaban,
            onValueChange = { jawaban = it },
            label = { Text(text = stringResource(R.string.input))},
            modifier = modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                dijawab = true
                hasil = if (jawaban.lowercase() == buah.name) right else wrong
            },
            modifier = modifier.fillMaxWidth(0.5f),
            contentPadding = PaddingValues(16.dp)
        ) {
            Text(text = stringResource(R.string.guess))
        }

        if (dijawab) {
            Text(
                text = hasil,
                modifier = modifier.padding(top = 2.dp),
                style= MaterialTheme.typography.titleMedium
            )
            Button(
                onClick = {
                    buah = listBuah.random()
                    jawaban = ""
                    hasil = ""
                    dijawab = false
                },
                modifier = modifier.fillMaxWidth(0.5f)
            ) {
                Text(stringResource(R.string.reload))
            }
        }
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