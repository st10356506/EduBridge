package com.example.edubridge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.edubridge.R // Make sure to import R for drawable resources
import com.example.edubridge.StudyMaterial // Import your data model

class StudyViewModel : ViewModel() {

    private val _studyMaterials = MutableLiveData<List<StudyMaterial>>()
    val studyMaterials: LiveData<List<StudyMaterial>> get() = _studyMaterials

    init {
        // Load initial data (this can come from a database, API, etc.)
        loadStudyMaterials()
    }

    private fun loadStudyMaterials() {
        val materials = listOf(
            StudyMaterial(
                id = "1",
                title = "Algebra Basics",
                duration = "20 min",
                difficulty = "Easy",
                iconRes = R.drawable.ic_article
            ),
            StudyMaterial(
                id = "2",
                title = "Fractions",
                duration = "15 min",
                difficulty = "Medium",
                iconRes = R.drawable.ic_article
            ),
            StudyMaterial(
                id = "3",
                title = "Chemistry: The Atom",
                duration = "30 min",
                difficulty = "Hard",
                iconRes = R.drawable.ic_article
            ),
            StudyMaterial(
                id = "4",
                title = "Biology: Photosynthesis",
                duration = "25 min",
                difficulty = "Medium",
                iconRes = R.drawable.ic_article
            )
        )
        _studyMaterials.value = materials
    }
}