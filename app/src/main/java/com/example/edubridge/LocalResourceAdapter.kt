package com.example.edubridge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class LocalResourceAdapter(
    private val onOpen: (LocalResource) -> Unit,
    private val onDelete: (LocalResource) -> Unit
) : ListAdapter<LocalResource, LocalResourceAdapter.VH>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_resource, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.tv_title)
        private val subtitle: TextView = itemView.findViewById(R.id.tv_subtitle)
        private val icon: ImageView = itemView.findViewById(R.id.iv_icon)
        private val btnDelete: Button = itemView.findViewById(R.id.btn_delete)

        fun bind(item: LocalResource) {
            title.text = item.title
            subtitle.text = ""
            icon.setImageResource(R.drawable.ic_article)

            itemView.setOnClickListener { onOpen(item) }
            btnDelete.setOnClickListener { onDelete(item) }
        }
    }

    private class Diff : DiffUtil.ItemCallback<LocalResource>() {
        override fun areItemsTheSame(oldItem: LocalResource, newItem: LocalResource): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: LocalResource, newItem: LocalResource): Boolean = oldItem == newItem
    }
}


