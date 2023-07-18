package com.data.sqlexample2023

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.data.sqlexample2023.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)
        viewData()

        binding.button.setOnClickListener{
            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()

            addData(title, content)
        }

        binding.buttonUpdate.setOnClickListener{
            val id = binding.etID.text.toString().toInt()
            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()
            update(id,title, content)

        }
        binding.buttonDelete.setOnClickListener {
            val id = binding.etID.text.toString().toInt()
            delete(id)
        }


    }

    private fun viewData(){
        var allDAta = databaseHelper.getAllNotes()

        var resultString = ""

        for(note in allDAta){
            resultString += "Note : ${note.id} ${note.title} ${note.content}\n"

        }
        binding.txtNote.text = resultString
    }
    private fun addData(title:String, content: String){
        val sampleNote = Note(0, title, content)
        databaseHelper .insertNote(sampleNote)
        viewData()
        Toast.makeText(applicationContext, "New Note Added!", Toast.LENGTH_LONG).show()

    }

    private fun update(id: Int, title: String, content: String){
        val noteObject = Note (id, title, content)
        databaseHelper.updateData(noteObject)
        viewData()
        Toast.makeText(applicationContext, "Note Updated!", Toast.LENGTH_LONG).show()

    }
    private fun delete ( id: Int){
        databaseHelper.deleteData(id)
        viewData()
        Toast.makeText(applicationContext, "Note Deleted!", Toast.LENGTH_LONG).show()
    }


}