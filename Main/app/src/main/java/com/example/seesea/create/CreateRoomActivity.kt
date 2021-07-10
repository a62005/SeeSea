package com.example.seesea.create

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.ImageDecoder
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.seesea.R
import com.example.seesea.SharedPref
import com.example.seesea.databinding.ActivityCreateRoomBinding
import com.example.seesea.room.PreviewRoom
import com.example.seesea.roomDB.RoomDataApplication
import com.example.seesea.roomDB.RoomDataViewModel
import com.example.seesea.roomDB.RoomDataViewModelFactory
import java.io.File
import java.util.*

//創建房間頁面
class CreateRoomActivity : AppCompatActivity(){

    private val pickImage = 100
    private var image:File? = null
    private var isTimeCorrect = false
    private var dateTime :String = ""
    private val viewModel: RoomDataViewModel by viewModels {
        RoomDataViewModelFactory((application as RoomDataApplication).repository)
    }
    private val binding by lazy { ActivityCreateRoomBinding.inflate(layoutInflater) }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val user = SharedPref.getInstance(applicationContext).getUser()!!
        viewModel.loginPage.setUser(user)
        viewModel.mapPage.loadingDivingPoint()

        val roomName = binding.editTextCreateRoomName
        val pNumber = binding.editTextCreateParticipatePeople
        val divingType = binding.chipGroup
        val divingLevel = binding.spinnerCreateDivingLevel
        val purpose = binding.spinnerCreatePurpose
        val date = binding.editTextCreateDate
        val area = binding.spinnerCreateArea
        val place = binding.spinnerCreatePlace
        val transportation = binding.spinnerCreateTransportation
        val describe = binding.editTextCreateDescribe
        val estimate = binding.spinnerCreateEstimateCost

        divingType.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.create_scubaDivingChip->divingLevel.adapter = ArrayAdapter.createFromResource(applicationContext,
                    R.array.create_divingLevelByScuba,android.R.layout.simple_spinner_item)
                R.id.create_freeDivingChip->divingLevel.adapter = ArrayAdapter.createFromResource(applicationContext,
                    R.array.create_divingLevelByFree,android.R.layout.simple_spinner_item)
            }
        }

        binding.apply {
            textViewCreateImagePickButton.setOnClickListener{
                val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, pickImage)
            }
            createImage.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, pickImage)
            }

            imageViewCreateCancelButton.setOnClickListener {
                this@CreateRoomActivity.finish()
            }
            date.setOnClickListener {
                dateTimePicker.visibility = View.VISIBLE
                dateTimeComplete.visibility = View.VISIBLE
            }

            //控制出團時間不能為過往，但未來則無限制
            val calendar = Calendar.getInstance(Locale.ENGLISH)
            dateTimePicker.setOnDateTimeChangedListener {
                val currentTime = Date(System.currentTimeMillis())
                if(it - currentTime.time <0){
                    dateTimePicker.setThemeColor(resources.getColor(R.color.gray_light_more,null))
                    isTimeCorrect = false
                    return@setOnDateTimeChangedListener
                }
                isTimeCorrect = true
                dateTimePicker.setThemeColor(resources.getColor(R.color.blue,null))
                calendar.timeInMillis = it
                dateTime = DateFormat.format("yyyy/MM/dd HH:mm",calendar.timeInMillis).toString()
            }

            dateTimeComplete.setOnClickListener {
                if(isTimeCorrect){
                    editTextCreateDate.text = dateTime
                    dateTimePicker.visibility = View.GONE
                    dateTimeComplete.visibility = View.GONE
                }
            }

            describe.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}//輸入後的監聽
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}//輸入後的監聽
                @SuppressLint("SetTextI18n")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    textViewCreateDescribeWordNumber.text = "${start - before + 1}/200"

                }//輸入文字產生變化的監聽
            })

            //把選擇的area丟進filter
            area.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    viewModel.createRoomPage.getDivingPlaceByArea(selectedItem)
                }
                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }

            //當user選擇area後，會從maps filter該地區的所有place
            viewModel.createRoomPage.divingPlace.observe(this@CreateRoomActivity){
                val arrayAdapter = ArrayAdapter(this@CreateRoomActivity,android.R.layout.simple_spinner_dropdown_item,it)
                place.adapter = arrayAdapter
            }

            //輸入完成後會判斷若必填欄位有未輸入的則跳提示，否則包成PreviewRoom類別並顯示至DecideCreate
            val manager = supportFragmentManager
            buttonCreatePreviewButton.setOnClickListener {
                if(roomName.text.toString() == "" || pNumber.text.toString() == "" || divingType.checkedChipId == 0
                    || divingLevel.selectedItemPosition == 0 || !isTimeCorrect
                    || area.selectedItemPosition == 0  || transportation.selectedItemPosition == 0
                    || estimate.selectedItemPosition == 0){
                    val transaction = manager.beginTransaction()
                    transaction.replace(R.id.fragment_enterError, EnterErrorFragment(),EnterErrorFragment::class.java.simpleName)
                        .addToBackStack(null)
                        .commit()
                    return@setOnClickListener
                }
                val divingTypeCode = when(divingType.checkedChipId){
                    createFreeDivingChip.id->1
                    else->2
                }
                if(image == null)image = File("")
                val room = PreviewRoom(user.userId,
                    "${roomName.text}",
                    image!!,
                    pNumber.text.toString().toInt(),
                    divingTypeCode,
                    divingLevel.selectedItemPosition,
                    purpose.selectedItemPosition,
                    dateTime,
                    area.selectedItemPosition,
                    place.selectedItem.toString(),
                    transportation.selectedItemPosition,
                    "${describe.text}",
                    "",
                    estimate.selectedItemPosition)

                viewModel.createRoomPage.setPreviewRoom(room)
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.fragment_createHelp, DecideCreate(),DecideCreate::class.java.simpleName)
                    .addToBackStack(null)
                    .commit()
            }

        }

        
    }

    //跳頁讀取本地端照片的func，但因還不熟只能先用別人套件使用
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == pickImage && resultCode == RESULT_OK && data != null) {
            val currentPictureUri = data.data
            Glide.with(this).load(currentPictureUri).into(binding.createImage)
            currentPictureUri?.apply {
                //从系统表中查询指定Uri对应的照片
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = contentResolver.query(currentPictureUri, filePathColumn, null, null, null)
                val moveToFirst = cursor?.moveToFirst()
                val path = cursor?.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)) as String
                cursor.close()
                val file = File(path)
                image =  file

                if(Build.VERSION.SDK_INT < 29){
                    RoomDataViewModel.bitmap = MediaStore.Images.Media.getBitmap(contentResolver,this)
                }else{
                    val source = ImageDecoder.createSource(contentResolver,this)
                    RoomDataViewModel.bitmap = ImageDecoder.decodeBitmap(source)
                }
            }
        }
    }


}


