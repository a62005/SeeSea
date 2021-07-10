package com.example.seesea.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.seesea.*
import com.example.seesea.databinding.ActivityLoginBinding
import com.example.seesea.roomDB.RoomDataApplication
import com.example.seesea.roomDB.RoomDataViewModel
import com.example.seesea.roomDB.RoomDataViewModelFactory

//登入頁面
class LoginActivity : AppCompatActivity() {

    private val viewModel: RoomDataViewModel by viewModels {
        RoomDataViewModelFactory((application as RoomDataApplication).repository)
    }
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val sp = SharedPref.getInstance(applicationContext)
        binding.apply {
            loginLoginButton.setOnClickListener {
                viewModel.loginPage.canLogin(loginUsername.text.toString(),loginPassword.text.toString())
            }

            //按下登入鍵後使用監聽判斷是否有該帳號，若有則用該帳號Login
            viewModel.loginPage.canLogin.observe(this@LoginActivity){
                if(it){
                    viewModel.loginPage.login(loginUsername.text.toString(),loginPassword.text.toString())
                }else{
                    Toast.makeText(this@LoginActivity, "帳號或密碼錯誤哦", Toast.LENGTH_LONG).show()
                }
            }

            //Login後監聽到user被寫入則跳轉至Main
            viewModel.user.observe(this@LoginActivity){user->
                if(user == null){
                    return@observe
                }
                sp.saveUser(user)
                val intent = Intent()
                intent.putExtra(LOGIN, user)
                intent.setClass(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                this@LoginActivity.finish()
            }



            val manager = supportFragmentManager
            loginSignInButton.setOnClickListener {
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.layout_login, SignInFragment(viewModel), SignInFragment::class.java.simpleName)
                    .addToBackStack(null)
                    .commit()
            }


            forgotPassword.setOnClickListener {
                layoutForgotPassword.visibility = View.VISIBLE
            }


            loginForgotPassword.okay.setOnClickListener {
                viewModel.loginPage.forgotPassword(loginForgotPassword.editTextSignInAccountInput.text.toString(),loginForgotPassword.editTextSignInEmail.text.toString())
                layoutForgotPassword.visibility = View.INVISIBLE
            }
            loginForgotPassword.back.setOnClickListener {
                layoutForgotPassword.visibility = View.GONE
            }

        }




    }
}

