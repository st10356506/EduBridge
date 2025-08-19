package com.example.edubridge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.edubridge.data.Lesson
import com.example.edubridge.databinding.ItemStudyMaterialBinding

class LessonAdapter(
    private val lessons: List<Lesson>,
    private val onClick: (Lesson) -> Unit
) : RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {

    inner class LessonViewHolder(val binding: ItemStudyMaterialBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(lesson: Lesson) {
            binding.materialName.text = lesson.title
            binding.materialDuration.text = lesson.duration
            binding.materialDifficulty.text = lesson.difficulty
            binding.root.setOnClickListener { onClick(lesson) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val binding = ItemStudyMaterialBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LessonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        holder.bind(lessons[position])
    }

    override fun getItemCount(): Int = lessons.size
}
