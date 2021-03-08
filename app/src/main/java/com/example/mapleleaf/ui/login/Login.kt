package com.example.mapleleaf.ui.login

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.mapleleaf.MainActivity
import com.example.mapleleaf.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class Login : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //将背景图与状态栏融合
        val decorView =window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT

        //val account =intent.getStringExtra("account")
        //accountEdit1.setText(account)
        //获取SharedPreferences对象
        val prefs = getSharedPreferences("login", Context.MODE_PRIVATE)
        val checkPrefs = getSharedPreferences("checkBox", Context.MODE_PRIVATE)

        //提取密码和账号信息
        val isRemember = checkPrefs.getBoolean("remember_password", false)
        if (isRemember) {
            //将账号和密码都设置到文本框中
            val account = checkPrefs.getString("account", "")
            val password = checkPrefs.getString("password", "")
            login_accountEdit.setText(account)
            login_passwordEdit.setText(password)
            rememberPass.isChecked = true
        }

        //登录按钮被点击
        btn_login.setOnClickListener {
            val username = login_accountEdit.text.toString().trim()
            val password1 = login_passwordEdit.text.toString().trim()
            val password2 = prefs.getString(username, "")

            //对输入内容进行判断
            when {
                TextUtils.isEmpty(username) -> {
                    Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
                }
                TextUtils.isEmpty(password1) -> {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                }
                password1 != password2 -> {
                    Toast.makeText(this, "输入的用户名和密码不一致", Toast.LENGTH_SHORT).show();
                }
                else -> {
                    //保存登录成功的用户名,利用SharedPreferences来传递数据
                    val prefs_user = getSharedPreferences("user",Context.MODE_PRIVATE)
                    val editor_user = prefs_user.edit()
                    editor_user.putString("success",username)
                    editor_user.apply()

                    //记住密码操作
                    val editor = checkPrefs.edit()
                    if (rememberPass.isChecked) {
                        editor.putBoolean("remember_password", true)
                        editor.putString("account", username)
                        editor.putString("password", password1)
                    } else {
                        editor.clear()
                    }
                    editor.apply()
                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                    val intent = Intent(this, MainActivity::class.java)
//                    intent.putExtra("username",username)//传递用户名信息
//                    startActivity(intent)
                    startActivity(intent);
                    finish()//销毁Activity
                }
            }
        }

        //注册按钮被点击
        btn_register.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

    }
}


