package de.erikspall.mensaapp.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.erikspall.mensaapp.domain.model.FoodProvider

@Composable
fun FoodProvidersList(
    modifier: Modifier = Modifier,
    list: List<FoodProvider>,
    onClickedFoodProvider: (FoodProvider) -> Unit = {},

) {
    LazyColumn(
        modifier = modifier
    ) {
        items(list) { foodProvider ->
            FoodProviderCard(
                modifier = Modifier
                    .padding(8.dp),
                name = foodProvider.name,
                type = foodProvider.type,
                image = foodProvider.photo,
                openingInfo = foodProvider.openingHoursString
            )
        }
    }
}