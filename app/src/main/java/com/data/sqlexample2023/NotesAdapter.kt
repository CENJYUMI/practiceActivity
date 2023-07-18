package com.data.sqlexample2023

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.data.sqlexample2023.databinding.NoteItemLayoutBinding

class NotesAdapter (private val notes: List<Note>) :RecyclerView.Adapter<NoteItemViewHolder>(){

    var onDeleteClick :((Note) -> Unit)? = null
    var onUpdateClick :((Note) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NoteItemLayoutBinding.inflate(inflater,parent, false)
        return NoteItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NoteItemViewHolder, position: Int) {
        holder.bind(notes[position])
        holder.binding.apply {
            edtDelete.setOnClickListener{
                onDeleteClick ?.invoke(notes[position])
            }
            edtUpdate.setOnClickListener{
                onUpdateClick ?.invoke(notes[position])
            }
        }

    }
}