package com.securemessenger.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.securemessenger.databinding.FragmentChatListBinding
import com.securemessenger.ui.adapter.ChatAdapter
import com.securemessenger.ui.viewmodel.ChatListViewModel

class ChatListFragment : Fragment() {

    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ChatListViewModel
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ChatListViewModel::class.java]
        setupRecyclerView()
        observeViewModel()

        binding.fabCreateGroup.setOnClickListener {
            findNavController().navigate(
                com.securemessenger.R.id.action_chatListFragment_to_createGroupFragment
            )
        }
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter { chat ->
            // Navigate to chat detail
            val action = ChatListFragmentDirections.actionChatListFragmentToChatFragment(
                chatId = chat.id,
                chatName = chat.name
            )
            findNavController().navigate(action)
        }

        binding.recyclerViewChats.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.chats.observe(viewLifecycleOwner) { chats ->
            chatAdapter.submitList(chats)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
