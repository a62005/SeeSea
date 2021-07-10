package com.example.seesea.room.activity

import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.seesea.R
import com.example.seesea.databinding.FragmentModifyActivityBinding
import com.example.seesea.room.ModifyRoom
import com.example.seesea.roomDB.RoomDataViewModel
import java.io.File


class ModifyActivityFragment : Fragment() {
    private val pickImage = 100
    private val viewModel: RoomDataViewModel by activityViewModels()
    private var binding : FragmentModifyActivityBinding? = null
    private var image:File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewModel.mapPage.loadingDivingPoint()
        val view = inflater.inflate(R.layout.fragment_modify_activity, container, false)
        binding = FragmentModifyActivityBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gray = resources.getColor(R.color.gray_light_more,null)
        val freeLevel = resources.getStringArray(R.array.create_divingLevelByFree)
        val scubaLevel = resources.getStringArray(R.array.create_divingLevelByScuba)
        val area = resources.getStringArray(R.array.create_divingLocation)
        val transportation = resources.getStringArray(R.array.create_transportation)
        val purpose = resources.getStringArray(R.array.create_purpose)
        val cost = resources.getStringArray(R.array.create_estimateCost)
        binding?.apply {

            modifyActivityBack.setOnClickListener {
                activity?.onBackPressed()
            }

            includeModifyActivity.apply {
                chipGroup.children.forEach { it.isEnabled = false }
                spinnerCreateArea.isEnabled = false
                editTextCreateParticipatePeople.isEnabled = false
                editTextCreateDate.isEnabled = false
                spinnerCreatePurpose.isEnabled = false
                textViewDivingType.setTextColor(gray)
                textViewArea.setTextColor(gray)
                textViewPplNumber.setTextColor(gray)
                textViewDate.setTextColor(gray)
                textViewPurpose.setTextColor(gray)


                viewModel.roomPage.currentRoom.observe(viewLifecycleOwner){room->
                    when(room.divingType){
                        this@ModifyActivityFragment.resources.getString(R.string.create_divingFree)->{
                            spinnerCreateDivingLevel.adapter = ArrayAdapter.createFromResource(requireContext().applicationContext,
                                R.array.create_divingLevelByFree,android.R.layout.simple_spinner_item)
                            spinnerCreateDivingLevel.setSelection(freeLevel.indexOf(room.divingLevel))
                            createFreeDivingChip.isChecked = true
                        }
                        this@ModifyActivityFragment.resources.getString(R.string.create_divingScuba)->{
                            spinnerCreateDivingLevel.adapter = ArrayAdapter.createFromResource(requireContext().applicationContext,
                                R.array.create_divingLevelByScuba,android.R.layout.simple_spinner_item)
                            spinnerCreateDivingLevel.setSelection(scubaLevel.indexOf(room.divingLevel))
                            createScubaDivingChip.isChecked = true
                        }
                    }
                    editTextCreateDate.text = room.activityDate
                    spinnerCreateArea.setSelection(area.indexOf(room.activityArea))

                    spinnerCreateArea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                            val selectedItem = parent.getItemAtPosition(position).toString()
                            viewModel.createRoomPage.getDivingPlaceByArea(selectedItem)
                        }
                        override fun onNothingSelected(parent: AdapterView<*>) {
                        }
                    }
                    viewModel.createRoomPage.divingPlace.observe(viewLifecycleOwner){
                        val arrayAdapter = ArrayAdapter(requireContext().applicationContext,android.R.layout.simple_spinner_dropdown_item,it)
                        spinnerCreatePlace.adapter = arrayAdapter
                        spinnerCreatePlace.setSelection(it.indexOf(room.activityPlace))
                    }
                    editTextCreateRoomName.setText(room.activityName)
                    editTextCreateParticipatePeople.setText(room.participantNumber.toString())
                    spinnerCreatePurpose.setSelection(purpose.indexOf(room.activityProperty))
                    spinnerCreateTransportation.setSelection(transportation.indexOf(room.transportation))
                    spinnerCreateEstimateCost.setSelection(cost.indexOf(room.estimateCost))
                    Glide.with(requireContext()).load(room.activityPicture).placeholder(R.drawable.mainlist_on_click).into(createImage)
                    editTextCreateDescribe.setText(room.activityDescription)
                    createImage.setOnClickListener {
                        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(intent, pickImage)
                    }

                }

                buttonCreatePreviewButton.text = "完成"
                buttonCreatePreviewButton.setOnClickListener {
                    layoutConfirmSuccess.visibility = View.VISIBLE
                    var id = 0
                    viewModel.roomPage.currentRoom.observe(viewLifecycleOwner){
                        id = it.activityId
                    }
                    if(image == null)image = File("")
                    val modifyRoom = ModifyRoom(
                        id,
                        editTextCreateRoomName.text.toString(),
                        image!!,
                        spinnerCreateDivingLevel.selectedItemPosition,
                        spinnerCreatePlace.selectedItemPosition,
                        spinnerCreateTransportation.selectedItemPosition,
                        editTextCreateDescribe.text.toString(),
                        "",
                        spinnerCreateEstimateCost.selectedItemPosition
                    )
                    viewModel.roomPage.modifyRoom(modifyRoom)
                }

                buttonConfirm.setOnClickListener {
                    activity?.onBackPressed()
                }
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == pickImage && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            val currentPictureUri = data.data
            Glide.with(this).load(currentPictureUri).into(binding!!.includeModifyActivity.createImage)
            currentPictureUri?.apply {
                //从系统表中查询指定Uri对应的照片
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = activity?.contentResolver?.query(currentPictureUri, filePathColumn, null, null, null)
                cursor?.moveToFirst()
                val path = cursor?.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)) as String
                cursor.close()
                val file = File(path)
                image =  file

                if(Build.VERSION.SDK_INT < 29){
                    RoomDataViewModel.bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver,this)
                }else{
                    val source = ImageDecoder.createSource(activity?.contentResolver!!,this)
                    RoomDataViewModel.bitmap = ImageDecoder.decodeBitmap(source)
                }
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}