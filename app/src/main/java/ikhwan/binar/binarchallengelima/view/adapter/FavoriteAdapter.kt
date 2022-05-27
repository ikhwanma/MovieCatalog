package ikhwan.binar.binarchallengelima.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ikhwan.binar.binarchallengelima.data.room.Favorite
import ikhwan.binar.binarchallengelima.databinding.ItemFavoriteBinding

class FavoriteAdapter(val onItemClick: OnClickListener) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemFavoriteBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Favorite){
            binding.apply {
                val baseUrlImg = "https://image.tmdb.org/t/p/w500/"
                val urlImage = baseUrlImg + data.imgMovie

                Glide.with(itemView).load(urlImage).into(imgMovie)

                root.setOnClickListener {
                    onItemClick.onClickItem(data)
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Favorite>(){
        override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitData(value: List<Favorite>?) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemFavoriteBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = differ.currentList[itemCount - position -1]
        data.let {
            holder.bind(data)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    interface OnClickListener{
        fun onClickItem(data: Favorite)
    }
}