package ikhwan.binar.binarchallengelima.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ikhwan.binar.binarchallengelima.databinding.ItemNowBinding
import ikhwan.binar.binarchallengelima.model.nowplaying.ResultNow

class NowPlayingAdapter(val onItemClick: OnClickListener): RecyclerView.Adapter<NowPlayingAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemNowBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(data: ResultNow) {
            if (data.releaseDate != "") {
                val list = data.releaseDate.split("-").toTypedArray()
                val day = list[2]
                var month = ""
                val year = list[0]

                when {
                    list[1] == "01" -> {
                        month = "Jan"
                    }
                    list[1] == "02" -> {
                        month = "Feb"
                    }
                    list[1] == "03" -> {
                        month = "Mar"
                    }
                    list[1] == "04" -> {
                        month = "Apr"
                    }
                    list[1] == "05" -> {
                        month = "May"
                    }
                    list[1] == "06" -> {
                        month = "Jun"
                    }
                    list[1] == "07" -> {
                        month = "Jul"
                    }
                    list[1] == "08" -> {
                        month = "Aug"
                    }
                    list[1] == "09" -> {
                        month = "Sep"
                    }
                    list[1] == "10" -> {
                        month = "Oct"
                    }
                    list[1] == "11" -> {
                        month = "Nov"
                    }
                    list[1] == "12" -> {
                        month = "Dec"
                    }
                }
                val date = "$month $day, $year"
                binding.tvDate.text = date
            }
            binding.apply {
                val baseUrlImg = "https://image.tmdb.org/t/p/w500/"
                val urlImage = baseUrlImg + data.posterPath
                tvMovie.text = data.title
                Glide.with(itemView).load(urlImage).into(imgMovie)
                tvRating.text = data.voteAverage.toString()
                root.setOnClickListener {
                    onItemClick.onClickItem(data)
                }
                when (data.voteAverage) {
                    in 7.0..10.0 -> {
                        tvRating.setTextColor(Color.parseColor("#21d07a"))
                        cvRatingYellow.visibility = View.INVISIBLE
                        cvRatingRed.visibility = View.INVISIBLE
                    }
                    in 4.0..7.0 -> {
                        tvRating.setTextColor(Color.parseColor("#FFFB00"))
                        cvRatingYellow.visibility = View.VISIBLE
                    }
                    else -> {
                        tvRating.setTextColor(Color.parseColor("#db2360"))
                        cvRatingRed.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<ResultNow>(){
        override fun areItemsTheSame(oldItem: ResultNow, newItem: ResultNow): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ResultNow, newItem: ResultNow): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitData(value: List<ResultNow>?) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemNowBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = differ.currentList[position]

        data.let {
            holder.bind(data)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    interface OnClickListener{
        fun onClickItem(data: ResultNow)
    }
}