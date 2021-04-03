package com.tokayoapp.Modal

data class DataShowReview(
    val `data`: List<Data>,
    val total_count: Int,
    val Average_count: AverageCount
) {
    data class Data(
        val id: String,
        val product_id: String,
        val user_id: String,
        val score: String,
        val comment: String,
        val created_date: String,
        val Name: String
    )

    data class AverageCount(
        val Average_score: String
    )
}