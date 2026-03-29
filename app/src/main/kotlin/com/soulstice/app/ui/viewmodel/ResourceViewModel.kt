package com.soulstice.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulstice.app.data.local.dao.SoulsticeDao
import com.soulstice.app.data.local.entities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ResourceViewModel @Inject constructor(
    private val dao: SoulsticeDao
) : ViewModel() {

    val allResources = dao.getAllResources()

    fun getResourcesByProject(projectId: String) = dao.getResourcesByProject(projectId)

    fun addResource(
        title: String,
        type: String,
        content: String? = null,
        url: String? = null,
        localUri: String? = null,
        projectId: String? = null
    ) {
        viewModelScope.launch {
            val resource = Resource(
                id = UUID.randomUUID().toString(),
                title = title,
                type = type,
                content = content,
                url = url,
                localUri = localUri,
                projectId = projectId,
                createdAt = System.currentTimeMillis()
            )
            dao.insertResource(resource)
        }
    }

    fun deleteResource(resource: Resource) {
        viewModelScope.launch {
            dao.deleteResource(resource)
        }
    }
}
