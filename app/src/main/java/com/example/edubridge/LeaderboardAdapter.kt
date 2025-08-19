package com.example.edubridge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val points: Int,
    val isYou: Boolean,
    val avatar: String
)

class LeaderboardAdapter(private val entries: List<LeaderboardEntry>) :
    RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rankCircle: CardView = itemView.findViewById(R.id.rank_circle)
        val rankNumber: TextView = itemView.findViewById(R.id.rank_number)
        val userAvatar: TextView = itemView.findViewById(R.id.user_avatar)
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val userPoints: TextView = itemView.findViewById(R.id.user_points)
        val youBadge: com.google.android.material.chip.Chip = itemView.findViewById(R.id.you_badge)
        val trophyIcon: ImageView = itemView.findViewById(R.id.trophy_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leaderboard_entry, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]
        holder.rankNumber.text = entry.rank.toString()
        holder.userAvatar.text = entry.avatar
        holder.userName.text = entry.name
        holder.userPoints.text = "${entry.points} points"

        holder.youBadge.visibility = if (entry.isYou) View.VISIBLE else View.GONE
        holder.trophyIcon.visibility = if (entry.rank <= 3) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int = entries.size
}


