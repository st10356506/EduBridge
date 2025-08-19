package com.example.edubridge

import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ResourceAdapter(
	private val onOpenUrl: (String) -> Unit,
	private val onExportItem: (ResourceItem) -> Unit,
	private val onDelete: (ResourceItem) -> Unit
) : ListAdapter<ResourceItem, ResourceAdapter.ResourceViewHolder>(Diff()) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.item_resource, parent, false)
		return ResourceViewHolder(view)
	}

	override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	inner class ResourceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		private val title: TextView = itemView.findViewById(R.id.tv_title)
		private val subtitle: TextView = itemView.findViewById(R.id.tv_subtitle)
		private val icon: ImageView = itemView.findViewById(R.id.iv_icon)
		private val btnDelete: android.widget.Button = itemView.findViewById(R.id.btn_delete)

		fun bind(item: ResourceItem) {
			title.text = item.title.ifEmpty { "Untitled" }
			btnDelete.setOnClickListener { onDelete(item) }

			if (!item.url.isNullOrEmpty()) {
				subtitle.text = "Link"
				icon.setImageResource(R.drawable.ic_upload)
				itemView.setOnClickListener { onOpenUrl(item.url) }
			} else if (!item.inlineBase64.isNullOrEmpty()) {
				subtitle.text = ""
				icon.setImageResource(R.drawable.ic_article)
				itemView.setOnClickListener { onExportItem(item) }
			} else if ((item.chunkCount ?: 0) > 0) {
				subtitle.text = ""
				icon.setImageResource(R.drawable.ic_article)
				itemView.setOnClickListener { onExportItem(item) }
			} else {
				subtitle.text = "Unknown"
				icon.setImageResource(R.drawable.ic_article)
				itemView.setOnClickListener(null)
			}
		}
	}

	private class Diff : DiffUtil.ItemCallback<ResourceItem>() {
		override fun areItemsTheSame(oldItem: ResourceItem, newItem: ResourceItem): Boolean = oldItem.id == newItem.id
		override fun areContentsTheSame(oldItem: ResourceItem, newItem: ResourceItem): Boolean = oldItem == newItem
	}
}


