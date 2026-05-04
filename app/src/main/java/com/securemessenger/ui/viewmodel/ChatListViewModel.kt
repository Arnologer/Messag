package com.securemessenger.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.securemessenger.data.model.Chat
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatListViewModel : ViewModel() {

    private val _chats = MutableLiveData<List<Chat>>()
    val chats: LiveData<List<Chat>> = _chats

    init {
        loadChats()
    }

    private fun loadChats() {
        viewModelScope.launch {
            // TODO: Inject repository and collect from Flow
            // chatRepository.getAllChats().collectLatest { chatList ->
            //     _chats.value = chatList
            // }
            
            // Mock data for prototype
            _chats.value = listOf(
                Chat(
                    id = "1",
                    name = "John Doe",
                    participants = listOf("user1", "user2"),
                    isGroup = false,
                    unreadCount = 2
                ),
                Chat(
                    id = "2",
                    name = "Project Team",
                    participants = listOf("user1", "user2", "user3", "user4"),
                    isGroup = true,
                    groupAdminId = "user1",
                    unreadCount = 5
                )
            )
        }
    }

    fun createGroup(name: String, participants: List<String>) {
        viewModelScope.launch {
            // TODO: Implement group creation logic
            // chatRepository.createGroup(name, participants)
        }
    }
}
