package com.example.edubridge

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.edubridge.data.Lesson
import com.example.edubridge.data.LessonDao
import com.example.edubridge.databinding.FragmentLessonDetailBinding
import kotlinx.coroutines.launch

class LessonDetailFragment : Fragment() {

    private var _binding: FragmentLessonDetailBinding? = null
    private val binding get() = _binding!!

    private val lessonDao = LessonDao()

    companion object {
        private const val ARG_LESSON_ID = "lesson_id"

        fun newInstance(lessonId: String): LessonDetailFragment {
            val fragment = LessonDetailFragment()
            val bundle = Bundle().apply { putString(ARG_LESSON_ID, lessonId) }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            _binding = FragmentLessonDetailBinding.inflate(inflater, container, false)
            return binding.root
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback to a simple TextView if binding fails
            val textView = android.widget.TextView(requireContext())
            textView.text = "Error loading lesson detail"
            return textView
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            // Set up back button click listener
            binding.backButton.setOnClickListener {
                android.util.Log.d("LessonDetailFragment", "Back button clicked, navigating back")
                // Navigate back to the previous fragment
                requireActivity().supportFragmentManager.popBackStack()
            }

            val lessonId = arguments?.getString(ARG_LESSON_ID)
            if (lessonId == null) {
                showFallback()
                return
            }

            binding.detailTitleText.text = "Loading…"
            binding.detailMetaText.text = ""
            binding.detailContentText.text = "Please wait…"

            lifecycleScope.launch {
                try {
                    val lesson = lessonDao.getLesson(lessonId)
                    bindLesson(lesson)
                } catch (e: Exception) {
                    e.printStackTrace()
                    showFallback()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            showFallback()
        }
    }

    private fun bindLesson(lesson: Lesson) {
        try {
            android.util.Log.d("LessonDetailFragment", "Binding lesson: ${lesson.title}")
            binding.detailTitleText.text = lesson.title
            binding.detailMetaText.text = "${lesson.duration} • ${lesson.difficulty}"
            binding.detailContentText.text =
                HtmlCompat.fromHtml(lesson.content.ifBlank { "Content coming soon." },
                    HtmlCompat.FROM_HTML_MODE_LEGACY)
            binding.detailContentText.movementMethod = LinkMovementMethod.getInstance()
        } catch (e: Exception) {
            e.printStackTrace()
            android.util.Log.e("LessonDetailFragment", "Error binding lesson", e)
            showFallback()
        }
    }

    private fun showFallback() {
        binding.detailTitleText.text = "Study Material"
        binding.detailMetaText.text = ""
        binding.detailContentText.text = "Content coming soon."
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
