package com.example.edubridge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AiFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ai_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.messages_recycler_view)
        val sendButton = view.findViewById<MaterialButton>(R.id.send_button)
        val messageInput = view.findViewById<TextInputEditText>(R.id.message_input)

        // Setup RecyclerView (no NestedScrollView needed now)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        // Attach empty adapter (no seeded messages)
        adapter = ChatAdapter()
        recyclerView.adapter = adapter

        // Scroll to bottom helper
        fun scrollToBottom() {
            if (adapter.itemCount > 0) {
                recyclerView.scrollToPosition(adapter.itemCount - 1)
            }
        }

        // Auto-scroll when new messages are inserted
        val observer = object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) { scrollToBottom() }
        }
        adapter.registerAdapterDataObserver(observer)

        // Send button logic
        sendButton.setOnClickListener {
            val text = messageInput.text?.toString()?.trim()
            if (!text.isNullOrEmpty()) {
                adapter.addMessage(text)
                messageInput.text?.clear()
            }
        }

        // Initial scroll
        view.post { scrollToBottom() }
    }

    override fun onResume() {
        super.onResume()
        view?.post {
            if (adapter.itemCount > 0) {
                view?.findViewById<RecyclerView>(R.id.messages_recycler_view)
                    ?.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AiFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
