package com.example.edubridge

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val messages: MutableList<String> = mutableListOf()) : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

	class MessageViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
		val textView = TextView(parent.context)
		textView.layoutParams = ViewGroup.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.WRAP_CONTENT
		)
		textView.setPadding(24, 16, 24, 16)
		return MessageViewHolder(textView)
	}

	override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
		holder.textView.text = messages[position]
	}

	override fun getItemCount(): Int = messages.size

	fun addMessage(message: String) {
		val insertPosition = messages.size
		messages.add(message)
		notifyItemInserted(insertPosition)
	}
}