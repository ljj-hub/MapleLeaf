package com.example.mapleleaf

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mapleleaf.ui.recycler.Fruit
import kotlinx.android.synthetic.main.fragment_one_view_2.*


class Search : AppCompatActivity() {

    private lateinit var mEditText: EditText
    private lateinit var mImageView: ImageView
    private lateinit var mListView: RecyclerView
    private lateinit var mTextView: TextView
    lateinit var context: Context
    lateinit var cursor: Cursor
    val fruitList = ArrayList<Fruit>()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.mapleleaf.R.layout.activity_search)
        context = this

//        //将背景图与状态栏融合
//        val decorView = window.decorView
//        decorView.systemUiVisibility =
//            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//        window.statusBarColor = Color.rgb(255, 51, 51)

        initView()
    }

    private fun initView() {
        mTextView = findViewById<View>(com.example.mapleleaf.R.id.textview) as TextView
        mEditText = findViewById<View>(com.example.mapleleaf.R.id.edittext) as EditText
        mImageView = findViewById<View>(com.example.mapleleaf.R.id.imageview) as ImageView
        mListView = findViewById<View>(com.example.mapleleaf.R.id.listview) as RecyclerView

        //设置删除图片的点击事件
        mImageView.setOnClickListener { //把EditText内容设置为空
            mEditText.setText("")
            //把ListView隐藏
            mListView.visibility = View.GONE
        }

        //EditText添加监听
        mEditText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            } //文本改变之前执行

            //文本改变的时候执行
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //如果长度为0
                if (s.isEmpty()) {
                    //隐藏“删除”图片
                    mImageView!!.visibility = View.GONE
                } else { //长度不为0
                    //显示“删除图片”
                    mImageView!!.visibility = View.VISIBLE
                    //显示ListView
                    showListView()
                }
            }

            override fun afterTextChanged(s: Editable) {} //文本改变之后执行
        })

        mTextView.setOnClickListener {
            //如果输入框内容为空，提示请输入搜索内容
            if (TextUtils.isEmpty(mEditText.text.toString().trim { it <= ' ' })) {
                Toast.makeText(this,"请输入搜索内容",Toast.LENGTH_SHORT).show()
            } else {
                //判断cursor是否为空
                if (cursor != null) {
                    val columnCount = cursor.count
                    if (columnCount == 0) {
                        Toast.makeText(this,"搜索结果为空",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this,"已发现$columnCount"+"个结果",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun showListView() {
        mListView!!.visibility = View.VISIBLE
        //获得输入的内容
        val str = mEditText!!.text.toString().trim { it <= ' ' }
        fruitList.clear()
        val dbHelper = MyDatabaseHelper(this, "NoteBook.db", 1)
        //读取SQLite中的数据
        val db = dbHelper.writableDatabase
        //查询Book表中所有的数据
        cursor = db.query("Note", null, "title =?", arrayOf(str), null, null, null)
        if (cursor.moveToFirst()){
            do{
                val title = cursor.getString(cursor.getColumnIndex("title"))
//                if (str == title){
                    fruitList.add(Fruit(title!!, R.drawable.icon_order))
                    val layoutManager = LinearLayoutManager(this)
                    mListView.layoutManager = layoutManager
                    val adapter = FruitAdapter(fruitList)
                    mListView.adapter = adapter
                    adapter.notifyDataSetChanged()
//                }
            }while (cursor.moveToNext())
        }
        cursor.close()
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
                val intent = Intent(context, Detail::class.java)
                intent.putExtra("item", fruit.name)
                startActivity(intent)
            }
            viewHolder.fruitImage.setOnClickListener {
                val position = viewHolder.adapterPosition
                val fruit = fruitList[position]
                val intent = Intent(context, Detail::class.java)
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
        }

        override fun getItemCount() = fruitList.size

    }

}