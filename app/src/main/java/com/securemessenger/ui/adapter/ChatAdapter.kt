package com.securemessenger.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.securemessenger.data.model.Chat
import com.securemessenger.databinding.ItemChatBinding

class ChatAdapter(
    private val onItemClick: (Chat) -> Unit
) : ListAdapter<Chat, ChatAdapter.ChatViewHolder>(ChatDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChatViewHolder(
        private val binding: ItemChatBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(chat: Chat) {
            binding.apply {
                textViewChatName.text = chat.name
                textViewLastMessage.text = chat.lastMessage?.content ?: "No messages yet"
                textViewUnreadCount.text = if (chat.unreadCount > 0) chat.unreadCount.toString() else ""
                textViewUnreadCount.visibility = if (chat.unreadCount > 0) android.view.View.VISIBLE else android.view.GONE
                
                val iconRes = if (chat.isGroup) android.R.drawable.ic_menu_myplaces 
                             else android.R.drawable.ic_dialog_email
                imageViewAvatar.setImageResource(iconRes)
            }
        }
    }

    class ChatDiffCallback : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem == newItem
        }
    }
}
