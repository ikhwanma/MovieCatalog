package ikhwan.binar.binarchallengelima.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ikhwan.binar.binarchallengelima.databinding.ItemMovieLinearBinding
import ikhwan.binar.binarchallengelima.model.popularmovie.ResultMovie

class MovieLinearAdapter(val onItemClick: MovieLinearAdapter.OnClickListener) :
    RecyclerView.Adapter<MovieLinearAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemMovieLinearBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResultMovie) {
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
                        month = "Des"
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

    private val diffCallback = object : DiffUtil.ItemCallback<ResultMovie>() {
        override fun areItemsTheSame(oldItem: ResultMovie, newItem: ResultMovie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ResultMovie, newItem: ResultMovie): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitData(value: List<ResultMovie>?) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemMovieLinearBinding.inflate(inflater, parent, false))
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
        fun onClickItem(data: ResultMovie)
    }
}