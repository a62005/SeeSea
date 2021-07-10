package com.example.seesea.room.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.seesea.R
import com.example.seesea.databinding.FragmentSignupBinding
import com.example.seesea.room.roomList.USER_TYPE_LIST
import com.example.seesea.roomDB.RoomDataViewModel
import java.text.SimpleDateFormat
import java.util.*

private const val PARTICIPANT_ID = "id"

class SignupFragment : Fragment() {

    private val viewModel: RoomDataViewModel by activityViewModels()
    private var binding : FragmentSignupBinding? = null
    private var participantId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            participantId = it.getString(PARTICIPANT_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signup, container, false)
        binding = FragmentSignupBinding.bind(view)
        return view
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            buttonSignupBackButton.setOnClickListener{
                activity?.onBackPressed()
            }
            editTextSignupDescribe.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}//輸入後的監聽
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}//輸入後的監聽
                @SuppressLint("SetTextI18n")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    textViewSignupDescribeWordNumber.text = "${start - before + 1}/200"

                }//輸入文字產生變化的監聽
            })
            viewModel.userPage.userType.observe(viewLifecycleOwner){
                if(it == USER_TYPE_LIST[0]){
                    editTextSignupDescribe.hint = "有什麼想對參加者說的嗎"
                }
            }

            val t = SimpleDateFormat("yyyy/MM/dd HH:mm")
            buttonSignupDecideButton.setOnClickListener {
                viewModel.userPage.userType.observe(viewLifecycleOwner){
                    viewModel.roomPage.currentRoom.observe(viewLifecycleOwner){room->
                        if(it == USER_TYPE_LIST[0]){
                            viewModel.userPage.quitParticipant(participantId!!.toInt())
                            viewModel.userPage.getParticipantUser()
                        }
                        if(it == USER_TYPE_LIST[1]){
                            viewModel.roomPage.pullOutActivity()
                            viewModel.roomPage.getRoomById(room.activityId)
                            viewModel.userPage.setUserType(USER_TYPE_LIST[3])
                            val transaction = activity?.supportFragmentManager!!.beginTransaction()
                            transaction.replace(R.id.layout_main, RoomFragment(), RoomFragment::class.java.simpleName)
                                .addToBackStack(null)
                                .commit()
                        }
                        if(it == USER_TYPE_LIST[2]){
                            viewModel.roomPage.quitSignUp()
                            viewModel.roomPage.getRoomById(room.activityId)
                            viewModel.userPage.setUserType(USER_TYPE_LIST[3])
                            activity?.onBackPressed()
                        }
                        if(it == USER_TYPE_LIST[3]){
                            val date = Date(System.currentTimeMillis())
                            val time = t.format(date)
                            viewModel.roomPage.signUp(room.activityId,"${editTextSignupDescribe.text}",time)
                            viewModel.roomPage.getRoomById(room.activityId)
                        }
                        viewModel.userPage.setUserType(USER_TYPE_LIST[2])
                    }
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
        fun newInstance(participantId: Int) =
            SignupFragment().apply {
                arguments = Bundle().apply {
                    putString(PARTICIPANT_ID,participantId.toString())
                }
            }
    }

}