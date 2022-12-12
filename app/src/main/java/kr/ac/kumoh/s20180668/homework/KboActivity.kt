package kr.ac.kumoh.s20180668.homework

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.LruCache
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import kr.ac.kumoh.s20180668.homework.databinding.ActivityKboBinding

class KboActivity : AppCompatActivity() {
    companion object {
        const val KEY_NAME = "KboName"
        const val KEY_TEAM = "KboTeam"
        const val KEY_IMAGE = "KboImage"
    }

    private lateinit var binding: ActivityKboBinding
    private lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKboBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageLoader = ImageLoader(Volley.newRequestQueue(this),
            object : ImageLoader.ImageCache {
                private val cache = LruCache<String, Bitmap>(100)
                override fun getBitmap(url: String): Bitmap? {
                    return cache.get(url)
                }

                override fun putBitmap(url: String, bitmap: Bitmap?) {
                    cache.put(url, bitmap)
                }
            })

        binding.imageKbo.setImageUrl(intent.getStringExtra(KEY_IMAGE), imageLoader)
        binding.textTeam.text = intent.getStringExtra(KEY_TEAM)
        binding.textName.text = intent.getStringExtra(KEY_NAME)
    }
}