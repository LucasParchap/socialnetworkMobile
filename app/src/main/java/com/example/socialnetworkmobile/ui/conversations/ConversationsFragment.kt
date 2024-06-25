package com.example.socialnetworkmobile.ui.conversations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.socialnetworkmobile.databinding.FragmentConversationsBinding

class ConversationsFragment : Fragment() {

    private var _binding: FragmentConversationsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val conversationsViewModel =
            ViewModelProvider(this).get(ConversationsViewModel::class.java)

        _binding = FragmentConversationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textConversations
        conversationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}