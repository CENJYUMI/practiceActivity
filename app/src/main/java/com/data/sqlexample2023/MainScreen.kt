package com.data.sqlexample2023

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.data.sqlexample2023.databinding.ActivityMainScreenBinding
import com.data.sqlexample2023.databinding.DialogLayoutBinding

class MainScreen : AppCompatActivity() {
    //global
    private lateinit var binding : ActivityMainScreenBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteList:MutableList<Note>
    private lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //object instantiation
        databaseHelper = DatabaseHelper(this)

        //setup the recyclerview
        recyclerView = binding.recyclerView
        //add layout to recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)

        //declare data
        noteList = getData()
        //initialize adapter object
        adapter = NotesAdapter(noteList)
        adapter.onDeleteClick = {note ->
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Delete Note")
            alertDialogBuilder.setMessage("Are you sure you want to delete this note?")

            alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                //delete from database
                delete(note.id)
                //delete from list
                noteList.remove(note)
                //notify the adapter that dat has changed
                adapter.notifyDataSetChanged()

            }
            alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            val alertDialog: AlertDialog = alertDialogBuilder.create()
            alertDialog.show()

        }
        adapter.onUpdateClick = {note ->
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Update Note")
            alertDialogBuilder.setMessage("Enter the updated details for the note:")

            val dialogLayout = layoutInflater.inflate(R.layout.dialog_layout, null)
            val dialogBinding = DialogLayoutBinding.bind(dialogLayout)
            alertDialogBuilder.setView(dialogLayout)

            dialogBinding.etDialogTitle.setText(note.title)
            dialogBinding.etDialogContent.setText(note.content)

            alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                val title = dialogBinding.etDialogTitle.text.toString()
                val content = dialogBinding.etDialogContent.text.toString()

                val newNote = Note(note.id,title,content)
                update(newNote)

                //find the index of the viewHolder in the recyclerview
                val updateNotePosition = noteList.indexOfFirst { it.id == note.id }
                if(updateNotePosition != -1){
                    noteList[updateNotePosition] = newNote
                    adapter.notifyItemChanged(updateNotePosition)
                }


            }
            alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            val alertDialog: AlertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        recyclerView.adapter = adapter


        //add item
        binding.floatingActionButton.setOnClickListener{
            showAddDialog()
        }
    }

    private fun showAddDialog(){
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Add New Note")

        val dialogLayout = layoutInflater.inflate(R.layout.dialog_layout, null)
        val dialogBinding = DialogLayoutBinding.bind(dialogLayout)
        alertDialogBuilder.setView(dialogLayout)

        alertDialogBuilder.setPositiveButton("OK"){ dialog, _ ->
            val title = dialogBinding.etDialogTitle.text.toString()
            val content = dialogBinding.etDialogContent.text.toString()

            var newNote = Note(0,title,content)
            //add new data to database table
            addData(newNote)
            //add new note to list
            noteList.add(newNote)
            //notify adapter that data has changed
            recyclerView.adapter?.notifyDataSetChanged()
            dialog.dismiss()

        }

        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


    private fun getData(): MutableList<Note> {
        return databaseHelper.getAllNotes()
    }

    private fun addData(note:Note){
        databaseHelper .insertNote(note)
        Toast.makeText(applicationContext, "New Note Added!", Toast.LENGTH_LONG).show()

    }

    private fun update(note:Note){
        databaseHelper.updateData(note)
        getData()
        Toast.makeText(applicationContext, "Note Updated!", Toast.LENGTH_LONG).show()

    }
    private fun delete ( id: Int){
        databaseHelper.deleteData(id)
        getData()
        Toast.makeText(applicationContext, "Note Deleted!", Toast.LENGTH_LONG).show()
    }
}