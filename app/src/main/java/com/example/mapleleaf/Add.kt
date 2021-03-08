package com.example.mapleleaf

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_add.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class Add : AppCompatActivity() {

    val takePhoto = 1
    val fromAlbum = 2
    lateinit var imageUri: Uri
    lateinit var outputImage: File
    lateinit var bitmap: Bitmap
    fun isbitmapInitialzed()=::bitmap.isInitialized

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        //设置编辑时的时间
        val formatter = SimpleDateFormat("YYYY-MM-dd") //设置时间格式
        formatter.timeZone = TimeZone.getTimeZone("GMT+08") //设置时区
        val curDate = Date(System.currentTimeMillis()) //获取当前时间
        val createDate: String = formatter.format(curDate) //格式转换
        time.text =createDate

        fab1_add.setOnClickListener {
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

        fab2_add.setOnClickListener {
            // 打开文件选择器
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            // 指定只显示照片
            intent.type = "image/*"
            startActivityForResult(intent, fromAlbum)
        }

        //完成按钮被点击时跳转MainActivity
        btn_finish.setOnClickListener {
            val edit_title = edit_title.text.toString()
            val edit_context = edit_context.text.toString()
            val kind =spinner.selectedItem.toString()
            //对输入内容进行判断
            when {
                TextUtils.isEmpty(edit_title) -> {
                    Toast.makeText(this, "请输入记事标题", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(edit_context) -> {
                    Toast.makeText(this, "请输入记事内容", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    //创建SQLite数据库保存数据
                    val dbHelper =MyDatabaseHelper(this, "NoteBook.db", 1)
                    dbHelper.writableDatabase
                    //插入操作
                    val db =dbHelper.writableDatabase
                    if (!isbitmapInitialzed()){
                        val values_add = ContentValues().apply {
                            //开始组装数据
                            put("title", edit_title)
                            put("context", edit_context)
                            put("time", createDate)
                            put("class", kind)
                        }
                        //插入数据
                        db.insert("Note", null, values_add)
                    }else{
                        val by: ByteArray = bitmapToBytes(bitmap)!!
                        val values_add = ContentValues().apply {
                            //开始组装数据
                            put("title", edit_title)
                            put("context", edit_context)
                            put("time", createDate)
                            put("class", kind)
                            put("bit_image",by)
                        }
                        //插入数据
                        db.insert("Note", null, values_add)
                    }
                    val intent =Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            takePhoto -> {
                if (resultCode == Activity.RESULT_OK) {
                    // 将拍摄的照片显示出来
                    bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
                    imageView_add.setImageBitmap(rotateIfRequired(bitmap!!))
                }
            }
            fromAlbum -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let { uri ->
                        // 将选择的照片显示
                        bitmap = getBitmapFromUri(uri)!!
                        imageView_add.setImageBitmap(bitmap)
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
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL)
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