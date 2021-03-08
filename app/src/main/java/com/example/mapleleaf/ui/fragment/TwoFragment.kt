package com.example.mapleleaf.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapleleaf.R
import com.example.mapleleaf.ui.recycler.Msg
import com.example.mapleleaf.ui.recycler.MsgAdapter
import kotlinx.android.synthetic.main.fragment_two.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TwoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("UNREACHABLE_CODE")
class TwoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val msgList = ArrayList<Msg>()
    private lateinit var adapter: MsgAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_two, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initMsg()
        val layoutManager = LinearLayoutManager(context)
        recyclerview.layoutManager = layoutManager
        if (!::adapter.isInitialized) {
            adapter = MsgAdapter(msgList)
        }
        recyclerview.adapter = adapter
        val send: Button = activity!!.findViewById<View>(R.id.send) as Button
        send.setOnClickListener {
            onClick(send)
        }

    }

    private fun onClick(v: View?) {
        when (v) {
            send -> {
                val content = inputText.text.toString()
                if (content.isNotEmpty()) {
                    val msg = Msg(content, Msg.TYPE_SENT)
                    msgList.add(msg)
                    adapter.notifyItemInserted(msgList.size - 1) // 当有新消息时，刷新RecyclerView中的显示
                    recyclerview.scrollToPosition(msgList.size - 1)  // 将 RecyclerView定位到最后一行
                    inputText.setText("") // 清空输入框中的内容
                }
            }
        }
    }

    private fun initMsg() {
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TwoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TwoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}