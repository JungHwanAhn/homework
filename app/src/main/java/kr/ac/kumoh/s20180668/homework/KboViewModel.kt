package kr.ac.kumoh.s20180668.homework

import android.app.Application
import android.graphics.Bitmap
import android.util.LruCache
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder

class KboViewModel (application: Application): AndroidViewModel(application) {
    data class Kbo (var id: Int, var team: String, var name: String, var image: String)

    companion object {
        const val QUEUE_TAG = "KboVolleyRequest"

        const val SERVER_URL = "https://expressdb-rdgpm.run.goorm.io"
    }

    private val kbo = ArrayList<Kbo>()
    private val _list = MutableLiveData<ArrayList<Kbo>>()
    val list: LiveData<ArrayList<Kbo>>
        get() = _list


    private var queue: RequestQueue
    val imageLoader: ImageLoader

    init {
        _list.value = kbo
        queue = Volley.newRequestQueue(getApplication())

        imageLoader = ImageLoader(queue,
            object : ImageLoader.ImageCache {
                private val cache = LruCache<String, Bitmap>(100)
                override fun getBitmap(url: String): Bitmap? {
                    return cache.get(url)
                }
                override fun putBitmap(url: String, bitmap: Bitmap) {
                    cache.put(url, bitmap)
                }
            })
    }

    fun getImageUrl(i: Int): String = "$SERVER_URL/image/" + URLEncoder.encode(kbo[i].image, "utf-8")

    fun requestKbo() {
        val request = JsonArrayRequest (
            Request.Method.GET,
            "$SERVER_URL/",
            null,
            {
                //Toast.makeText(getApplication(), it.toString(), Toast.LENGTH_LONG).show()
                kbo.clear()
                parseJson(it)
                _list.value = kbo
            },
            {
                Toast.makeText(getApplication(), it.toString(), Toast.LENGTH_LONG).show()
            }
        )

        request.tag = QUEUE_TAG
        queue.add(request)
    }

    private fun parseJson(items: JSONArray) {
        for (i in 0 until items.length()) {
            val item: JSONObject = items[i] as JSONObject
            val id = item.getInt("id")
            val team = item.getString("team")
            val name = item.getString("name")
            //val position = item.getString("position")
            val image = item.getString("image")

            kbo.add(Kbo(id, team, name, image))
        }
    }

    override fun onCleared() {
        super.onCleared()
        queue.cancelAll(QUEUE_TAG)
    }
}