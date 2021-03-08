package com.example.mapleleaf.ui.fragment

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.*
import android.view.ContextMenu.ContextMenuInfo
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.example.mapleleaf.Detail
import com.example.mapleleaf.MyDatabaseHelper
import com.example.mapleleaf.R
import com.example.mapleleaf.ViewPageAdapter
import com.example.mapleleaf.ui.recycler.Fruit
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_one_view_1.*
import kotlinx.android.synthetic.main.fragment_one_view_2.*
import kotlinx.android.synthetic.main.fragment_one_view_3.*
import kotlinx.android.synthetic.main.fragment_one_view_4.*
import java.io.File


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OneFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("UNREACHABLE_CODE")
class OneFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var viewPager: ViewPager
    lateinit var tabLayout: TabLayout
    private lateinit var views: MutableList<View>
    private lateinit var titles: MutableList<String>
    val fruitList1 = ArrayList<Fruit>()
    val fruitList2 = ArrayList<Fruit>()
    val fruitList3 = ArrayList<Fruit>()
    val fruitList4 = ArrayList<Fruit>()
    var f =-100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_one, container, false)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo?) {
        // 这个方法是创建点击以后显示的布局条目的
        activity!!.menuInflater.inflate(R.menu.commen_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        // 返回值true是拦截事件,false是不拦截
        if (userVisibleHint) {
            // 这个方法是判断当前页面是哪一个页面的返回false就是不在当前页面
            //通过SharedPreferences读取列表项title
            val prefs_user = activity!!.getSharedPreferences("item_title", Context.MODE_PRIVATE)
            val select_title =prefs_user.getString("fruit_name", "")
            val dbHelper = MyDatabaseHelper(context!!, "NoteBook.db", 1)
            if (item.itemId === R.id.delete) {
                val db = dbHelper.writableDatabase
                val cursor = db.query("Note", null, "title =?", arrayOf(select_title), null, null, null)
                if (cursor.moveToFirst()){
                    do {
                        val title = cursor.getString(cursor.getColumnIndex("title"))
                        db.delete("Note", "title =?", arrayOf(title))
                    } while (cursor.moveToNext())
                }
                cursor.close()
                onResume()
//                Toast.makeText(activity, "已删除，请刷新", Toast.LENGTH_SHORT).show()
            } else if (item.itemId === R.id.top) {
                val values =ContentValues()
                fun cun(): Int {
                    f+=f
                    return f
                }
                values.put("id",cun())
                val db = dbHelper.writableDatabase
                val cursor = db.query("Note", null, "title =?", arrayOf(select_title), null, null, null)
                if (cursor.moveToFirst()){
                    do {
                        val id = cursor.getString(cursor.getColumnIndex("id"))
                        db.update("Note",values,"id =?", arrayOf(id))
                    } while (cursor.moveToNext())
                }
                cursor.close()
                onResume()
//                Toast.makeText(activity, "已置顶，请刷新", Toast.LENGTH_SHORT).show()
            }
            return true
        }
        return false
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = view.findViewById(R.id.one_view_pager)
        tabLayout = view.findViewById(R.id.tab_layout)
        val viewOne: View = LayoutInflater.from(view.context).inflate(
            R.layout.fragment_one_view_1,
            null
        )
        val viewTwo: View = LayoutInflater.from(view.context).inflate(
            R.layout.fragment_one_view_2,
            null
        )
        val viewThree: View = LayoutInflater.from(view.context).inflate(
            R.layout.fragment_one_view_3,
            null
        )
        val viewFour: View = LayoutInflater.from(view.context).inflate(
            R.layout.fragment_one_view_4,
            null
        )

        views = ArrayList()
        views.add(viewOne)
        views.add(viewTwo)
        views.add(viewThree)
        views.add(viewFour)
        titles = ArrayList()
        titles.add("全部")
        titles.add("生活")
        titles.add("工作")
        titles.add("学习")
        val adapter = ViewPageAdapter(views, titles)
        for (title in titles) {
            tabLayout.addTab(tabLayout.newTab().setText(title))
        }
        tabLayout.setupWithViewPager(viewPager)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 4
    }

    override fun onResume() {
        println("onResume")
        val startTime = object : CountDownTimer(500, 500) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                val dbHelper = MyDatabaseHelper(activity!!, "NoteBook.db", 1)
                //读取SQLite中的数据
                val db = dbHelper.writableDatabase
                //查询Book表中所有的数据
                val cursor = db.query("Note", null, null, null, null, null, null)
                val amount = cursor.count

                val layoutManager1 = LinearLayoutManager(activity)
                val layoutManager2 = LinearLayoutManager(activity)
                val layoutManager3 = LinearLayoutManager(activity)
                val layoutManager4 = LinearLayoutManager(activity)
                val adapter1 = FruitAdapter(fruitList1)
                val adapter2 = FruitAdapter(fruitList2)
                val adapter3 = FruitAdapter(fruitList3)
                val adapter4 = FruitAdapter(fruitList4)

                if (amount != 0) {
                    fruitList1.clear()
                    fruitList2.clear()
                    fruitList3.clear()
                    fruitList4.clear()
                    if (cursor.moveToFirst()) {
                        do {
                            //遍历Cursor对象，取出数据并打印
                            val title_one = cursor.getString(cursor.getColumnIndex("title"))
                            val context_one = cursor.getString(cursor.getColumnIndex("context"))
                            val time_one = cursor.getString(cursor.getColumnIndex("time"))
                            val class_one = cursor.getString(cursor.getColumnIndex("class"))
                            when (class_one) {
                                "生活" -> {
                                    fruitList2.add(Fruit(title_one!!, R.drawable.icon_order))
                                    val adapter2 = FruitAdapter(fruitList2)
                                    recyclerView2.layoutManager = layoutManager2
                                    recyclerView2.adapter = adapter2
                                    adapter2.notifyDataSetChanged()

                                    fruitList1.add(Fruit(title_one!!, R.drawable.icon_order))
                                    val adapter1 = FruitAdapter(fruitList1)
                                    recyclerView1.layoutManager = layoutManager1
                                    recyclerView1.adapter = adapter1
                                    adapter1.notifyDataSetChanged()
                                }
                                "工作" -> {
                                    fruitList3.add(Fruit(title_one!!, R.drawable.icon_order))
                                    val adapter3 = FruitAdapter(fruitList3)
                                    recyclerView3.layoutManager = layoutManager3
                                    recyclerView3.adapter = adapter3
                                    adapter3.notifyDataSetChanged()

                                    fruitList1.add(Fruit(title_one!!, R.drawable.icon_order))
                                    val adapter1 = FruitAdapter(fruitList1)
                                    recyclerView1.layoutManager = layoutManager1
                                    recyclerView1.adapter = adapter1
                                    adapter1.notifyDataSetChanged()
                                }
                                "学习" -> {
                                    fruitList4.add(Fruit(title_one!!, R.drawable.icon_order))
                                    val adapter4 = FruitAdapter(fruitList4)
                                    recyclerView4.layoutManager = layoutManager4
                                    recyclerView4.adapter = adapter4
                                    adapter4.notifyDataSetChanged()

                                    fruitList1.add(Fruit(title_one!!, R.drawable.icon_order))
                                    val adapter1 = FruitAdapter(fruitList1)
                                    recyclerView1.layoutManager = layoutManager1
                                    recyclerView1.adapter = adapter1
                                    adapter1.notifyDataSetChanged()
                                }
                                else -> {
                                    fruitList1.add(Fruit(title_one!!, R.drawable.icon_order))
                                    val adapter1 = FruitAdapter(fruitList1)
                                    recyclerView1.layoutManager = layoutManager1
                                    recyclerView1.adapter = adapter1
                                    adapter1.notifyDataSetChanged()
                                }
                            }
                        } while (cursor.moveToNext())
                    }
                    cursor.close()
                } else {
                    fruitList1.clear()
                    fruitList2.clear()
                    fruitList3.clear()
                    fruitList4.clear()
                    initFruits1()
                    initFruits2()
                    initFruits3()
                    initFruits4()
                    recyclerView1.layoutManager = layoutManager1
                    recyclerView2.layoutManager = layoutManager2
                    recyclerView3.layoutManager = layoutManager3
                    recyclerView4.layoutManager = layoutManager4
                    recyclerView1.adapter = adapter1
                    recyclerView2.adapter = adapter2
                    recyclerView3.adapter = adapter3
                    recyclerView4.adapter = adapter4
                    adapter1.notifyDataSetChanged()
                    adapter2.notifyDataSetChanged()
                    adapter3.notifyDataSetChanged()
                    adapter4.notifyDataSetChanged()
                }
                //注册上下文菜单
                registerForContextMenu(recyclerView1)
                registerForContextMenu(recyclerView2)
                registerForContextMenu(recyclerView3)
                registerForContextMenu(recyclerView4)

                swipe_refresh1.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
                    override fun onRefresh() {
                        fruitList1.clear()
                        val cursor = db.query("Note", null, null, null, null, null, null)
                        if (cursor.moveToFirst()) {
                            do {
                                val title_one = cursor.getString(cursor.getColumnIndex("title"))
                                val class_one = cursor.getString(cursor.getColumnIndex("class"))
                                fruitList1.add(Fruit(title_one!!, R.drawable.icon_order))
                                val adapter1 = FruitAdapter(fruitList1)
                                recyclerView1.layoutManager = layoutManager1
                                recyclerView1.adapter = adapter1
                                adapter1.notifyDataSetChanged()
                            } while (cursor.moveToNext())

                        }
                        cursor.close()
                        Handler().postDelayed({
                            if (swipe_refresh1.isRefreshing) {
                                swipe_refresh1.isRefreshing = false
                            }
                        }, 1500)
                    }
                })
                swipe_refresh2.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
                    override fun onRefresh() {
                        fruitList2.clear()
                        val cursor = db.query("Note", null, null, null, null, null, null)
                        if (cursor.moveToFirst()) {
                            do {
                                val title_one = cursor.getString(cursor.getColumnIndex("title"))
                                val class_one = cursor.getString(cursor.getColumnIndex("class"))
                                when (class_one) {
                                    "生活" -> {
                                        fruitList2.add(Fruit(title_one!!, R.drawable.icon_order))
                                        val adapter2 = FruitAdapter(fruitList2)
                                        recyclerView2.layoutManager = layoutManager2
                                        recyclerView2.adapter = adapter2
                                        adapter2.notifyDataSetChanged()
                                    }
                                }
                            } while (cursor.moveToNext())

                        }
                        cursor.close()
                        Handler().postDelayed({
                            if (swipe_refresh2.isRefreshing) {
                                swipe_refresh2.isRefreshing = false
                            }
                        }, 1500)
                    }
                })
                swipe_refresh3.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
                    override fun onRefresh() {
                        fruitList3.clear()
                        val cursor = db.query("Note", null, null, null, null, null, null)
                        if (cursor.moveToFirst()) {
                            do {
                                val title_one = cursor.getString(cursor.getColumnIndex("title"))
                                val class_one = cursor.getString(cursor.getColumnIndex("class"))
                                when (class_one) {
                                    "工作" -> {
                                        fruitList3.add(Fruit(title_one!!, R.drawable.icon_order))
                                        val adapter3 = FruitAdapter(fruitList3)
                                        recyclerView3.layoutManager = layoutManager3
                                        recyclerView3.adapter = adapter3
                                        adapter3.notifyDataSetChanged()
                                    }
                                }
                            } while (cursor.moveToNext())

                        }
                        cursor.close()
                        Handler().postDelayed({
                            if (swipe_refresh3.isRefreshing) {
                                swipe_refresh3.isRefreshing = false
                            }
                        }, 1500)
                    }
                })
                swipe_refresh4.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
                    override fun onRefresh() {
                        fruitList4.clear()
                        val cursor = db.query("Note", null, null, null, null, null, null)
                        if (cursor.moveToFirst()) {
                            do {
                                val title_one = cursor.getString(cursor.getColumnIndex("title"))
                                val class_one = cursor.getString(cursor.getColumnIndex("class"))
                                when (class_one) {
                                    "学习" -> {
                                        fruitList4.add(Fruit(title_one!!, R.drawable.icon_order))
                                        val adapter4 = FruitAdapter(fruitList4)
                                        recyclerView4.layoutManager = layoutManager4
                                        recyclerView4.adapter = adapter4
                                        adapter4.notifyDataSetChanged()
                                    }
                                }
                            } while (cursor.moveToNext())

                        }
                        cursor.close()
                        Handler().postDelayed({
                            if (swipe_refresh4.isRefreshing) {
                                swipe_refresh4.isRefreshing = false
                            }
                        }, 1500)
                    }
                })
//                //为下拉刷新，设置一组颜色
//                swipe_refresh1.setColorSchemeColors(Color.BLUE, Color.RED, Color.GREEN)
//                //设置触发刷新的距离
//                swipe_refresh1.setDistanceToTriggerSync(200)
//                //设置滑动的距离
//                swipe_refresh1.setSlingshotDistance(400)

            }
        }.start()
        super.onResume()
    }

    private fun initFruits1() {
        fruitList1.add(Fruit("还没有记事？在这里输入", R.drawable.icon_nothing))
    }

    private fun initFruits2() {
        fruitList2.add(Fruit("还没有记事？在这里输入", R.drawable.icon_nothing))
    }

    private fun initFruits3() {
        fruitList3.add(Fruit("还没有记事？在这里输入", R.drawable.icon_nothing))
    }

    private fun initFruits4() {
        fruitList4.add(Fruit("还没有记事？在这里输入", R.drawable.icon_nothing))
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OneFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OneFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    //内嵌recyclerView适配器
    inner class FruitAdapter(val fruitList: List<Fruit>) :
        RecyclerView.Adapter<FruitAdapter.ViewHolder>() {

        private var position = -1

        fun getPosition(): Int {
            return position
        }

        fun setPosition(position: Int) {
            this.position = position
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val fruitImage: ImageView = view.findViewById(R.id.fruitImage)
            val fruitName: TextView = view.findViewById(R.id.fruitName)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fruit_item, parent, false)

            //点击事件标椎写法
            val viewHolder = ViewHolder(view)
            viewHolder.itemView.setOnClickListener {
                val position = viewHolder.adapterPosition
                val fruit = fruitList[position]
                val intent =Intent(context, Detail::class.java)
                intent.putExtra("item", fruit.name)
                startActivity(intent)
            }
            viewHolder.fruitImage.setOnClickListener {
                val position = viewHolder.adapterPosition
                val fruit = fruitList[position]
                val intent =Intent(context, Detail::class.java)
                intent.putExtra("item", fruit.name)
                startActivity(intent)
            }
            return viewHolder
//        return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val fruit = fruitList[position]
            holder.fruitImage.setImageResource(fruit.imageID)
            holder.fruitName.text = fruit.name
            holder.itemView.isLongClickable = true
            holder.itemView.setOnLongClickListener {
                this@FruitAdapter.position = holder.adapterPosition
                val fruit_name =fruit.name
                //保存长按的列表项的title，方便删除操作
                val prefs_title = activity!!.getSharedPreferences("item_title",
                    Context.MODE_PRIVATE)
                val editor_title = prefs_title.edit()
                editor_title.putString("fruit_name", fruit_name)
                editor_title.apply()
                false
            }
        }

        override fun getItemCount() = fruitList.size

    }

}