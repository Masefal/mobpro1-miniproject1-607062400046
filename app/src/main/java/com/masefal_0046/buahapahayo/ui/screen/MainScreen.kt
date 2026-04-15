package com.masefal_0046.buahapahayo.ui.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.masefal_0046.buahapahayo.R
import com.masefal_0046.buahapahayo.model.Buah
import com.masefal_0046.buahapahayo.navigation.Screen
import com.masefal_0046.buahapahayo.ui.theme.BuahApaHayoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
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
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.About.route)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.about),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
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
    var jawaban by rememberSaveable { mutableStateOf("") }
    var dijawab by rememberSaveable { mutableStateOf(false) }
    var isCorrect by rememberSaveable { mutableStateOf(false) }
    var isError by rememberSaveable { mutableStateOf(false) }

    val imageRes = if (dijawab) buah.imageResId else buah.imagePertanyaanResId
    val context = LocalContext.current

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
            },
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text(stringResource(R.string.guess))
        }

        if (dijawab) {
            val jawabanBuah = buah.nama.first()
            val messageRight = stringResource(R.string.right_result, jawaban)
            val messageWrong = stringResource(R.string.wrong_result, jawaban, jawabanBuah)
            val hasilText = if (isCorrect) {
                stringResource(R.string.right)
            } else {
                stringResource(R.string.wrong, jawabanBuah)
            }

            Text(
                text = hasilText,
                color = if (isCorrect) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                },
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(
                onClick = {
                    buah = getRandomBuah(listBuah, buah)
                    jawaban = ""
                    dijawab = false
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = stringResource(R.string.play_again)
                )
            }
            Button(
                onClick = {
                    val message = if (isCorrect) messageRight else messageWrong
                    shareData(context,message)
                },
                modifier = Modifier.padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = stringResource(R.string.share))
            }
        }
    }
}

private fun shareData(context: Context, message: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
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
        MainScreen(rememberNavController())
    }
}