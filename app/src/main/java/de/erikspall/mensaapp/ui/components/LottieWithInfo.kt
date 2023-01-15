package de.erikspall.mensaapp.ui.components

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import de.erikspall.mensaapp.R

@Composable
fun LottieWithInfo(
    modifier: Modifier = Modifier,
    @RawRes lottie: Int,
    description: String,
    iterations: Int = LottieConstants.IterateForever
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(lottie))

    Column(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            modifier = Modifier.fillMaxWidth()
                .height(200.dp),
            composition = composition,
            iterations = iterations
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewLottieWithInfo() {
    LottieWithInfo(
        lottie = R.raw.loading_menus,
        description = "Suche nach Gerichten ..."
    )
}