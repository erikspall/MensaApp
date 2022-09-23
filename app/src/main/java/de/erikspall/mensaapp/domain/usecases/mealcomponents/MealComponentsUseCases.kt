package de.erikspall.mensaapp.domain.usecases.mealcomponents

data class MealComponentsUseCases (
    val getAllergenic: GetAllergenic,
    val getIngredients: GetIngredients
)