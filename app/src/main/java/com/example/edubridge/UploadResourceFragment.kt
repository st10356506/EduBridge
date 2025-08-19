package com.example.edubridge

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import java.io.File
import java.io.FileOutputStream

class UploadResourceFragment : Fragment() {

    private var selectedFileUri: Uri? = null

    private val pickFileLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedFileUri = result.data?.data
            // Show selected file name
            val uri = selectedFileUri
            var name: String? = null
            if (uri != null) {
                requireContext().contentResolver.query(uri, null, null, null, null)?.use { c ->
                    val idx = c.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    if (idx != -1 && c.moveToFirst()) name = c.getString(idx)
                }
            }
            view?.findViewById<android.widget.TextView>(R.id.tv_selected_file)?.text =
                "Selected: ${name ?: (uri?.lastPathSegment ?: "file")}"
            Toast.makeText(requireContext(), "File selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upload_resource, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleInput: EditText = view.findViewById(R.id.input_title)
        val pickButton: MaterialButton = view.findViewById(R.id.btn_pick_file)
        val uploadButton: MaterialButton = view.findViewById(R.id.btn_upload)
        val progressBar: ProgressBar = view.findViewById(R.id.upload_progress)
        val tvSelected: android.widget.TextView = view.findViewById(R.id.tv_selected_file)
        val tvProgress: android.widget.TextView = view.findViewById(R.id.tv_progress)

        pickButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*/*"
            }
            pickFileLauncher.launch(Intent.createChooser(intent, "Select file"))
        }

        uploadButton.setOnClickListener {
            val title = titleInput.text.toString().trim()
            val fileUri = selectedFileUri
            if (title.isEmpty()) {
                Toast.makeText(requireContext(), "Enter a title", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (fileUri == null) {
                Toast.makeText(requireContext(), "Pick a file", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE
            val cr = requireContext().contentResolver
            val mime = cr.getType(fileUri) ?: "application/octet-stream"
            tvProgress.text = "Copying..."
            Thread {
                try {
                    val dir = java.io.File(requireContext().filesDir, "uploads")
                    if (!dir.exists()) dir.mkdirs()
                    val safe = title.replace(Regex("[^A-Za-z0-9._-]"), "_")
                    val dest = java.io.File(dir, System.currentTimeMillis().toString() + "_" + safe)
                    cr.openInputStream(fileUri)?.use { input ->
                        java.io.FileOutputStream(dest).use { out ->
                            val buf = ByteArray(8192)
                            while (true) {
                                val read = input.read(buf)
                                if (read == -1) break
                                out.write(buf, 0, read)
                            }
                            out.flush()
                        }
                    }
                    LocalResourceStore.add(requireContext(), LocalResource(
                        title = title,
                        path = dest.absolutePath,
                        mimeType = mime
                    ))
                    view?.post {
                        progressBar.visibility = View.GONE
                        tvProgress.text = ""
                        Toast.makeText(requireContext(), "Uploaded", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    view?.post {
                        progressBar.visibility = View.GONE
                        tvProgress.text = ""
                        Toast.makeText(requireContext(), "Copy failed: ${'$'}{e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }.start()
        }
    }
}



