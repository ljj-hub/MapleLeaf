package com.example.mapleleaf


import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.transition.Explode
import android.transition.Fade
import android.transition.Slide
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.mapleleaf.ui.fragment.OneFragment
import com.example.mapleleaf.ui.fragment.ThreeFragment
import com.example.mapleleaf.ui.fragment.TwoFragment
import com.example.mapleleaf.ui.login.Register
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


open class MainActivity : AppCompatActivity() {

    lateinit var bnView: BottomNavigationView
    lateinit var viewPager: ViewPager

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.mapleleaf.R.layout.activity_main)
        setSupportActionBar(tool_bar);

        //将背景图与状态栏融合
        val decorView = window.decorView
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.rgb(255, 51, 51)

        // 设置阴影部分颜色为透明
        drawer_layout.setScrimColor(Color.TRANSPARENT);

        // Toolbar 的 Home 点击事件与 DrawerLayout 关联
        val toggle: ActionBarDrawerToggle =
            object : ActionBarDrawerToggle(this, drawer_layout, tool_bar,
                R.string.app_name, R.string.app_name) {
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    val content: View = drawer_layout.getChildAt(0) //获得主界面View
                    if (drawerView.tag == "left") {  //判断是否是左菜单
                        val offset = (drawerView.width * slideOffset).toInt()
                        content.translationX = offset.toFloat()
                    }
                }
            }
        toggle.syncState()
        drawer_layout.addDrawerListener(toggle)

        navigation_view.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                com.example.mapleleaf.R.id.nav_above -> {
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("提示")
                        .setMessage("简易便签软件\n仅用于学习交流使用\n\n版本：1.0.0")
                        .setCancelable(true)
                        .setPositiveButton("确定", null)
                        .show()
                }
                com.example.mapleleaf.R.id.nav_us -> {
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("提示")
                        .setMessage("电话：×××××××××××\n邮箱：×××××××××××\n\n欢迎联系")
                        .setCancelable(true)
                        .setPositiveButton("确定", null)
                        .show()
                }
                com.example.mapleleaf.R.id.nav_help -> Toast.makeText(
                    this,
                    "有待开发中",
                    Toast.LENGTH_SHORT
                ).show()
            }
            true
        })


        bnView = findViewById(com.example.mapleleaf.R.id.bottom_nav_view)
        viewPager = findViewById(com.example.mapleleaf.R.id.view_pager)
        val fragments: MutableList<Fragment> = ArrayList<Fragment>()
        fragments.add(OneFragment())
        fragments.add(TwoFragment())
        fragments.add(ThreeFragment())
//        fragments.add(FourFragment())
        val adapter = FragmentAdapter(fragments, supportFragmentManager)
        viewPager.adapter = adapter

        //BottomNavigationView 点击事件监听，每点击一下按钮，需要跳转到指定的Fragment中
        bnView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            val menuId = menuItem.itemId
            when (menuId) {
                com.example.mapleleaf.R.id.tab_one -> viewPager.currentItem = 0
                com.example.mapleleaf.R.id.tab_two -> viewPager.currentItem = 1
                com.example.mapleleaf.R.id.tab_three -> viewPager.currentItem = 2
//                com.example.mapleleaf.R.id.tab_four -> viewPager.currentItem = 3
            }
            false
        })

        // ViewPager 滑动事件监听,BottomNavigationView跟随滑动切换
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {

            override fun onPageScrolled(i: Int, v: Float, i1: Int) {}

            override fun onPageSelected(i: Int) {
                //将滑动到的页面对应的 menu 设置为选中状态
                bnView.menu.getItem(i).isChecked = true
            }

            override fun onPageScrollStateChanged(i: Int) {}
        })

        //悬浮按钮的点击事件
        fab.setOnClickListener {
            val intent = Intent(this, Add::class.java)
            startActivity(intent)
        }

    }


    //加载toolbar.xml这个菜单文件
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.example.mapleleaf.R.menu.toolbar_menu, menu)
        return true
    }

    //处理toolbar上各个按钮的点击事件
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            com.example.mapleleaf.R.id.search ->
//                Toast.makeText(this, "有待开发中", Toast.LENGTH_SHORT).show()
            {
                val intent = Intent(this, Search::class.java)
                startActivity(intent)
            }
        }
        return true
    }

}