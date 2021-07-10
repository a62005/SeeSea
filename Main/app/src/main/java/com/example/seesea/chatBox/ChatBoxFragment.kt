package com.example.seesea.chatBox

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.seesea.R
import com.example.seesea.databinding.FragmentChatBoxBinding

//此為聊天室Fragment，尚未開發
class ChatBoxFragment : Fragment() {

    private var binding : FragmentChatBoxBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat_box, container, false)
        binding = FragmentChatBoxBinding.bind(view)
        // Inflate the layout for this fragment
        return view
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}