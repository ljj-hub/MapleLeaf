package com.example.mapleleaf

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapleleaf.ui.fragment.OneFragment
import com.example.mapleleaf.ui.recycler.Fruit
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fragment_one_view_1.*
import kotlinx.android.synthetic.main.fragment_one_view_2.*
import kotlinx.android.synthetic.main.fragment_one_view_3.*
import kotlinx.android.synthetic.main.fragment_one_view_3.recyclerView3
import kotlinx.android.synthetic.main.fragment_one_view_4.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Detail : AppCompatActivity() {

    val takePhoto = 1
    val fromAlbum = 2
    lateinit var imageUri: Uri
    lateinit var outputImage: File
    private lateinit var bytes1: String
    private val fruitList = ArrayList<Fruit>()
    lateinit var bitmap: Bitmap
    fun isbitmapInitialzed()=::bitmap.isInitialized
    fun isbytes1Initialzed()=::bytes1.isInitialized

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val title =intent.getStringExtra("item")
        detail_title.setText(title)

        //设置编辑时的时间
        val formatter = SimpleDateFormat("YYYY-MM-dd") //设置时间格式
        formatter.timeZone = TimeZone.getTimeZone("GMT+08") //设置时区
        val curDate = Date(System.currentTimeMillis()) //获取当前时间
        val createDate: String = formatter.format(curDate) //格式转换

        fab1_detail.setOnClickListener{
            // 创建File对象，用于存储拍照后的图片
            outputImage = File(externalCacheDir, "output_image.jpg")
            if (outputImage.exists()) {
                outputImage.delete()
            }
            outputImage.createNewFile()
            imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(this, "com.example.MapleLeaf.fileprovider", outputImage);
            } else {
                Uri.fromFile(outputImage);
            }
            // 启动相机程序
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, takePhoto)
        }

        fab2_detail.setOnClickListener {
            // 打开文件选择器
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            // 指定只显示照片
            intent.type = "image/*"
            startActivityForResult(intent, fromAlbum)
        }

        val dbHelper = MyDatabaseHelper(this, "NoteBook.db", 1)
        //读取SQLite中的数据
        val db = dbHelper.writableDatabase
        val cursor = db.query("Note", null, "title =?", arrayOf(title), null, null, null)
        if (cursor.moveToFirst()){
            do {
                val title_detail = cursor.getString(cursor.getColumnIndex("title"))
                val context_detail = cursor.getString(cursor.getColumnIndex("context"))
                val time_detail = cursor.getString(cursor.getColumnIndex("time"))
                val class_detail = cursor.getString(cursor.getColumnIndex("class"))
                // byte[] —> Bitmap
                try {
                    bytes1 = (cursor.getBlob(cursor.getColumnIndex("bit_image"))).toString()
                }catch (e: Exception){

                }
                if (!isbytes1Initialzed()){

                }else{
                    val bytes: ByteArray = cursor.getBlob(cursor.getColumnIndex("bit_image"))
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
                    imageView_detail.setImageBitmap(bitmap)
                }
                when (class_detail) {
                    "生活" -> {
                        detail_spinner.setSelection(1)
                        detail_context.setText(context_detail)
                        detail_time.text =time_detail
                    }
                    "工作" -> {
                        detail_spinner.setSelection(2)
                        detail_context.setText(context_detail)
                        detail_time.text =time_detail
                    }
                    "学习" -> {
                        detail_spinner.setSelection(3)
                        detail_context.setText(context_detail)
                        detail_time.text =time_detail
                    }
                    else -> {
                        detail_context.setText(context_detail)
                        detail_time.text =time_detail
                    }
                }
            } while (cursor.moveToNext())
        }
        cursor.close()

        detail_finish.setOnClickListener {
            val db2 =dbHelper.writableDatabase
            db2.delete("Note","title =?", arrayOf(title))
            val detail_title = detail_title.text.toString()
            val detail_context = detail_context.text.toString()
            val detail_kind =detail_spinner.selectedItem.toString()
            if (!isbitmapInitialzed()){
                val values_detail = ContentValues().apply {
                    //开始组装数据
                    put("title",detail_title)
                    put("context",detail_context)
                    put("time",createDate)
                    put("class",detail_kind)
                }
                //插入数据
                db2.insert("Note",null,values_detail)
            }else{
                val values_detail = ContentValues().apply {
                    //开始组装数据
                    val by: ByteArray = bitmapToBytes(bitmap)!!
                    put("title",detail_title)
                    put("context",detail_context)
                    put("time",createDate)
                    put("class",detail_kind)
                    put("bit_image",by)
                }
                //插入数据
                db2.insert("Note",null,values_detail)
            }
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            takePhoto -> {
                if (resultCode == Activity.RESULT_OK) {
                    // 将拍摄的照片显示出来
                    bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
                    imageView_detail.setImageBitmap(rotateIfRequired(bitmap))
                }
            }
            fromAlbum -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let { uri ->
                        // 将选择的照片显示
                        bitmap = getBitmapFromUri(uri)!!
                        imageView_detail.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }

    private fun getBitmapFromUri(uri: Uri) = contentResolver.openFileDescriptor(uri, "r")?.use {
        BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
    }

    private fun rotateIfRequired(bitmap: Bitmap): Bitmap {
        val exif = ExifInterface(outputImage.path)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
            else -> bitmap
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        bitmap.recycle()
        return rotatedBitmap
    }

    fun bitmapToBytes(bitmap: Bitmap?): ByteArray? {
        if (bitmap == null) {
            return null
        }
        val os = ByteArrayOutputStream()
        // 将Bitmap压缩成PNG编码，质量为100%存储
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os) //除了PNG还有很多常见格式，如jpeg等。
        return os.toByteArray()
    }

}