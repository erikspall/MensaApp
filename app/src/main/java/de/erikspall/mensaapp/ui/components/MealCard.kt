package de.erikspall.mensaapp.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import de.erikspall.mensaapp.domain.enums.AdditiveType
import de.erikspall.mensaapp.domain.enums.Role
import de.erikspall.mensaapp.domain.model.Additive
import de.erikspall.mensaapp.domain.model.Meal
import de.erikspall.mensaapp.domain.utils.Extensions.noRippleClickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealCard(
    modifier: Modifier = Modifier,
    meal: Meal,
    role: Role = Role.STUDENT
) {
    val ingredients = remember {
        meal.additives.filter { additive ->
            additive.type == AdditiveType.INGREDIENT
        }.joinToString(", ") { it.name }
    }

    val containsNotLikedAdditives = remember {
        meal.additives.any { mealAdditive -> mealAdditive.isNotLiked }
    }


    var expanded by rememberSaveable { // Save it because it will scroll of screen
        mutableStateOf(false)
    }

    var currentRotation by remember { mutableStateOf(0f) }

    val rotation = remember { Animatable(currentRotation) }

    LaunchedEffect(expanded) {
        if (expanded) {
            rotation.animateTo(
                targetValue = currentRotation + 180f
            ) {
                currentRotation = value
            }
        } else {
            rotation.animateTo(
                targetValue = 0f
            ) {
                currentRotation = value
            }
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable {
                expanded = !expanded
            }
            .wrapContentHeight()
            .animateContentSize()
            .clip(RoundedCornerShape(28.dp)), // TODO: Not needed
        shape = RoundedCornerShape(28.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (ingredients.isNotEmpty()) {
                        DetailChip(
                            text = ingredients,
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            onContainerColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                    if (containsNotLikedAdditives) {
                        Icon(
                            imageVector = Icons.Rounded.ErrorOutline,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .wrapContentWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (meal.prices[role] != null) {
                        Text(
                            text = meal.prices[role]!!,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    if (meal.additives.isNotEmpty()) {
                        FilledIconButton(
                            modifier = Modifier.rotate(rotation.value),
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            onClick = { expanded = !expanded }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.KeyboardArrowDown,
                                contentDescription = ""
                            )
                        }
                    }
                }


            }
            Text(
                text = meal.name,
                style = MaterialTheme.typography.bodyLarge
            )
            if (expanded) {
                Divider(color = MaterialTheme.colorScheme.onSurface)
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    //crossAxisSpacing = 16.dp,
                    mainAxisSpacing = 8.dp
                ) {
                    meal.additives.filter { additive -> additive.type == AdditiveType.ALLERGEN }
                        .forEach { additive ->
                            AssistChip(
                                border = if (additive.isNotLiked) {
                                    AssistChipDefaults.assistChipBorder(
                                        borderColor = MaterialTheme.colorScheme.error
                                    )
                                } else {
                                    AssistChipDefaults.assistChipBorder()
                                },
                                /* TODO leadingIcon = {
                                     if (it.isNotLiked)
                                         Icon(
                                             modifier = Modifier.size(18.dp),
                                             imageVector = Icons.Rounded.Check,
                                             contentDescription = ""
                                         )
                                 },*/
                                colors = if (additive.isNotLiked) {
                                    AssistChipDefaults.assistChipColors(
                                        labelColor = MaterialTheme.colorScheme.error
                                    )
                                } else {
                                    AssistChipDefaults.assistChipColors()
                                },
                                label = {
                                    Text(text = additive.name)
                                },
                                onClick = {}
                            )
                        }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewMealCard() {
    MealCard(
        meal = Meal(
            name = "Vegane Lasagne",
            prices = mapOf(
                Role.STUDENT to "2,35€"
            ),
            additives = listOf(
                Additive(
                    name = "Vegan",
                    type = AdditiveType.INGREDIENT,
                    isNotLiked = true
                ),
                Additive(
                    name = "Nüsse",
                    type = AdditiveType.ALLERGEN,
                    isNotLiked = true
                )
            )
        )
    )
}