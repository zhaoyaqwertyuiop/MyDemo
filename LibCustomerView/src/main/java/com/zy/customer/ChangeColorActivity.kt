package com.zy.customer

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.zy.customer.databinding.CustomerActivityChangeColorBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChangeColorActivity : AppCompatActivity(){

    private val binding by lazy { CustomerActivityChangeColorBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.changeTV.setProgress(progress / 100f)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        binding.autoBtn.setOnClickListener {
            lifecycleScope.launch {
                var pregress = 0f
                while (pregress < 1) {
                    binding.changeTV.autoProgress(pregress)
                    delay(100)
                    pregress += 0.01f
                }
            }
            binding.changeTV.start()
        }
    }
}