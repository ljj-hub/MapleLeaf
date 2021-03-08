package com.example.mapleleaf.ui.login

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.mapleleaf.R
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //将背景图与状态栏融合
        val decorView =window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT

        val prefs = getSharedPreferences("login",Context.MODE_PRIVATE)

        //确认注册被点击
        register.setOnClickListener {
            val account = reg_accountEdit.text.toString().trim()//注册时设置的用户名
            val password = reg_passwordEdit1.text.toString().trim()//注册时设置的密码
            val againPassword =reg_passwordEdit2.text.toString().trim()//第二次输入的用于确认的密码
            when {
                TextUtils.isEmpty(account) -> {
                    Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(password) -> {
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show()
                }
                againPassword !=password -> {
                    Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val editor = prefs.edit()
                    editor.putString(account, password)
                    editor.apply()
                    Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Login::class.java)
                    //intent.putExtra("account",account)
                    startActivity(intent)
                    finish()
                }
            }
        }

        //返回登录被点击
        back.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

    }
}