package com.example.seesea.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.seesea.R
import com.example.seesea.databinding.FragmentUserDetailBinding
import com.example.seesea.roomDB.RoomDataViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

const val HOST = "USER"
const val GUEST = "GUEST"
const val IDENTITY = "IDENTITY"


class UserDetailFragment : Fragment() {
    private val viewModel: RoomDataViewModel by activityViewModels()
    private var binding : FragmentUserDetailBinding? = null
    private var identity :String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            identity = it.getString(IDENTITY)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val view = inflater.inflate(R.layout.fragment_user_detail, container, false)
        binding = FragmentUserDetailBinding.bind(view)
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val divingTagAdapter = DivingTagAdapter(viewModel, isModify = false, isLastTag = false)
        binding?.apply {
            textViewUserUserNickName.isEnabled = false
            textViewUserPhoneInput.isEnabled = false
            textViewUserDescribe.isEnabled = false
            textView36.visibility = View.GONE
            activity?.apply {
                viewModel.userPage.guest.observe(viewLifecycleOwner){ guest->
                    if(guest.userId == viewModel.userId){
                        identity = HOST
                    }
                }
                val layoutManager = FlexboxLayoutManager(baseContext)
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.flexWrap = FlexWrap.WRAP
                layoutManager.justifyContent = JustifyContent.FLEX_START
                recyclerViewUserTag.adapter = divingTagAdapter
                recyclerViewUserTag.layoutManager = layoutManager

                when(identity){
                    HOST->{
                        viewModel.user.observe(viewLifecycleOwner){ user->
                            textViewUserUserNickName.setText(user.userNickName)
                            textViewUserUserName.text = user.userName
                            textViewUserAge.text = "${user.userAge}"
                            textViewUserPhoneInput.setText("0${user.userPhone}")
                            textViewUserEmailInput.text = user.userEmail
                            textViewUserDescribe.setText(user.userDescription)
                            Glide.with(this.application).load(user.userImage).into(imageViewModifyUserImage)
                            val str = mutableListOf<String>()
                            str.addAll(user.divingTypeTag)
                            str.addAll(user.areaTag)
                            divingTagAdapter.submitList(str)
                        }
                    }
                    GUEST->{
                        viewModel.userPage.guest.observe(viewLifecycleOwner){ user->
                            if(user == null){
                                viewModel.roomPage.getHostData(viewModel.userId)
                            }
                            textViewUserUserName.visibility = View.GONE
                            textViewUserNameStatus.visibility = View.GONE
                            textViewUserPhone.visibility = View.GONE
                            textViewUserPhoneInput.visibility = View.GONE
                            textViewUserEmail.visibility = View.GONE
                            textViewUserEmailInput.visibility = View.GONE
                            userDetailModifyButton.visibility = View.GONE

                            textViewUserUserNickName.setText(user.userNickName)
                            textViewUserAge.text = "${user.userAge}"
                            textViewUserDescribe.setText(user.userDescription)
                            Glide.with(this.application).load(user.userImage).into(imageViewModifyUserImage)
                            val str = mutableListOf<String>()
                            str.addAll(user.divingTypeTag)
                            str.addAll(user.areaTag)
                            divingTagAdapter.submitList(str)
                        }
                    }
                }

            }
            userDetailModifyButton.setOnClickListener {
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_userDetail, ModifyUserDetailFragment(), ModifyUserDetailFragment::class.java.simpleName)
                    .addToBackStack(null)
                    .commit()
            }
            userDetailBackButton.setOnClickListener {
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
        fun newInstance(identity: String) =
            UserDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(IDENTITY, identity)
                }
            }
    }

}