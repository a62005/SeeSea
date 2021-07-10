package com.example.seesea.room.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.seesea.R
import com.example.seesea.databinding.FragmentRoomBinding
import com.example.seesea.room.NewMessage
import com.example.seesea.room.roomList.*
import com.example.seesea.roomDB.RoomDataViewModel
import com.example.seesea.user.*
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import java.text.SimpleDateFormat
import java.util.*

class RoomFragment : Fragment() {

    private lateinit var statusArr:Array<String>
    private val viewModel: RoomDataViewModel by activityViewModels()
    private var binding : FragmentRoomBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_room, container, false)
        binding = FragmentRoomBinding.bind(view)
        return view
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            statusArr = resources.getStringArray(R.array.activityStatus)
            binding.apply {
                activityCancelButton.visibility = View.INVISIBLE
                activityHostRightsButton.visibility = View.INVISIBLE
                buttonActivitySignup.visibility = View.INVISIBLE

                val messageAdapter = MessageAdapter(viewModel,activity as FragmentActivity)
                recyclerViewMessage.adapter = messageAdapter
                recyclerViewMessage.setItemViewCacheSize(Int.MAX_VALUE)
                viewModel.roomPage.currentRoomMessage.observe(activity as FragmentActivity){
                    messageAdapter.submitList(it.toList())
                    messageAdapter.notifyItemChanged(it.size)
                }

                val layoutManager = FlexboxLayoutManager(requireContext())
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.flexWrap = FlexWrap.WRAP
                layoutManager.justifyContent = JustifyContent.FLEX_START
                val applicationPhotoAdapter = ApplicantPhotoAdapter()
                recyclerViewSignupPpl.adapter = applicationPhotoAdapter
                recyclerViewSignupPpl.layoutManager = layoutManager

                viewModel.roomPage.currentRoom.observe(viewLifecycleOwner){ room->
                    Glide.with(requireContext()).load(room.hostImage).placeholder(R.drawable.activity_host_image).into(imageViewActivityHostImage)
                    textViewActivityHostName.text = room.hostName
                    textViewActivityName.text = room.activityName
                    textViewActivityStatus.text = statusArr[room.activityStatusCode]
                    textViewActivityDate.text = "${room.activityDate} ${room.activityTime}"
                    textViewActivityDivingType.text = room.divingType
                    textViewActivityDivingLevel.text = room.divingLevel
                    textViewActivityDivingArea.text = room.activityArea
                    textViewActivityDivingPlace.text = room.activityPlace
                    activityParticipantNumber.text = "${room.participantNumber} 人團"
                    textViewLackNumber.text = (room.participantNumber-room.currentParticipantNumber).toString()
                    textViewActivityTransportation.text = room.transportation
                    textViewActivityDescribe.text = room.activityDescription
                    textViewEstimateCostInput.text = room.estimateCost
                    activityPurpose.text = room.activityProperty
                    applicationPhotoAdapter.submitList(room.applicantImage)
                    if(room.activityStatusCode == HOLDING){
                        textViewActivityStatus.setTextColor(resources.getColor(R.color.salmon_pink,null))
                    }

                    viewModel.user.observe(viewLifecycleOwner){user->
                        Glide.with(requireContext()).load(user.userImage).placeholder(R.drawable.activity_host_image).into(messageUserImage)
                        if(user.userFavoriteRoom.contains(room.activityId)) {
                            imageViewActivityMyFavourite.setImageResource(R.drawable.list_heart_onclick)
                            room.isFavorite = true
                        }
                        var isGuest = true
                        if(user.userId == room.hostId){
                            viewModel.userPage.setUserType(USER_TYPE_LIST[0])
                            return@observe
                        }
                        if(user.userParticipatingActivity.contains(room.activityId)){
                            viewModel.userPage.setUserType(USER_TYPE_LIST[1])
                            isGuest = false
                        }
                        if(user.userSigningUpActivity.contains(room.activityId)){
                            viewModel.userPage.setUserType(USER_TYPE_LIST[2])
                            isGuest = false
                        }
                        if(isGuest){
                            viewModel.userPage.setUserType(USER_TYPE_LIST[3])
                        }
                    }
                    if(room.activityPicture == ""){
                        imageViewActivityImage.visibility = View.GONE
                    }else{
                        Glide.with(requireContext()).load(room.activityPicture).placeholder(R.drawable.mainlist_on_click).into(imageViewActivityImage)
                    }


                    viewModel.userPage.userType.observe(viewLifecycleOwner){type->
                        when(type){
                            USER_TYPE_LIST[0]-> {
                                activityHostRightsButton.visibility = View.VISIBLE
                                modifyButton.visibility = View.VISIBLE
                            }
                            USER_TYPE_LIST[1]-> activityHostRightsButton.visibility = View.VISIBLE
                            USER_TYPE_LIST[2]-> {
                                activityCancelButton.visibility = View.VISIBLE
                                buttonActivitySignup.visibility = View.INVISIBLE
                            }
                            USER_TYPE_LIST[3]-> {
                                if(room.activityStatusCode == 1) {
                                    buttonActivitySignup.visibility = View.VISIBLE
                                }
                                activityCancelButton.visibility = View.INVISIBLE
                                activityHostRightsButton.visibility = View.INVISIBLE
                            }
                        }
                    }
                }

                imageViewActivityMyFavourite.setOnClickListener {
                    viewModel.roomPage.currentRoom.observe(viewLifecycleOwner){room->
                        if(room.isFavorite){
                            imageViewActivityMyFavourite.setImageResource(R.drawable.activity_heart_unclick)
                            viewModel.roomListPage.deleteFavorite(room.activityId,room)
                        }else{
                            imageViewActivityMyFavourite.setImageResource(R.drawable.list_heart_onclick)
                            viewModel.roomListPage.addFavorite(room.activityId,room)
                        }
                        room.isFavorite = !room.isFavorite

                    }
                }
                val manager = parentFragmentManager
                val userDetailFragment = UserDetailFragment.newInstance(GUEST)
                textViewActivityHostName.setOnClickListener {
                    viewModel.roomPage.getHostData(viewModel.roomPage.currentRoom.value!!.hostId)
                    val transaction = manager.beginTransaction()
                    transaction.replace(R.id.layout_userDetail, userDetailFragment, UserDetailFragment::class.java.simpleName)
                        .addToBackStack(null)
                        .commit()
                }

                cardActivityHost.setOnClickListener {
                    viewModel.roomPage.getHostData(viewModel.roomPage.currentRoom.value!!.hostId)
                    val transaction = manager.beginTransaction()
                    transaction.replace(R.id.layout_userDetail, userDetailFragment, UserDetailFragment::class.java.simpleName)
                        .addToBackStack(null)
                        .commit()
                }

                imageViewActivityBack.setOnClickListener {
                    activity?.onBackPressed()
                }

                activityCancelButton.setOnClickListener {
                    val transaction = manager.beginTransaction()
                    transaction.replace(R.id.fragment_activitySignup, SignupFragment(), SignupFragment::class.java.simpleName)
                        .addToBackStack(null)
                        .commit()
                }

                buttonActivitySignup.setOnClickListener {
                    val transaction = manager.beginTransaction()
                    var hasExperience = false
                    viewModel.user.observe(viewLifecycleOwner){
                        if(!it.isAcccountActive){
                            layoutNotVerifiedYet.visibility = View.VISIBLE
                            hasExperience = true
                            return@observe
                        }
                        if(it.userExperience == USER_EXPERIENCE[0]){
                            val fragment = LowExperienceFragment.newInstance(SIGN_UP)
                            transaction.replace(R.id.fragment_activitySignup,fragment, LowExperienceFragment::class.java.simpleName)
                                .addToBackStack(null)
                                .commit()
                            hasExperience = true
                        }
                    }
                    if(hasExperience){
                        return@setOnClickListener
                    }
                    transaction.replace(R.id.fragment_activitySignup, SignupFragment(), SignupFragment::class.java.simpleName)
                        .addToBackStack(null)
                        .commit()
                }

                modifyButton.setOnClickListener {
                    val transaction = manager.beginTransaction()
                    transaction.replace(R.id.fragment_hostRights, ModifyActivityFragment(), ModifyActivityFragment::class.java.simpleName)
                        .addToBackStack(null)
                        .commit()

                }

                activityHostRightsButton.setOnClickListener {
                    val transaction = manager.beginTransaction()
                    transaction.replace(R.id.fragment_hostRights, HostRightsFragment(), HostRightsFragment::class.java.simpleName)
                        .addToBackStack(null)
                        .commit()
                }
                val t = SimpleDateFormat("yyyy-MM-dd HH:mm")
                messageSend.setOnClickListener {
                    var isAccountActive = false
                    viewModel.user.observe(viewLifecycleOwner) {
                        if (!it.isAcccountActive) {
                            layoutNotVerifiedYet.visibility = View.VISIBLE
                            return@observe
                        }
                        isAccountActive = true
                    }
                    if(!isAccountActive){
                        return@setOnClickListener
                    }
                    val date = Date(System.currentTimeMillis())
                    val time = t.format(date)
                    var message : NewMessage? = null
                    viewModel.user.observe(viewLifecycleOwner){user->
                        viewModel.roomPage.currentRoom.observe(viewLifecycleOwner){room->
                            message = NewMessage(
                                room.activityId,
                                user.userId,
                                messageMessageInput.text.toString(),
                                time
                            )

                        }

                    }
                    viewModel.roomPage.addMessage(message!!)
                    messageMessageInput.text.clear()
                }
                activityNotVerifiedYet.okay.setOnClickListener {
                    layoutNotVerifiedYet.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}