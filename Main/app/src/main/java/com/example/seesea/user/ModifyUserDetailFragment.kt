package com.example.seesea.user

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.seesea.R
import com.example.seesea.databinding.FragmentModifyUserDetailBinding
import com.example.seesea.roomDB.RoomDataViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import java.io.File

class ModifyUserDetailFragment : Fragment() {
    private val pickImage = 100
    private val viewModel: RoomDataViewModel by activityViewModels()
    private var binding : FragmentModifyUserDetailBinding? = null
    private var image: File? = null
    private var isPasswordCorrect = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_modify_user_detail, container, false)
        binding = FragmentModifyUserDetailBinding.bind(view)

        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gray = resources.getColor(R.color.gray_light_more,null)
        val black = resources.getColor(R.color.black,null)
        val areaTag = resources.getStringArray(R.array.create_divingLocation).toMutableList()
        val divingTag = resources.getStringArray(R.array.create_divingType).toMutableList()
        areaTag.removeFirst()
        divingTag.removeFirst()
        viewModel.userPage.setLastUserTag(areaTag,divingTag)


        binding?.apply {
            modifyUserBack.setOnClickListener {
                viewModel.userPage.getUserData()
                activity?.onBackPressed()
            }
            includeUserDetail.apply {
                userDetailModifyButton.visibility = View.GONE
                buttonOkayButton.visibility = View.VISIBLE
                modifyPassword.visibility = View.VISIBLE
                textViewUserUserName.setTextColor(gray)
                textView78.setTextColor(gray)
                textViewUserAge.setTextColor(gray)
                textView80.setTextColor(gray)
//                textViewUserPhone.setTextColor(black)
//                textViewUserPhoneInput.setTextColor(black)

                val divingTagAdapter = DivingTagAdapter(viewModel,
                    isModify = true,
                    isLastTag = false)
                val layoutManager = FlexboxLayoutManager(context)
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.flexWrap = FlexWrap.WRAP
                layoutManager.justifyContent = JustifyContent.FLEX_START
                recyclerViewUserTag.adapter = divingTagAdapter
                recyclerViewUserTag.layoutManager = layoutManager

                val lastDivingTagAdapter = DivingTagAdapter(viewModel,
                    isModify = true,
                    isLastTag = true)
                val tagLayoutManager = FlexboxLayoutManager(context)
                tagLayoutManager.flexDirection = FlexDirection.ROW
                tagLayoutManager.flexWrap = FlexWrap.WRAP
                tagLayoutManager.justifyContent = JustifyContent.FLEX_START
                recyclerViewLastDivingTag.adapter = lastDivingTagAdapter
                recyclerViewLastDivingTag.layoutManager = tagLayoutManager


                viewModel.user.observe(viewLifecycleOwner){ user->
                    textViewUserUserNickName.setText(user.userNickName)
                    textViewUserUserName.text = user.userName
                    textViewUserAge.text = "${user.userAge}"
                    textViewUserPhoneInput.setText("0${user.userPhone}")
                    textViewUserEmailInput.text = user.userEmail
                    textViewUserDescribe.setText(user.userDescription)
                    Glide.with(requireActivity().applicationContext).load(user.userImage).into(imageViewModifyUserImage)

                    val str = mutableListOf<String>()
                    str.addAll(user.divingTypeTag)
                    str.addAll(user.areaTag)
                    divingTagAdapter.submitList(str)
                    viewModel.userPage.lastUserTag.observe(viewLifecycleOwner){
                        lastDivingTagAdapter.submitList(it)
                        lastDivingTagAdapter.notifyDataSetChanged()
                    }
                }

                imageViewModifyUserImage.setOnClickListener {
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, pickImage)
                }
                modifyPassword.setOnClickListener {
                    layoutResetPassword.visibility = View.VISIBLE
                }
                passwordBack.setOnClickListener {
                    layoutResetPassword.visibility = View.INVISIBLE
                }

                passwordOkayButton.setOnClickListener {
                    viewModel.loginPage.checkOldPassword(editTextOldPasswordInput.text.toString())
                }
                viewModel.loginPage.canLogin.observe(viewLifecycleOwner){
                    if(it&&isPasswordCorrect){
                        layoutResetPassword.visibility = View.INVISIBLE
                    }else{
                        imageViewCheckOldPassword.visibility = View.VISIBLE
                        textViewCheckOldPassword.visibility = View.VISIBLE
                    }
                }
                editTextNewPasswordInput.addTextChangedListener(object :TextWatcher{
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        when {
                            start - before + 1 == 0 -> {
                                imageViewNewPassword.visibility = View.INVISIBLE
                                textViewCheckAccountLong.visibility = View.INVISIBLE
                            }
                            start - before + 1 in 1..5 -> {
                                imageViewNewPassword.visibility = View.VISIBLE
                                textViewCheckAccountLong.visibility = View.VISIBLE
                                imageViewNewPassword.setImageResource(R.drawable.wrong)
                                isPasswordCorrect = false
                            }
                            else -> {
                                textViewCheckAccountLong.visibility = View.INVISIBLE
                                imageViewNewPassword.setImageResource(R.drawable.okay)
                                isPasswordCorrect = true
                            }
                        }
                    }
                    override fun afterTextChanged(s: Editable?) {}
                })
                editTextNewCheckPassword.addTextChangedListener(object :TextWatcher{
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        when {
                            start - before + 1 == 0 -> {
                                imageViewCheckNewPassword.visibility = View.INVISIBLE
                            }
                            start - before + 1 in 1..5 -> {
                                imageViewCheckNewPassword.visibility = View.VISIBLE
                                imageViewCheckNewPassword.setImageResource(R.drawable.wrong)
                                isPasswordCorrect = false
                            }
                            s.toString() == editTextNewPasswordInput.text.toString() -> {
                                imageViewCheckNewPassword.setImageResource(R.drawable.okay)
                                isPasswordCorrect = true
                            }
                            s.toString() != editTextNewPasswordInput.text.toString() -> {
                                imageViewCheckNewPassword.setImageResource(R.drawable.wrong)
                                isPasswordCorrect = false
                            }
                        }
                    }
                    override fun afterTextChanged(s: Editable?) {}
                })

                buttonOkayButton.setOnClickListener {
                    if(textViewUserUserNickName.text.toString() == ""){
                        Toast.makeText(requireContext(),"若無暱稱則會使用本名哦",Toast.LENGTH_LONG).show()
                    }
                    if(image == null)image = File("")
                    val user = viewModel.user.value!!
                    val tmpDivingTag = resources.getStringArray(R.array.create_divingType).toMutableList()
                    val tmpAreaTag = resources.getStringArray(R.array.create_divingLocation).toMutableList()
                    val divingTagArr = mutableListOf<Int>()
                    val areaTagArr = mutableListOf<Int>()
                    tmpDivingTag.forEach { tag-> user.divingTypeTag.forEach { if(tag == it){ divingTagArr.add(tmpDivingTag.indexOf(it)) } }}
                    tmpAreaTag.forEach { tag->user.areaTag.forEach { if(tag == it){ areaTagArr.add(tmpAreaTag.indexOf(it)) } }}
                    var d = divingTagArr.toString().subSequence(1,divingTagArr.toString().length-1).toString()
                    var a = areaTagArr.toString().subSequence(1,areaTagArr.toString().length-1).toString()
                    if(d == "")d="0"
                    if(a == "")a="0"
                    val modifyUser = ModifyUser(
                        user.userId,
                        editTextNewPasswordInput.text.toString(),
                        textViewUserUserNickName.text.toString(),
                        textViewUserPhoneInput.text.toString().toInt(),
                        user.userEmail,
                        0,
                        image!!,
                        textViewUserDescribe.text.toString(),
                        d,
                        a
                    )
                    viewModel.loginPage.modifyUser(modifyUser)
                    viewModel.roomListPage.getRecommendRoom()
                    Toast.makeText(requireContext(),"更新完成",Toast.LENGTH_LONG).show()
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
            Glide.with(this).load(currentPictureUri).into(binding!!.includeUserDetail.imageViewModifyUserImage)
            currentPictureUri?.apply {
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