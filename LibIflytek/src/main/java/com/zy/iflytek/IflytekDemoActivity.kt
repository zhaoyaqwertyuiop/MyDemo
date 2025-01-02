package com.zy.iflytek

import android.Manifest
import android.annotation.SuppressLint
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.zy.iflytek.databinding.IflytekActivityMainBinding
import com.zy.iflytek.util.DeleteFeature
import com.zy.iflytek.util.DeleteGroup
import com.zy.iflytek.util.QueryFeatureList
import com.zy.iflytek.util.SearchOneFeature
import com.zy.iflytek.voice.VoiceRecogManager
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale


class IflytekDemoActivity : AppCompatActivity(){

    private val binding by lazy { IflytekActivityMainBinding.inflate(layoutInflater)}
    private val context by lazy {this}

    private val requestUrl: String = "https://api.xf-yun.com/v1/private/s782b4996"

    //控制台获取以下信息
//    private val APPID: String = "c08ef16b"
//    private val apiSecret: String = "ZGJlNjI4MTEwNzU5MDMwZTFkZmRiNjY2"
//    private val apiKey: String = "fbe0b21407ee8642d05bba116da0b51a"

    private val APPID: String = "21fc7db0"
    private val apiSecret: String = "ZDdkYTlhY2YwNDZiM2NkZjA1NTM4NTA3"
    private val apiKey: String = "c853fcb0efe3770688cfe67cc89f74b1"

    //音频存放位置(比对功能请注意更换音频)
    private val AUDIO_PATH: String = "audioExample/讯飞开放平台.mp3"

    /**1.创建声纹特征库*/
    //CreateGroup.doCreateGroup(requestUrl,APPID,apiSecret,apiKey);
    /**2.添加音频特征*/
    //CreateFeature.doCreateFeature(requestUrl,APPID,apiSecret,apiKey,AUDIO_PATH);
    /**3.查询特征列表*/
    //QueryFeatureList.doQueryFeatureList(requestUrl,APPID,apiSecret,apiKey);
    /**4.特征比对1:1*/
    //SearchOneFeature.doSearchOneFeature(requestUrl,APPID,apiSecret,apiKey,AUDIO_PATH);
    /**5.特征比对1:N*/
    //SearchFeature.doSearchFeature(requestUrl,APPID,apiSecret,apiKey,AUDIO_PATH);
    /**6.更新音频特征*/
    //UpdateFeature.doUpdateFeature(requestUrl,APPID,apiSecret,apiKey,AUDIO_PATH);
    /**7.删除指定特征*/
    //DeleteFeature.doDeleteFeature(requestUrl,APPID,apiSecret,apiKey);
    /**8.删除声纹特征库*/
    //DeleteGroup.doDeleteGroup(requestUrl,APPID,apiSecret,apiKey);

    private var handlerThread: HandlerThread? = null
    private var childHandler: Handler? = null

    private val PERMISSIONS_REQUEST_CODE = 10
    private val PERMISSIONS_REQUIRED = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO,
        )

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnsafeOptInUsageError")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        requestPermissions(PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE)

        // 创建一个新的 HandlerThread
        handlerThread = HandlerThread("MyHandlerThread");
        handlerThread!!.start()

        // 获取 HandlerThread 的 Looper
        val looper = handlerThread!!.looper
        childHandler = Handler(looper)

        binding.doCreateGroup.setOnClickListener {
            /**1.创建声纹特征库 */
//            childHandler!!.post {
//                CreateGroup.doCreateGroup(requestUrl, APPID, apiSecret, apiKey)
//            }
//            text字段Base64解码后=>{"groupName":"iFLYTEK_examples_groupName","groupId":"iFLYTEK_examples_groupId","groupInfo":"iFLYTEK_examples_groupInfo"}

            VoiceRecogManager.o.creatGroup {
                if (it != null) {
                    println("创建声纹特征库成功:" + Gson().toJson(it))
                }
            }
        }
        binding.doCreateFeature.setOnClickListener {
            /**2.添加音频特征*/
//            childHandler!!.post {
//                val audioFile = File(getExternalFilesDir("xmbase/test"), "讯飞开放平台.mp3")
//                CreateFeature.doCreateFeature(requestUrl,APPID,apiSecret,apiKey, audioFile.absolutePath)
//            }
//            file = File(getExternalFilesDir("xmbase/test"), "讯飞开放平台.mp3")
            file = File(getExternalFilesDir("xmbase/test"), "voice#262503473#20241230115843.pcm.ico")
            file?.let {
                val audio = VoiceRecogManager.read(it.absolutePath)
                VoiceRecogManager.o.doCreateFeature("featureId_01", audio) {
                    if (it != null) {
                        LogUtils.d("添加声纹特征库成功：" + Gson().toJson(it))
                    }
                }
            }

        }
        binding.doQueryFeatureList.setOnClickListener{
            /**3.查询特征列表*/
            childHandler!!.post {
                QueryFeatureList.doQueryFeatureList(requestUrl,APPID,apiSecret,apiKey)
            }
            VoiceRecogManager.o.doQueryFeatureList {
                if (it != null) {
                    LogUtils.d("查询特征列表：" + Gson().toJson(it))
                }
            }
        }
        binding.doSearchOneFeature.setOnClickListener{
            /**4.特征比对1:1*/
            childHandler!!.post {
                SearchOneFeature.doSearchOneFeature(requestUrl,APPID,apiSecret,apiKey,AUDIO_PATH);
            }
        }
        binding.doSearchFeature.setOnClickListener{
            /**5.特征比对1:N*/
//            childHandler!!.post {
//                SearchFeature.doSearchFeature(requestUrl,APPID,apiSecret,apiKey,AUDIO_PATH);
//            }
            file = File(getExternalFilesDir("xmbase/test"), "讯飞开放平台.mp3")
            file?.let {
                val audio = VoiceRecogManager.read(it.absolutePath)
                VoiceRecogManager.o.doSearchFeature(audio) {
                    if (it != null) {
                        LogUtils.d("特征比对1:N：" + Gson().toJson(it))
                    }
                }
            }
        }
        binding.doUpdateFeature.setOnClickListener{
            /**6.更新音频特征*/
//            childHandler!!.post {
//                UpdateFeature.doUpdateFeature(requestUrl, APPID, apiSecret, apiKey, AUDIO_PATH);
//            }
            file = File(getExternalFilesDir("xmbase/test"), "明天天气怎么样.mp3")
            file?.let {
                val audio = VoiceRecogManager.read(it.absolutePath)
                VoiceRecogManager.o.doCreateFeature("featureId_02", audio) {
                    if (it != null) {
                        LogUtils.d("添加声纹特征成功：" + Gson().toJson(it))
                    }
                }
            }
        }
        binding.doDeleteFeature.setOnClickListener{
            /**7.删除指定特征*/
//            childHandler!!.post {
//                DeleteFeature.doDeleteFeature(requestUrl, APPID, apiSecret, apiKey);
//            }
            val featureId = "featureId_02"
            VoiceRecogManager.o.doDeleteFeature(featureId) {
                if (it != null && it) {
                    LogUtils.d("删除声纹特征成功：" + featureId)
                } else {
                    LogUtils.d("删除声纹特征失败：")
                }
            }
        }
        binding.doDeleteGroup.setOnClickListener{
            /**8.删除声纹特征库*/
            childHandler!!.post {
                DeleteGroup.doDeleteGroup(requestUrl, APPID, apiSecret, apiKey);
            }
        }
        
//        binding.record.setOnTouchListener { v, event ->
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> {
//
//                }
//                MotionEvent.ACTION_UP -> {
//                    stopRecord()
//                }
//            }
//            true
//        }

        binding.record.setOnClickListener{
            if (!isRecoding) {
                startRecord()
                binding.record.setText("停止录音")
            } else {
                stopRecord()
                binding.record.setText("录制音频")
            }
        }

    }

//    val recorder = AudioRecorder()
    val recorder = MediaRecorder()
    var file: File? = null
    var isRecoding = false
    @SuppressLint("WrongConstant")
    private fun startRecord() {
        if (isRecoding) {
            return
        }
        isRecoding = true
        val sdf = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        val fileName = sdf.format(System.currentTimeMillis()) + ".mp3"
        file = File(getExternalFilesDir("xmbase/test"), fileName)

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC) // 设置音频源为麦克风
//        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4) // 设置输出格式
//        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC) // 设置音频编码器
        recorder.setOutputFormat(MediaRecorder.OutputFormat.OGG) // 设置输出格式
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.OPUS) // 设置音频编码器
        recorder.setOutputFile(file!!.absolutePath) // 设置输出文件路径


        try {
            recorder.prepare() // 准备录制
            recorder.start() // 开始录制
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // 开始录音
//        val sdf = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
//        val fileName = sdf.format(System.currentTimeMillis()) + ".mp3"
//        file = File(getExternalFilesDir("xmbase/test"), fileName)
//        recorder.startRecording()
//        LogUtils.d(file!!.absolutePath)
//
//        Thread({
//            recorder.recordAudio(file!!.absolutePath)
//        }).start()
    }
    
    private fun stopRecord() {
        isRecoding = false
        recorder.stop(); // 停止录制
        recorder.release(); // 释放资源

//        recorder.stopRecording()
    }
}