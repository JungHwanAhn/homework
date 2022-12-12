package kr.ac.kumoh.s20180668.homework

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.NetworkImageView
import kr.ac.kumoh.s20180668.homework.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var model: KboViewModel
    private val kboAdapter = KboAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this)[KboViewModel::class.java]

        binding.list.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = kboAdapter
        }

        model.list.observe(this) {
            kboAdapter.notifyItemRangeInserted(0, model.list.value?.size ?: 0)
        }

        model.requestKbo()
    }

    inner class KboAdapter: RecyclerView.Adapter<KboAdapter.ViewHolder>() {
        inner class ViewHolder(itemView: View)
            : RecyclerView.ViewHolder(itemView), OnClickListener {
            val txTeam: TextView = itemView.findViewById(R.id.text1)
            val txName: TextView = itemView.findViewById(R.id.text2)

            val niImage: NetworkImageView = itemView.findViewById<NetworkImageView>(R.id.image)

            init {
                niImage.setDefaultImageResId(android.R.drawable.ic_menu_report_image)
                itemView.setOnClickListener(this)
            }

            override fun onClick(v: View?) {
                val intent = Intent(application, KboActivity::class.java)
                intent.putExtra(KboActivity.KEY_TEAM,
                    model.list.value?.get(adapterPosition)?.team)
                intent.putExtra(KboActivity.KEY_NAME,
                    model.list.value?.get(adapterPosition)?.name)
                intent.putExtra(KboActivity.KEY_IMAGE,
                    model.getImageUrl(adapterPosition))
                startActivity(intent)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = layoutInflater.inflate(R.layout.item_kbo,
                parent,
                false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.txTeam.text = model.list.value?.get(position)?.team ?: null
            holder.txName.text = model.list.value?.get(position)?.name ?: null
            holder.niImage.setImageUrl(model.getImageUrl(position), model.imageLoader)
        }

        override fun getItemCount() = model.list.value?.size ?: 0
    }
}