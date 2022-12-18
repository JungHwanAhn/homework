package kr.ac.kumoh.s20180668.homework

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.LruCache
import android.view.View
import android.widget.Button
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

        val btn_event = findViewById<Button>(R.id.button)

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


        btn_event.setOnClickListener {
            var name = ""
            when (binding.textName.text) {
                "구창모" -> {
                    name = "구창모(야구선수)"
                }
                "박민우" -> {
                    name = "박민우(야구선수)"
                }
                "김재환" -> {
                    name = "김재환(야구선수)"
                }
                "김현수" -> {
                    name = "김현수(1988)"
                }
                "소크라테스" -> {
                    name = "소크라테스 브리토"
                }
                else -> {
                    name = binding.textName.text.toString()
                }
            }
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://namu.wiki/w/"+ name))
            startActivity(intent)
        }
    }
}