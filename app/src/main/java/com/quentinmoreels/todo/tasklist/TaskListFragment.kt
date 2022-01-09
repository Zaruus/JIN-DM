package com.quentinmoreels.todo.tasklist

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.quentinmoreels.todo.databinding.FragmentTaskListBinding
import com.quentinmoreels.todo.form.FormActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.quentinmoreels.todo.R
import com.quentinmoreels.todo.authentication.AuthenticationActivity
import com.quentinmoreels.todo.authentication.SHARED_PREF_TOKEN_KEY
import com.quentinmoreels.todo.network.Api
import com.quentinmoreels.todo.userinfo.UserInfoActivity
import com.quentinmoreels.todo.userinfo.UserInfoViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class TaskListFragment : Fragment() {
    private lateinit var binding: FragmentTaskListBinding
    private val taskListViewModel: TaskListViewModel by viewModels()
    private val userInfoViewModel: UserInfoViewModel by viewModels()

    //private val tasksRepository = TasksRepository()
    val adapterListener = TaskListListener1()

    inner class TaskListListener1  : TaskListListener {
        override fun onClickDelete(task: Task) {
            lifecycleScope.launch {
                taskListViewModel.deleteTask(task)
            }
        }

        override fun onClickModify(task: Task) {
            val intent = Intent(activity, FormActivity::class.java)
            intent.putExtra("taskToModify", task)
            formLauncher.launch(intent)
        }
    }

    val adapter = TaskListAdapter(adapterListener)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskListBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

        val defaultSharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        if (defaultSharedPreferences.getString(SHARED_PREF_TOKEN_KEY, "").toString() == "") {
            findNavController().navigate(R.id.action_TaskListFragment_to_AuthenticateFragment)
        }

        val actionButton = binding.floatingActionButton
        actionButton.setOnClickListener { view ->
            // Instanciation d'un objet task avec des données préremplies:
            val intent = Intent(activity, FormActivity::class.java)
            formLauncher.launch(intent)
        }

        binding.disconnectButton.setOnClickListener { view ->
            PreferenceManager.getDefaultSharedPreferences(context).edit {
                putString(SHARED_PREF_TOKEN_KEY, "")
            }
            findNavController().navigate(R.id.action_TaskListFragment_to_AuthenticateFragment)
        }

        lifecycleScope.launch { // on lance une coroutine car `collect` est `suspend`
            taskListViewModel.taskList.collect { newList ->
                adapter.submitList(newList)
            }
        }

        val intent = activity!!.intent
        when {
            intent?.action == Intent.ACTION_SEND -> {
                if ("text/plain" == intent.type) {
                    val intent2 = Intent(activity, FormActivity::class.java)
                    val task = intent.getStringExtra(Intent.EXTRA_TEXT)?.let { Task("", "", it) }
                    intent2.putExtra("task", task)
                    formLauncher.launch(intent2)
                    // Handle text being sent
                } else {
                    // Handle single
                }
            }
            else -> {
                // Handle other intents, such as being started from the home screen
            }
        }
    }

    override fun onResume() {
        taskListViewModel.loadTasks()
        super.onResume()

        val image_view = binding.avatarImageView
        lifecycleScope.launch { // on lance une coroutine car `collect` est `suspend`
            userInfoViewModel.userInfo.collect { newUserInfo ->
                userInfoViewModel.editUserInfo(newUserInfo)
                    image_view.load(userInfoViewModel.userInfo.value.avatar) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            }
        }

        image_view.setOnClickListener {
            val intent = Intent(activity, UserInfoActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            // Ici on ne va pas gérer les cas d'erreur donc on force le crash avec "!!"
            userInfoViewModel.userInfo.collect { newUserInfo ->
                userInfoViewModel.editUserInfo(newUserInfo)
                val userInfo = userInfoViewModel.userInfo.value
                val userInfoTextView = binding.textView3
                userInfoTextView.text = "${userInfo.firstName} ${userInfo.lastName}"
            }
        }

    }

    val formLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val recyclerView = binding.recyclerView
            val task = result.data?.getSerializableExtra("taskModified") as Task
            recyclerView.adapter = adapter

            val oldTask = taskListViewModel.taskList.value.firstOrNull { it.id == task.id }
            if (oldTask != null) {
                lifecycleScope.launch {
                    taskListViewModel.editTask(task)
                }
            } else {
                lifecycleScope.launch {
                    taskListViewModel.addTask(task)
                }
            }
        }
    }
}