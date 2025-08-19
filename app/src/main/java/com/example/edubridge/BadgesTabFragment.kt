package com.example.edubridge

import Badge
import android.os.Bundle
import android. view.LayoutInflater
import android. view.View
import android. view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BadgesTabFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.tab_badges, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val points = requireActivity()
            .getSharedPreferences("user_data", android.content.Context.MODE_PRIVATE)
            .getInt("user_points", 0)

        val bronzeEarned = points >= 500

        val badges = listOf(
            Badge(
                id = "bronze",
                name = "Bronze Scholar",
                description = "Reach 500 total points",
                icon = "🥉",
                rarity = "common",
                pointsRequired = 500,
                isEarned = bronzeEarned
            )
        )

        val recycler = view.findViewById<RecyclerView>(R.id.badges_recycler_view)
        recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        recycler.adapter = BadgesAdapter(badges)
    }
}


