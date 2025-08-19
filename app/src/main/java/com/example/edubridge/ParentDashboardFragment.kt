package com.example.edubridge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import android.widget.Toast

class ParentDashboardFragment : Fragment() {

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_parent_dashboard, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val btnReports: MaterialButton = view.findViewById(R.id.btn_view_reports)
		val btnGoals: MaterialButton = view.findViewById(R.id.btn_set_goals)

		btnReports.setOnClickListener {
			Toast.makeText(requireContext(), "Opening detailed reports (coming soon)", Toast.LENGTH_SHORT).show()
		}

		btnGoals.setOnClickListener {
			Toast.makeText(requireContext(), "Set study goals (coming soon)", Toast.LENGTH_SHORT).show()
		}
	}
}


