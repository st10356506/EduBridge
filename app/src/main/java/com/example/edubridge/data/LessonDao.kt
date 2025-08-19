package com.example.edubridge.data

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LessonDao(
    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
) {

    suspend fun getLesson(lessonId: String): Lesson = suspendCancellableCoroutine { cont ->
        try {
            android.util.Log.d("LessonDao", "Fetching lesson with ID: $lessonId")
            database.child("lessons").child(lessonId).get()
                .addOnSuccessListener { snapshot ->
                    android.util.Log.d("LessonDao", "Successfully fetched lesson data")
                    val lesson = snapshot.getValue(Lesson::class.java)
                    if (lesson != null) {
                        android.util.Log.d("LessonDao", "Lesson found: ${lesson.title}")
                        cont.resume(lesson)
                    } else {
                        android.util.Log.w("LessonDao", "Lesson not found, creating default")
                        cont.resume(Lesson(id = lessonId))
                    }
                }
                .addOnFailureListener { e ->
                    android.util.Log.e("LessonDao", "Error fetching lesson", e)
                    cont.resumeWithException(e)
                }
        } catch (e: Exception) {
            android.util.Log.e("LessonDao", "Exception in getLesson", e)
            cont.resumeWithException(e)
        }
    }


    fun observeLesson(
        lessonId: String,
        onChange: (Lesson) -> Unit,
        onError: (DatabaseError) -> Unit
    ): ValueEventListener {
        val ref = database.child("lessons").child(lessonId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lesson = snapshot.getValue(Lesson::class.java) ?: Lesson(id = lessonId)
                onChange(lesson)
            }
            override fun onCancelled(error: DatabaseError) {
                onError(error)
            }
        }
        ref.addValueEventListener(listener)
        return listener
    }

    fun removeObserver(lessonId: String, listener: ValueEventListener) {
        database.child("lessons").child(lessonId).removeEventListener(listener)
    }


    fun saveLesson(lesson: Lesson) {
        // Use the lesson's ID as the key in the database
        val lessonRef = database.child("lessons").child(lesson.id)
        lessonRef.setValue(lesson)
            .addOnSuccessListener {
                // Handle success, e.g., logging
                println("Lesson saved successfully!")
            }
            .addOnFailureListener { e ->
                // Handle failure, e.g., logging or showing an error
                println("Error saving lesson: ${e.message}")
            }
    }
}
