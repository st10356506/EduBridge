package com.example.edubridge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edubridge.data.Lesson
import com.example.edubridge.data.LessonDao
import com.example.edubridge.databinding.FragmentAllSubjectsBinding
import com.example.edubridge.LessonDetailFragment

class AllSubjectsFragment : Fragment() {

    private var _binding: FragmentAllSubjectsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: StudyMaterialAdapter
    private val lessonDao = LessonDao()
    private val viewModel: StudyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllSubjectsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        fetchLessonsFromFirebase()
        
        // Observe the view model's study materials to get updates
        viewModel.studyMaterials.observe(viewLifecycleOwner) { materials ->
            adapter.submitList(materials)
        }
    }

    private fun setupRecyclerView() {
        adapter = StudyMaterialAdapter(
            onStart = { material ->
                // Track that this material was started
                viewModel.onStartMaterial(material)
                // Replace fragment inside the main container
                val fragment = LessonDetailFragment.newInstance(material.id)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null) // allow back navigation
                    .commit()
            },
            onToggleSave = { material ->
                // Toggle saved status using the view model
                viewModel.toggleSaved(material)
            },
            isSaved = { material -> 
                // Check saved status from the view model
                viewModel.isSaved(material)
            }
        )

        binding.allSubjectsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.allSubjectsRecyclerView.adapter = adapter
    }

    private fun fetchLessonsFromFirebase() {
        lessonDao.database.child("lessons").get().addOnSuccessListener { snapshot ->
            val materials = mutableListOf<StudyMaterial>()
            snapshot.children.forEach { child ->
                val lesson = child.getValue(Lesson::class.java)
                lesson?.let {
                    materials.add(
                        StudyMaterial(
                            id = it.id,
                            title = it.title,
                            duration = it.duration,
                            difficulty = it.difficulty,
                            iconRes = R.drawable.ic_article // generic lesson icon
                        )
                    )
                }
            }
            // Update the view model with the fetched materials
            viewModel.updateStudyMaterials(materials)
            // Don't call adapter.submitList here since we're observing the view model
        }.addOnFailureListener { e ->
            e.printStackTrace() // optional: show a Toast for errors
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.allSubjectsRecyclerView.adapter = null
        _binding = null
    }
}
