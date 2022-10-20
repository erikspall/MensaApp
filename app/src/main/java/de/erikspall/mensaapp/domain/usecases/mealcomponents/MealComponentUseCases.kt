package de.erikspall.mensaapp.domain.usecases.mealcomponents

data class MealComponentUseCases (
    val getAllergens: GetAllergens,
    val getIngredients: GetIngredients,
    val setAllergenLikeStatus: SetAllergenLikeStatus,
    val setIngredientLikeStatus: SetIngredientLikeStatus,
    val fetchLatest: FetchLatest
)