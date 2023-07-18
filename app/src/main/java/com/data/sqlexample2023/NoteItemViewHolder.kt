package com.data.sqlexample2023

import androidx.recyclerview.widget.RecyclerView
import com.data.sqlexample2023.databinding.NoteItemLayoutBinding

class NoteItemViewHolder ( val binding:NoteItemLayoutBinding):RecyclerView.ViewHolder(binding.root) {
    fun bind(note:Note){
        binding.tvTitleRecycler.text = note.title
        binding.tvContentRecycler.text = note.content
    }


}