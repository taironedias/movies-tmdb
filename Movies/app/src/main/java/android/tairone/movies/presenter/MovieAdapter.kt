package android.tairone.movies.presenter
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.tairone.movies.OverviewActivity
import android.tairone.movies.R
import android.tairone.movies.model.CustomItemClickListener
import android.tairone.movies.model.MovieResults
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.layout_movies.view.*
import java.text.FieldPosition

class MovieAdapter(val context : Context, var movieList: List<MovieResults.ResultsBean>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(), Filterable {

    var movieListFull: List<MovieResults.ResultsBean> = ArrayList(movieList)

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MovieViewHolder {
        var view: View = LayoutInflater.from(p0.context).inflate(R.layout.layout_movies, p0, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(movieViewHolder: MovieViewHolder, p1: Int) {
        val movie: MovieResults.ResultsBean = movieList.get(p1)
        movieViewHolder.textView.text = movie.title

        val urlImage: String = "https://image.tmdb.org/t/p/w500/"+movie.backdrop_path
        ImageFromURL(movieViewHolder.imageView).execute(urlImage)

        movieViewHolder.setOnCustomItemClickListener(object : CustomItemClickListener {
            override fun onCustomItemClickListener(view: View, position: Int) {
                Log.i("Dias", "Position: $position")
                goToOverview(movie)
            }
        })
    }

    fun goToOverview(data: MovieResults.ResultsBean) {
        val intent = Intent(context, OverviewActivity::class.java)
        intent.putExtra("title", data.title)
        intent.putExtra("overview", data.overview)
        intent.putExtra("backdrop_path", data.backdrop_path)
        context.startActivity(intent)
    }

    /*override fun getFilter(): Filter {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }*/

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {

                val filteredList: ArrayList<MovieResults.ResultsBean> = ArrayList<MovieResults.ResultsBean>()

                if (charSequence == null || charSequence.length == 0) {
                    filteredList.addAll(movieListFull)
                } else {
                    val filterPattern: String = charSequence.toString().toLowerCase().trim()

                    for (item in movieListFull) {
                        if (item.title.toLowerCase().contains(filterPattern)) {
                            filteredList.add(item)
                        }
                    }
                }

                val results: FilterResults = FilterResults()
                results.values = filteredList

                return results
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                val temp = ArrayList<MovieResults.ResultsBean>()
                temp.addAll(filterResults.values as List<MovieResults.ResultsBean>)
                movieList = temp
                Log.i("Dias", "Size movieList: "+movieList.size)
                notifyDataSetChanged()
            }
        }
    }


    class MovieViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var imageView: ImageView
        var textView: TextView
        var customItemClickListener: CustomItemClickListener? = null

        init {
            imageView = itemView.findViewById(R.id.imageMovie)
            textView = itemView.findViewById(R.id.labelTitleMovie)
            itemView.setOnClickListener(this)
        }

        fun setOnCustomItemClickListener(itemClickListener: CustomItemClickListener) {
            this.customItemClickListener = itemClickListener
        }

        override fun onClick(view: View?) {
            this.customItemClickListener!!.onCustomItemClickListener(view!!, adapterPosition)
        }

    }
}
