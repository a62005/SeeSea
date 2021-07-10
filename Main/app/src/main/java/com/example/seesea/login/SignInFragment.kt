package com.example.seesea.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.seesea.ChipSetter
import com.example.seesea.R
import com.example.seesea.create.EnterErrorFragment
import com.example.seesea.databinding.FragmentSignInBinding
import com.example.seesea.roomDB.RoomDataViewModel
import com.example.seesea.user.NewUser
import java.util.regex.Pattern

//註冊頁面
class SignInFragment(private val viewModel: RoomDataViewModel) : Fragment() {

    private var binding : FragmentSignInBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        binding = FragmentSignInBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var isAccountCorrect = false
        var isPasswordCorrect = false
        var isEmailCorrect = false
        binding?.apply {

            //上一頁的當前頁判斷
            imageViewSignInBack.setOnClickListener {
                when {
                    layoutSignInDivingAreaPage.visibility == View.VISIBLE -> {
                        layoutSignInDivingAreaPage.visibility = View.INVISIBLE
                        layoutSignInPurposePage.visibility = View.VISIBLE
                        return@setOnClickListener
                    }
                    layoutSignInPurposePage.visibility == View.VISIBLE -> {
                        layoutSignInPurposePage.visibility = View.INVISIBLE
                        layoutSignInExperiencePage.visibility = View.VISIBLE
                        return@setOnClickListener
                    }
                    layoutSignInExperiencePage.visibility == View.VISIBLE->{
                        layoutSignInExperiencePage.visibility = View.INVISIBLE
                        layoutSignInDivingTypePage.visibility = View.VISIBLE
                        return@setOnClickListener
                    }
                    layoutSignInDivingTypePage.visibility == View.VISIBLE -> {
                        layoutSignInDivingTypePage.visibility = View.INVISIBLE
                        layoutSignInUserData.visibility = View.VISIBLE
                        return@setOnClickListener
                    }

                    else -> activity?.onBackPressed()

                }
            }

            //Text監聽皆為判斷當前輸入值是否正確或重複
            editTextSignInAccountInput.addTextChangedListener( object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}//輸入後的監聽
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}//輸入後的監聽
                    @SuppressLint("SetTextI18n")
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        when {
                            start - before + 1 == 0 -> {
                                signInCheckAccount.visibility = View.INVISIBLE
                                textViewCheckAccountLong.visibility = View.INVISIBLE
                                textViewCheckAccountRepeat.visibility = View.INVISIBLE
                            }
                            start - before + 1 in 1..5 -> {
                                textViewCheckAccountLong.visibility = View.VISIBLE
                                signInCheckAccount.visibility = View.VISIBLE
                                textViewCheckAccountRepeat.visibility = View.INVISIBLE
                                signInCheckAccount.setImageResource(R.drawable.wrong)
                                isAccountCorrect = false
                            }
                            start - before + 1 in 5..12 -> {
                                viewModel.loginPage.checkAccountRepeat("${editTextSignInAccountInput.text}")
                                viewModel.loginPage.checkAccount.observe(viewLifecycleOwner){
                                    if(it){
                                        signInCheckAccount.setImageResource(R.drawable.okay)
                                        textViewCheckAccountRepeat.visibility = View.INVISIBLE
                                        textViewCheckAccountLong.visibility = View.INVISIBLE
                                        isAccountCorrect = true
                                    }else{
                                        signInCheckAccount.setImageResource(R.drawable.wrong)
                                        textViewCheckAccountRepeat.visibility = View.VISIBLE
                                        textViewCheckAccountLong.visibility = View.INVISIBLE
                                        isAccountCorrect = false
                                    }
                                }
                            }
                        }

                    }
            })


            editTextSignInEmail.addTextChangedListener( object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    editTextSignInEmail.setOnFocusChangeListener { _, hasFocus ->
                        if(!hasFocus){
                            if(isValidEmailId(editTextSignInEmail.text.toString())){
                                viewModel.loginPage.checkEmailRepeat("${editTextSignInEmail.text}")
                            }else{
                                signInCheckEmail.setImageResource(R.drawable.wrong)
                                signInCheckEmail.visibility = View.VISIBLE
                                isEmailCorrect = false
                            }
                        }
                    }
                    viewModel.loginPage.checkEmail.observe(viewLifecycleOwner){
                        signInCheckEmail.visibility = View.VISIBLE
                        isEmailCorrect = if(it){
                            signInCheckEmail.setImageResource(R.drawable.okay)
                            true
                        }else{
                            signInCheckEmail.setImageResource(R.drawable.wrong)
                            false
                        }
                    }
                }//輸入後的監聽
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}//輸入後的監聽
                @SuppressLint("SetTextI18n")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(start - before + 1 == 0){
                        signInCheckEmail.visibility = View.INVISIBLE
                    }
                }
            })


            editTextSignInPasswordInput.addTextChangedListener( object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}//輸入後的監聽
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}//輸入後的監聽
                @SuppressLint("SetTextI18n")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    when {
                        start - before + 1 == 0 -> {
                            imageViewCheckPassword.visibility = View.INVISIBLE
                            textViewCheckPassword.visibility = View.INVISIBLE
                        }
                        start - before + 1 in 1..5 -> {
                            if(s.toString() != editTextPasswordCheckAgain.text.toString()){
                                imageViewPasswordCheckAgain.setImageResource(R.drawable.wrong)
                            }
                            imageViewCheckPassword.visibility = View.VISIBLE
                            textViewCheckPassword.visibility = View.VISIBLE
                            imageViewCheckPassword.setImageResource(R.drawable.wrong)
                            isPasswordCorrect = false
                        }
                        else -> {
                            if(s.toString() == editTextPasswordCheckAgain.text.toString()){
                                imageViewPasswordCheckAgain.setImageResource(R.drawable.okay)
                            }else{
                                imageViewPasswordCheckAgain.setImageResource(R.drawable.wrong)
                            }
                            textViewCheckPassword.visibility = View.INVISIBLE
                            imageViewCheckPassword.setImageResource(R.drawable.okay)
                            isPasswordCorrect = true
                        }
                    }
                }
            })

            editTextPasswordCheckAgain.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    when {
                        start - before + 1 == 0 -> { imageViewPasswordCheckAgain.visibility = View.INVISIBLE }
                        start - before + 1 in 1..5 -> {
                            imageViewPasswordCheckAgain.visibility = View.VISIBLE
                            imageViewPasswordCheckAgain.setImageResource(R.drawable.wrong) }
                        s.toString() == editTextSignInPasswordInput.text.toString() -> {
                            imageViewPasswordCheckAgain.setImageResource(R.drawable.okay)
                        }
                        s.toString() != editTextSignInPasswordInput.text.toString() -> {
                            imageViewPasswordCheckAgain.setImageResource(R.drawable.wrong)
                        }
                    }
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            editTextSignInPhone.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}//輸入後的監聽
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}//輸入後的監聽
                @SuppressLint("SetTextI18n")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    when {
                        start - before + 1 == 0 -> {
                            imageViewCheckPhone.visibility = View.INVISIBLE
                            textViewCheckPhoneLong.visibility = View.INVISIBLE
                            textViewCheckPhoneRepeat.visibility = View.INVISIBLE
                        }
                        start - before + 1 in 1..5 -> {
                            imageViewCheckPhone.visibility = View.VISIBLE
                            textViewCheckPhoneLong.visibility = View.VISIBLE
                            textViewCheckPhoneRepeat.visibility = View.INVISIBLE
                            imageViewCheckPhone.setImageResource(R.drawable.wrong)
                        }
                        start - before + 1 == 10 -> {
                            viewModel.loginPage.checkPhoneRepeat(editTextSignInPhone.text.toString().toInt())
                            viewModel.loginPage.checkPhone.observe(viewLifecycleOwner){
                                if(it){
                                    imageViewCheckPhone.setImageResource(R.drawable.okay)
                                    textViewCheckPhoneRepeat.visibility = View.INVISIBLE
                                    textViewCheckPhoneLong.visibility = View.INVISIBLE
                                }else{
                                    imageViewCheckPhone.setImageResource(R.drawable.wrong)
                                    textViewCheckPhoneRepeat.visibility = View.VISIBLE
                                    textViewCheckPhoneLong.visibility = View.INVISIBLE
                                }
                            }
                        }
                    }

                }
            })

            val manager = childFragmentManager
            textViewSignInNext.setOnClickListener {
                if(!isAccountCorrect || !isPasswordCorrect || editTextSignInName.text.toString() == "" || editTextSignInAge.text.toString() =="" || !isEmailCorrect ||
                        editTextSignInPasswordInput.text.toString() != editTextPasswordCheckAgain.text.toString()){
                    val transaction = manager.beginTransaction()
                    transaction.replace(R.id.layout_signInInputError, EnterErrorFragment(),
                        EnterErrorFragment::class.java.simpleName)
                        .addToBackStack(null)
                        .commit()
                    return@setOnClickListener
                }
                layoutSignInDivingTypePage.visibility = View.VISIBLE

                layoutSignInUserData.visibility = View.INVISIBLE
            }
            signInDivingTypeCompleteButton.setOnClickListener {
                layoutSignInDivingTypePage.visibility = View.INVISIBLE
                layoutSignInExperiencePage.visibility = View.VISIBLE
            }
            signInExperienceCompleteButton.setOnClickListener {
                if(chipNoExperience.isChecked){
                    layoutSignInExperiencePage.visibility = View.INVISIBLE
                    layoutSignInDivingAreaPage.visibility = View.VISIBLE
                    return@setOnClickListener
                }
                layoutSignInExperiencePage.visibility = View.INVISIBLE
                layoutSignInPurposePage.visibility = View.VISIBLE
            }
            signInPurposeCompleteButton.setOnClickListener {
                layoutSignInPurposePage.visibility = View.INVISIBLE
                layoutSignInDivingAreaPage.visibility = View.VISIBLE
            }

            val divingTypeChipSetter = ChipSetter(R.color.signInButton, R.color.blue)
                .add(chipFreeDiving)
                .add(chipScubaDiving)
                .add(chipBothDiving)
                .build()
            val experienceChipSetter = ChipSetter(R.color.signInButton, R.color.blue)
                .add(chipNoExperience)
                .add(chipJunior)
                .add(chipSenior)
                .build()
            val divingAreaChipSetter = ChipSetter(R.color.signInButton, R.color.blue)
                .add(chipNorth)
                .add(chipSouth)
                .add(chipInside)
                .add(chipGreenIsland)
                .add(chipLanYu)
                .add(chipPengHu)
                .add(chipLiuQiu)
                .build()
            val purposeChipSetter = ChipSetter(R.color.signInButton, R.color.blue)
                .add(chipPurposeA)
                .add(chipPurposeB)
                .add(chipPurposeC)
                .add(chipPurposeD)
                .build()

            //完成後把使用者點選得項目(chip)轉成後端使用的格式並已NewUser類別送出
            signInDivingAreaCompleteButton.setOnClickListener {
                var divingType = ChipSetter.intTranslateToStr(divingTypeChipSetter.translate().getIntArr())
                if(divingType == "3"){
                    divingType = "1,2"
                }
                val experience = when(chipExperience.checkedChipId){
                    chipJunior.id -> 2
                    chipSenior.id ->3
                    else -> 1
                }
                val divingArea = ChipSetter.intTranslateToStr(divingAreaChipSetter.translate().getIntArr())
                val purpose = when(chipPurpose.checkedChipId){
                    chipPurposeA.id -> 1
                    chipPurposeB.id -> 2
                    chipPurposeC.id -> 3
                    chipPurposeD.id -> 4
                    else -> 5
                }
                val user = NewUser(
                    editTextSignInAccountInput.text.toString()
                    ,editTextSignInPasswordInput.text.toString()
                    ,editTextSignInName.text.toString()
                    ,editTextSignInNickName.text.toString()
                    ,editTextSignInAge.text.toString().toInt()
                    ,editTextSignInPhone.text.toString().toInt()
                    ,editTextSignInEmail.text.toString()
                    ,divingType
                    ,experience
                    ,purpose
                    ,divingArea
                )
                viewModel.loginPage.createUser(user)
                layoutConfirmSuccess.visibility = View.VISIBLE
                buttonConfirm.setOnClickListener {
                    activity?.onBackPressed()
                }

            }

        }

    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun isValidEmailId(email: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }

}