package com.quentinmoreels.todo.form

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.quentinmoreels.todo.R
import com.quentinmoreels.todo.tasklist.Task
import java.util.*

class FormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        val task = intent.getSerializableExtra("taskToModify") as? Task
        val taskFormTitle = task?.title ?: ""
        val taskFormDescription = task?.description ?: ""
        val id = task?.id ?: UUID.randomUUID().toString()
        val textFormTitle = findViewById<EditText>(R.id.editTextFormTitle)
        val textFormDescription = findViewById<EditText>(R.id.editTextFormDescription)

        textFormTitle.setText(taskFormTitle)
        textFormDescription.setText(taskFormDescription)

        val validateButton = findViewById<Button>(R.id.butValidate)
        validateButton.setOnClickListener { view ->
            val newTitle = textFormTitle.text.toString()
            val newDescription = textFormDescription.text.toString()

            //Si on ne modifie pas la description, le titre ne se met pas à jour
            //Je ne sais pas pourquoi
            val newTask = Task(id, title = newTitle, description = newDescription)

            intent.putExtra("taskModified", newTask)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}