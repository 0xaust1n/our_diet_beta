package com.odstudio.ourdiet.dataClass


data class UserAll(
    val avatar: String,
    val uid: String? = null,
    val LastName: String? = null,
    val FirstName: String? = null,
    val Nick: String? = null,
    val Birthday: String? = null,
    val Height: Int? = null,
    val Weight: Int? = null,
    val BMI: Double? = null,
    val Gender: String? = null
)