package com.clearviewai.app

import com.clearviewai.app.data.local.entities.MediaItem
import org.junit.Assert.assertEquals
import org.junit.Test

class MediaItemTest {
    @Test
    fun testMediaItemCreation() {
        val item = MediaItem(
            id = 1L,
            uri = "content://media/1",
            type = "IMAGE",
            dateAdded = 123456789L,
            isBlurry = true,
            status = "TRASH"
        )
        assertEquals(1L, item.id)
        assertEquals("IMAGE", item.type)
        assertEquals(true, item.isBlurry)
        assertEquals("TRASH", item.status)
    }
}
