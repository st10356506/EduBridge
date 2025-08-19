package com.example.edubridge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import android.content.Intent
import android.net.Uri

class HelpFragment : Fragment() {

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_help, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val btnEmail: MaterialButton = view.findViewById(R.id.btn_email_support)
		val btnOpenHelp: MaterialButton = view.findViewById(R.id.btn_open_help_center)

		btnEmail.setOnClickListener {
			val intent = Intent(Intent.ACTION_SENDTO).apply {
				data = Uri.parse("mailto:")
				putExtra(Intent.EXTRA_EMAIL, arrayOf("support@edubridge.app"))
				putExtra(Intent.EXTRA_SUBJECT, "EduBridge Support Request")
			}
			startActivity(Intent.createChooser(intent, "Send email"))
		}

		btnOpenHelp.setOnClickListener {
			val url = "https://example.com/help"
			val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
			startActivity(intent)
		}
	}
}


