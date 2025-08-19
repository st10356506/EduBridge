package com.example.edubridge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LeaderboardTabFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.tab_leaderboard, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler = view.findViewById<RecyclerView>(R.id.leaderboard_recycler_view)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        val entries = listOf(
            LeaderboardEntry(1, "Nomsa K.", 3240, false, "👧"),
            LeaderboardEntry(2, "Thabo M.", 2980, true, "🧒"),
            LeaderboardEntry(3, "Aisha P.", 2875, false, "👩"),
            LeaderboardEntry(4, "Liam S.", 2700, false, "👦"),
            LeaderboardEntry(5, "Zinzi R.", 2580, false, "👩‍🦱")
        )

        recycler.adapter = LeaderboardAdapter(entries)
    }
}


