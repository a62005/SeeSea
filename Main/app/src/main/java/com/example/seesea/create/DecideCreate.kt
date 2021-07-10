package com.example.seesea.create

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.seesea.MainActivity
import com.example.seesea.R
import com.example.seesea.databinding.FragmentDecideCreateBinding
import com.example.seesea.roomDB.RoomDataViewModel
import java.io.File

class DecideCreate : Fragment() {

    private val viewModel: RoomDataViewModel by activityViewModels()
    private var binding : FragmentDecideCreateBinding? = null
    private var isCreated = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_decide_create, container, false)
        binding = FragmentDecideCreateBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.mapPage.loadingDivingPoint()
        val divingTypeArr = resources.getStringArray(R.array.create_divingType)
        val scubaLevelArr = resources.getStringArray(R.array.create_divingLevelByScuba)
        val freeLevelArr = resources.getStringArray(R.array.create_divingLevelByFree)
        val areaArr = resources.getStringArray(R.array.create_divingLocation)
        val transportationArr = resources.getStringArray(R.array.create_transportation)
        val costArr = resources.getStringArray(R.array.create_estimateCost)
        val purposeArr = resources.getStringArray(R.array.create_purpose)
        binding?.apply {
            fragmentCreateRoom.buttonActivitySignup.visibility = View.GONE
            fragmentCreateRoom.activityHostRightsButton.visibility = View.GONE
            fragmentCreateRoom.activityCancelButton.visibility = View.GONE

            //isCreated是為避免點選送出後還能按其他按鍵操作
            buttonConfirmCancelButton.setOnClickListener {
                if(isCreated){
                    return@setOnClickListener
                }
                activity?.onBackPressed()
            }
            buttonConfirmDecideCreateButton.setOnClickListener {
                if(isCreated){
                    return@setOnClickListener
                }
                isCreated= true
                viewModel.createRoomPage.postPreviewRoom()
                layoutConfirmSuccess.visibility = View.VISIBLE
            }
            buttonConfirm.setOnClickListener {
                    val intent = Intent()
                    activity?.let {activity-> intent.setClass(activity, MainActivity::class.java)
                        startActivity(intent)
                        activity.onBackPressed()
                        activity.finish()
                    }
            }

            //設定頁面顯示內容
            fragmentCreateRoom.apply {
                buttonActivitySignup.visibility = View.GONE
                viewModel.createRoomPage.previewRoom.observe(viewLifecycleOwner){ room->
                    viewModel.user.observe(viewLifecycleOwner){user->
                        textViewActivityHostName.text = user.userNickName
                    }
                    textViewActivityName.text = room.activityName
                    textViewActivityDate.text = room.activityDateTime
                    textViewActivityDivingType.text =when(room.divingTypeCode){
                        1->divingTypeArr[1]
                        2->divingTypeArr[2]
                        else -> divingTypeArr[0]
                    }
                    textViewActivityDivingLevel.text = when(room.divingTypeCode){
                        1->freeLevelArr[room.divingLevelCode]
                        2->scubaLevelArr[room.divingLevelCode]
                        else -> scubaLevelArr[room.divingLevelCode]
                    }
                    textViewActivityDivingArea.text = areaArr[room.activityAreaCode]
                    textViewActivityDivingPlace.text = room.activityPlace
                    activityParticipantNumber.text = "${room.participantNumber}"
                    textViewLackNumber.text = "${room.participantNumber}"
                    textViewActivityTransportation.text = transportationArr[room.transportationCode]
                    textViewActivityDescribe.text = room.activityDescription
                    textViewEstimateCostInput.text = costArr[room.estimateCostCode]
                    activityPurpose.text = purposeArr[room.activityPropertyCode]

                    textViewActivityStatus.setTextColor(resources.getColor(R.color.salmon_pink,null))
                    val uri = room.activityPicture.absolutePath
                    Glide.with(requireActivity().applicationContext).load(uri).placeholder(R.drawable.mainlist_on_click).into(imageViewActivityImage)
                }
                viewModel.user.observe(viewLifecycleOwner){user->
                    Glide.with(requireActivity().applicationContext).load(user.userImage).placeholder(R.drawable.mainlist_on_click).into(imageViewActivityHostImage)
                    textViewActivityHostName.text = user.userNickName
                }
            }

        }

    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}