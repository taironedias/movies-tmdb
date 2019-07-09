package android.tairone.movies.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.tairone.movies.R
import android.util.Log
import android.view.*
import android.support.v7.widget.SearchView
import android.tairone.movies.model.MovieResults
import android.tairone.movies.presenter.MovieAdapter
import android.tairone.movies.presenter.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadFragment()
        setHasOptionsMenu(true)
    }

    fun loadFragment() {
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    var movieAdapter: MovieAdapter? = null
    var recyclerView: RecyclerView? = null
    var listMovies: List<MovieResults.ResultsBean>? = null
    var listMoviesResearched: List<MovieResults.ResultsBean>? = null
    val TAM: Int = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_main, container, false)

        pageViewModel.text.observe(this, Observer<String> {
            getMoviesByGenre(it!!.toInt())
        })


        recyclerView = root.findViewById(R.id.recyclerView)
        recyclerView!!.setHasFixedSize(true)

        val gridLayoutManager: GridLayoutManager = GridLayoutManager(context,2)
        recyclerView!!.layoutManager = gridLayoutManager as RecyclerView.LayoutManager?



        return root
    }


    private var CATEGORY: String? = null
    private val API_KEY = "3a002056d6ffbf6148d1918e57ae7849"
    private val INCLUDE_ADULT = false
    private val PAGE = 1

    fun getMoviesByGenre(with_genre: Int) {

        CATEGORY = "discover"
        val language = getLanguageForApi()
        val sort_by = "popularity.desc"
        val include_video = false


        val call = RetrofitInitializer().api_interface()
            .getListMoviesGenres(CATEGORY!!, API_KEY, language, sort_by, INCLUDE_ADULT, include_video, PAGE, with_genre)

        call.enqueue(object : Callback<MovieResults> {
            override fun onResponse(call: Call<MovieResults>, response: Response<MovieResults>) {
                response?.body()?.let {
                    val movieResults: MovieResults = it
                    listMovies = movieResults.results
                    listMovies = limitingList(listMovies!!, TAM)

                    movieAdapter = MovieAdapter(context!!, listMovies!!)
                    recyclerView!!.adapter = movieAdapter

                }
            }

            override fun onFailure(call: Call<MovieResults>, t: Throwable) {
                Log.e("Dias", "Erro na busca dos filmes por gênero na API!")
            }
        })
    }

    fun getSearchMovie(query: String) {

        CATEGORY = "search"
        val language = getLanguageForApi()

        val call = RetrofitInitializer().api_interface()
            .getListMoviesResearched(CATEGORY!!, API_KEY, language, query, PAGE, INCLUDE_ADULT)

        call.enqueue(object : Callback<MovieResults> {
            override fun onResponse(call: Call<MovieResults>, response: Response<MovieResults>) {
                response?.body()?.let {
                    val movieResults: MovieResults = it

                    if (movieResults.results.size > 0) {
                        listMoviesResearched = movieResults.results

                        if (listMoviesResearched!!.size > 4) {
                            listMoviesResearched = limitingList(listMoviesResearched!!, 4)
                        }

                        movieAdapter = MovieAdapter(context!!, listMoviesResearched!!)
                        recyclerView!!.adapter = movieAdapter
                    }


                }
            }

            override fun onFailure(call: Call<MovieResults>, t: Throwable) {
                Log.e("Dias", "Erro na pesquisa dos filmes pela API!")
            }
        })
    }


    fun limitingList(list: List<MovieResults.ResultsBean>, size: Int) : List<MovieResults.ResultsBean> {
        val newList = ArrayList<MovieResults.ResultsBean>()
        var count = 0
        for (item in list) {
            if (count < size) {
                newList.add(item)
                count += 1
            }
        }

        return newList
    }

    fun getLanguageForApi(): String {
        val languageDevice = Locale.getDefault().getDisplayLanguage()
        if (languageDevice.toLowerCase().equals("português"))
            return "pt-BR"
        return "en-US"
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.movie_menu, menu)

        val searchItem: MenuItem = menu!!.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        //searchView.imeOptions = EditorInfo.IME_ACTION_DONE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                getSearchMovie(p0!!)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                //movieAdapter!!.filter.filter(p0)
                return false
            }
        })

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                loadFragment()
                return true;
            }

            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                return true;
            }
        })

    }

    companion object {

        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}