package com.example.mapleleaf.ui.login

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.mapleleaf.R
import java.util.*

class Start : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        //将背景图与状态栏融合
        val decorView =window.decorView
        decorView.systemUiVisibility =View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor =Color.TRANSPARENT

        //3秒后跳转
        val intent =Intent(this, Login::class.java)
        val timer = Timer()
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                startActivity(intent) //执行
                finish()//销毁Activity
            }
        }
        timer.schedule(task, 1000 * 3) //3秒后

    }
}