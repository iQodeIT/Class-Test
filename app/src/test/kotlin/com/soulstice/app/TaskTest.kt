package com.soulstice.app

import com.soulstice.app.data.local.entities.Task
import org.junit.Assert.assertEquals
import org.junit.Test

class TaskTest {
    @Test
    fun testTaskCreation() {
        val task = Task(
            id = "1",
            title = "Test Task",
            description = "Description",
            status = "todo",
            priority = "high",
            energy = "high",
            projectId = null,
            dueDate = null,
            createdAt = 123456789L
        )
        assertEquals("Test Task", task.title)
        assertEquals("todo", task.status)
        assertEquals("high", task.energy)
    }
}
