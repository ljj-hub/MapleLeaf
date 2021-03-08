package com.example.mapleleaf.ui.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapleleaf.R
import com.example.mapleleaf.ui.login.Login
import com.example.mapleleaf.ui.recycler.Msg
import com.example.mapleleaf.ui.recycler.MsgAdapter
import kotlinx.android.synthetic.main.fragment_one_view_1.*
import kotlinx.android.synthetic.main.fragment_three.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ThreeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("UNREACHABLE_CODE")
class ThreeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_three, container, false)
    }

    //为fragment中各控件添加点击监听事件
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val btn_exit: Button = activity!!.findViewById<View>(R.id.tab_exit) as Button
        btn_exit.setOnClickListener {
            val intent = Intent(activity, Login::class.java)
            startActivity(intent)
            activity!!.finish()
        }

        val textView_set = activity!!.findViewById<View>(R.id.tab_set)
        textView_set.setOnClickListener { Toast.makeText(activity, "有待开发中", Toast.LENGTH_SHORT).show() }

        val textView_change = activity!!.findViewById<View>(R.id.tab_change)
        textView_change.setOnClickListener {
            Toast.makeText(activity, "有待开发中", Toast.LENGTH_SHORT).show()
        }

        val textView_update = activity!!.findViewById<View>(R.id.tab_update)
        textView_update.setOnClickListener {
            AlertDialog.Builder(activity)
                .setTitle("提示")
                .setMessage("此版本已是最新版本\n\n无需更新")
                .setCancelable(true)
                .setPositiveButton("确定", null)
                .show()
        }

        //显示用户名
        val prefs_user = activity!!.getSharedPreferences("user", Context.MODE_PRIVATE)
        val username =prefs_user.getString("success","")
        val setUsername =activity!!.findViewById<View>(R.id.name) as TextView
        setUsername.text =username
        if (activity!!.isFinishing){
            val editor_user = prefs_user.edit()
            editor_user.clear()
            editor_user.apply()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ThreeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ThreeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}