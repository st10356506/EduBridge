package com.example.edubridge

data class StudyMaterial(
    val id: String,
    val title: String,
    val duration: String,
    val difficulty: String,
    val iconRes: Int, // Resource ID for the icon drawable
    val contentResId: Int? = null // Optional raw resource id for content
)