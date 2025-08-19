package com.example.edubridge

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class ResourceItem(
	val id: String = "",
	val title: String = "",
	val url: String? = null,
	val inlineBase64: String? = null,
	val mimeType: String? = null,
	val sizeBytes: Int? = null,
	val timestamp: Long = 0L,
	val chunkCount: Int? = null,
	val chunkSize: Int? = null
)

class ResourceCenterFragment : Fragment() {

	private lateinit var recycler: RecyclerView
	private lateinit var emptyView: TextView
	private lateinit var progress: ProgressBar
	private lateinit var adapter: ResourceAdapter

	private var pendingBytesToExport: ByteArray? = null
	private var pendingMimeType: String = "application/octet-stream"
	private var pendingSuggestedFileName: String = "resource"

	private val createDocumentLauncher = registerForActivityResult(
		ActivityResultContracts.CreateDocument()
	) { uri: Uri? ->
		uri ?: return@registerForActivityResult
		val contentResolver = requireContext().contentResolver
		contentResolver.openOutputStream(uri)?.use { out ->
			val data = pendingBytesToExport
			if (data != null) {
				out.write(data)
				out.flush()
			}
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_resource_center, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		recycler = view.findViewById(R.id.recycler_resources)
		emptyView = view.findViewById(R.id.tv_empty)
		progress = view.findViewById(R.id.progress_loading)

		adapter = ResourceAdapter(
			onOpenUrl = { url ->
				startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
			},
			onExportItem = { item ->
				when {
					!item.inlineBase64.isNullOrEmpty() -> {
						pendingBytesToExport = android.util.Base64.decode(item.inlineBase64, android.util.Base64.NO_WRAP)
						pendingMimeType = item.mimeType ?: "application/octet-stream"
						pendingSuggestedFileName = item.title.ifEmpty { "resource" }
						createDocumentLauncher.launch(pendingSuggestedFileName)
					}
					(item.chunkCount ?: 0) > 0 -> {
						// Fetch chunks and assemble
						val db = FirebaseDatabase.getInstance().reference.child("resources").child(item.id).child("chunks")
						db.get().addOnSuccessListener { snap ->
							val chunks = mutableListOf<String>()
							snap.children.sortedBy { it.key?.toIntOrNull() ?: 0 }.forEach { c ->
								val base64 = c.getValue(String::class.java)
								if (base64 != null) chunks.add(base64)
							}
							val joined = chunks.joinToString("")
							pendingBytesToExport = android.util.Base64.decode(joined, android.util.Base64.NO_WRAP)
							pendingMimeType = item.mimeType ?: "application/octet-stream"
							pendingSuggestedFileName = item.title.ifEmpty { "resource" }
							createDocumentLauncher.launch(pendingSuggestedFileName)
						}
					}
				}
			},
			onDelete = { item ->
				val ref = FirebaseDatabase.getInstance().reference.child("resources").child(item.id)
				ref.removeValue()
			}
		)

		recycler.layoutManager = LinearLayoutManager(requireContext())
		recycler.adapter = adapter

		loadResources()
	}

	private fun loadResources() {
		progress.visibility = View.VISIBLE
		val dbRef = FirebaseDatabase.getInstance().reference.child("resources")
		dbRef.addValueEventListener(object : ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {
				val items = mutableListOf<ResourceItem>()
				snapshot.children.forEach { child ->
					val item = child.getValue(ResourceItem::class.java)
					if (item != null) items.add(item)
				}
				items.sortByDescending { it.timestamp }
				adapter.submitList(items)
				emptyView.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
				progress.visibility = View.GONE
			}

			override fun onCancelled(error: DatabaseError) {
				progress.visibility = View.GONE
			}
		})
	}
}


