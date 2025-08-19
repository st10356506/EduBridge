package com.example.edubridge

import Badge
import android. view.LayoutInflater
import android. view.View
import android. view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BadgesAdapter(private val badges: List<Badge>) : RecyclerView.Adapter<BadgesAdapter.VH>() {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: TextView = itemView.findViewById(R.id.badge_icon)
        val name: TextView = itemView.findViewById(R.id.badge_name)
        val description: TextView = itemView.findViewById(R.id.badge_description)
        val rarity: com.google.android.material.chip.Chip = itemView.findViewById(R.id.badge_rarity)
        val points: com.google.android.material.chip.Chip = itemView.findViewById(R.id.badge_points)
        val trophy: ImageView = itemView.findViewById(R.id.badge_trophy)
        val progressContainer: View = itemView.findViewById(R.id.badge_progress_container)
        val progressText: TextView = itemView.findViewById(R.id.badge_progress_text)
        val progressBar: android.widget.ProgressBar = itemView.findViewById(R.id.badge_progress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_badge, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val badge = badges[position]
        holder.icon.text = badge.icon
        holder.name.text = badge.name
        holder.description.text = badge.description
        holder.rarity.text = badge.rarity
        holder.points.text = "${badge.pointsRequired} pts"

        if (badge.isEarned) {
            holder.trophy.visibility = View.VISIBLE
            holder.progressContainer.visibility = View.GONE
        } else {
            holder.trophy.visibility = View.GONE
            holder.progressContainer.visibility = View.VISIBLE
            holder.progressBar.max = badge.pointsRequired
            holder.progressBar.progress = 0
            holder.progressText.text = "0/${badge.pointsRequired}"
        }
    }

    override fun getItemCount(): Int = badges.size
}


