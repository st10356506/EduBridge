package com.example.edubridge
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.edubridge.databinding.ItemStudyMaterialBinding
import com.example.edubridge.StudyMaterial

class StudyMaterialAdapter(
    private val onStart: (StudyMaterial) -> Unit,
    private val onToggleSave: (StudyMaterial) -> Unit,
    private val isSaved: (StudyMaterial) -> Boolean
) : ListAdapter<StudyMaterial, StudyMaterialAdapter.StudyMaterialViewHolder>(StudyMaterialDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyMaterialViewHolder {
        val binding = ItemStudyMaterialBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StudyMaterialViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudyMaterialViewHolder, position: Int) {
        val material = getItem(position)
        holder.bind(material, onStart, onToggleSave, isSaved)
    }

    class StudyMaterialViewHolder(private val binding: ItemStudyMaterialBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            material: StudyMaterial,
            onStart: (StudyMaterial) -> Unit,
            onToggleSave: (StudyMaterial) -> Unit,
            isSaved: (StudyMaterial) -> Boolean
        ) {
            binding.materialName.text = material.title
            binding.materialDuration.text = material.duration
            binding.materialDifficulty.text = material.difficulty
            binding.materialIcon.setImageResource(material.iconRes)

            binding.startMaterialButton.setOnClickListener {
                onStart(material)
            }

            // Update the save button text based on current saved status
            updateSaveButtonText(material, isSaved)
            
            binding.saveMaterialButton.setOnClickListener {
                onToggleSave(material)
                // Update the button text after toggling
                updateSaveButtonText(material, isSaved)
            }
        }
        
        private fun updateSaveButtonText(material: StudyMaterial, isSaved: (StudyMaterial) -> Boolean) {
            binding.saveMaterialButton.text = if (isSaved(material)) "★ Saved" else "☆ Save"
        }
    }

    private class StudyMaterialDiffCallback : DiffUtil.ItemCallback<StudyMaterial>() {
        override fun areItemsTheSame(oldItem: StudyMaterial, newItem: StudyMaterial): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: StudyMaterial, newItem: StudyMaterial): Boolean {
            return oldItem == newItem
        }
    }
}