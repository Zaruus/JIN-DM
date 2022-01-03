package com.quentinmoreels.todo.tasklist

interface TaskListListener {
    fun onClickDelete(task: Task) = Unit
    fun onClickModify(task: Task) = Unit
}