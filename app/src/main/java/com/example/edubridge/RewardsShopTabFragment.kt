package com.example.edubridge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RewardsShopTabFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.tab_rewards_shop, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler = view.findViewById<RecyclerView>(R.id.rewards_recycler_view)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = SimpleRewardsAdapter(
            listOf(
                SimpleReward("👟", "Sneaker Sticker Pack", "Collect fun sneaker stickers", 300),
                SimpleReward("🎧", "Music Time", "Unlock a study playlist", 450),
                SimpleReward("🎮", "Game Night Pass", "30 mins of supervised game time", 600),
                SimpleReward("🍫", "Chocolate Treat", "One small chocolate bar", 250),
                SimpleReward("🎨", "Art Kit", "Crayons and mini sketchbook", 700),
                SimpleReward("📱", "Phone Wallpaper Pack", "Exclusive motivational wallpapers", 200),
                SimpleReward("🚀", "Rocket Badge", "A shiny profile badge", 350),
                SimpleReward("🎁", "Mystery Box", "A surprise classroom reward", 800),
                SimpleReward("🧩", "Puzzle Time", "Extra puzzle/brain teaser time", 500),
                SimpleReward("🎓", "Star Student Shoutout", "Get featured on the leaderboard", 900)
            )
        )
    }
}


