package com.example.seesea.user

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.seesea.R
import com.example.seesea.databinding.FragmentLowExperienceBinding
import com.example.seesea.roomDB.RoomDataViewModel

const val CREATE = "CREATE"
const val SIGN_UP = "SIGN"
const val KEY="KEY"

class LowExperienceFragment : Fragment() {

    private val viewModel: RoomDataViewModel by activityViewModels()
    private var binding : FragmentLowExperienceBinding? = null
    private var hasExperience = false
    private var key: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            key = it.getString(KEY)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_low_experience, container, false)
        binding = FragmentLowExperienceBinding.bind(view)
        return view
    }

    @SuppressLint("UseCompatLoadingForDrawables", "UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {

            back.setOnClickListener {
                activity?.onBackPressed()
            }
            checkBoxNoSign.setOnClickListener {
                checkBoxNoSign.visibility = View.INVISIBLE
                checkBoxSign.visibility = View.VISIBLE
                hasExperience = true
            }
            checkBoxSign.setOnClickListener {
                checkBoxNoSign.visibility = View.VISIBLE
                checkBoxSign.visibility = View.INVISIBLE
                hasExperience = false
            }
            okay.setOnClickListener {
                if(hasExperience){
                    viewModel.userPage.updateExperience()
                }
                activity?.onBackPressed()
            }

        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    companion object {
        @JvmStatic
        fun newInstance(key: String) =
            LowExperienceFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY,key)
                }
            }
    }
}