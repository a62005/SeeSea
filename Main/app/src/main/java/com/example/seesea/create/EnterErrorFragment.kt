package com.example.seesea.create

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.seesea.R
import com.example.seesea.databinding.FragmentEnterErrorBinding

//報錯頁面，但用一個layout即可呈現
class EnterErrorFragment : Fragment() {

    private var binding : FragmentEnterErrorBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_enter_error, container, false)
        binding = FragmentEnterErrorBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            buttonEErrorOkButton.setOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}