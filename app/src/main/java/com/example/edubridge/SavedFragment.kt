package com.example.edubridge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment // Correct import
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edubridge.databinding.FragmentSavedBinding
import com.example.edubridge.LessonDetailFragment

// Make sure it extends Fragment
class SavedFragment : Fragment() {

    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StudyViewModel by activityViewModels()
    private lateinit var adapter: StudyMaterialAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = StudyMaterialAdapter(
            onStart = { material ->
                viewModel.onStartMaterial(material)
                // Navigate to LessonDetailFragment instead of StudyDetailActivity
                val lessonDetailFragment = LessonDetailFragment.newInstance(material.id)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, lessonDetailFragment)
                    .addToBackStack(null)
                    .commit()
            },
            onToggleSave = { material -> viewModel.toggleSaved(material) },
            isSaved = { material -> viewModel.isSaved(material) }
        )

        binding.savedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.savedRecyclerView.adapter = adapter

        viewModel.savedMaterials.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.savedRecyclerView.adapter = null
        _binding = null
    }
}
