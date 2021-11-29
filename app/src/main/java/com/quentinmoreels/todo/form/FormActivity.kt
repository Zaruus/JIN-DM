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

        val task = intent.getSerializableExtra("task") as? Task
        val taskFormTitle = task?.title ?: ""
        val taskFormDescription = task?.description ?: ""
        val id = task?.id ?: UUID.randomUUID().toString()
        val textFormTitle = findViewById<EditText>(R.id.editTextFormTitle)
        val textFormDescription = findViewById<EditText>(R.id.editTextFormDescription)

        textFormTitle.setText(taskFormTitle)
        textFormDescription.setText(taskFormDescription)

        val validateButton = findViewById<Button>(R.id.butValidate)
        validateButton.setOnClickListener { view ->
            val newTask = Task(id, title = textFormTitle.text.toString(), description = textFormDescription.text.toString())
            intent.putExtra("task", newTask)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}