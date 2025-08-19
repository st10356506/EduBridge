package com.example.edubridge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel

class StudyViewModel : ViewModel() {

    private val _studyMaterials = MutableLiveData<List<StudyMaterial>>()
    val studyMaterials: LiveData<List<StudyMaterial>> get() = _studyMaterials

    // Track saved and recent by id so we can reconstruct full models when needed
    private val _savedIds = MutableLiveData<Set<String>>(emptySet())
    private val _recentIds = MutableLiveData<List<String>>(emptyList())

    // Expose computed lists for UI to observe
    val savedMaterials: LiveData<List<StudyMaterial>> = MediatorLiveData<List<StudyMaterial>>().apply {
        fun recompute() {
            val all = _studyMaterials.value.orEmpty()
            val ids = _savedIds.value.orEmpty()
            value = all.filter { ids.contains(it.id) }
        }
        addSource(_studyMaterials) { recompute() }
        addSource(_savedIds) { recompute() }
    }

    val recentMaterials: LiveData<List<StudyMaterial>> = MediatorLiveData<List<StudyMaterial>>().apply {
        fun recompute() {
            val allById = _studyMaterials.value.orEmpty().associateBy { it.id }
            val ids = _recentIds.value.orEmpty()
            value = ids.mapNotNull { allById[it] }
        }
        addSource(_studyMaterials) { recompute() }
        addSource(_recentIds) { recompute() }
    }

    init {
        // Load initial data (this can come from a database, API, etc.)
        loadStudyMaterials()
    }

    private fun loadStudyMaterials() {
        val materials = listOf(
            StudyMaterial(
                id = "lesson_1",
                title = "Introduction to Algebra",
                duration = "10 min",
                difficulty = "Easy",
                iconRes = R.drawable.ic_article
            ),
            StudyMaterial(
                id = "lesson_2",
                title = "Photosynthesis",
                duration = "8 min",
                difficulty = "Medium",
                iconRes = R.drawable.ic_article
            ),
            StudyMaterial(
                id = "lesson_3",
                title = "World War II Overview",
                duration = "15 min",
                difficulty = "Hard",
                iconRes = R.drawable.ic_article
            ),
            StudyMaterial(
                id = "lesson_4",
                title = "Chemistry: The Atom",
                duration = "25 min",
                difficulty = "Medium",
                iconRes = R.drawable.ic_article
            )
        )
        _studyMaterials.value = materials
    }

    fun onStartMaterial(material: StudyMaterial) {
        val current = _recentIds.value.orEmpty()
        // Move to front, keep unique order
        val newOrder = listOf(material.id) + current.filterNot { it == material.id }
        android.util.Log.d("StudyViewModel", "Adding ${material.title} to recent. New order: $newOrder")
        _recentIds.value = newOrder
    }

    fun toggleSaved(material: StudyMaterial) {
        val current = _savedIds.value.orEmpty()
        val newSavedIds = if (current.contains(material.id)) current - material.id else current + material.id
        android.util.Log.d("StudyViewModel", "Toggling saved for ${material.title}. New saved IDs: $newSavedIds")
        _savedIds.value = newSavedIds
    }

    fun isSaved(material: StudyMaterial): Boolean {
        return _savedIds.value?.contains(material.id) == true
    }

    fun updateStudyMaterials(materials: List<StudyMaterial>) {
        // Preserve existing saved and recent IDs when updating materials
        val currentSavedIds = _savedIds.value.orEmpty()
        val currentRecentIds = _recentIds.value.orEmpty()
        
        android.util.Log.d("StudyViewModel", "Updating study materials. Count: ${materials.size}, Saved IDs: $currentSavedIds, Recent IDs: $currentRecentIds")
        
        _studyMaterials.value = materials
        
        // Ensure saved and recent materials are still tracked
        _savedIds.value = currentSavedIds
        _recentIds.value = currentRecentIds
    }
}