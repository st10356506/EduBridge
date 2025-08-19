package com.example.edubridge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class SimpleReward(val icon: String, val name: String, val description: String, val cost: Int)

class SimpleRewardsAdapter(private val rewards: List<SimpleReward>) : RecyclerView.Adapter<SimpleRewardsAdapter.VH>() {
    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: TextView = itemView.findViewById(R.id.reward_icon)
        val name: TextView = itemView.findViewById(R.id.reward_name)
        val desc: TextView = itemView.findViewById(R.id.reward_description)
        val cost: TextView = itemView.findViewById(R.id.reward_cost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_reward, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val r = rewards[position]
        holder.icon.text = r.icon
        holder.name.text = r.name
        holder.desc.text = r.description
        holder.cost.text = "${r.cost} points"
    }

    override fun getItemCount(): Int = rewards.size
}


