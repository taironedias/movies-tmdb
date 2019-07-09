package android.tairone.movies

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.tairone.movies.presenter.ImageFromURL
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_overview.*

class OverviewActivity : AppCompatActivity() {

    private var title: String? = null
    private var overview: String? = null
    private var backdrop_path: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        val labelOverview: TextView = findViewById(R.id.labelOverview)
        val imageMovieOverview: ImageView = findViewById(R.id.imageMovieOverview)

        if (intent.extras.containsKey("title")) {
            title = intent.getStringExtra("title")
        }
        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        if (intent.extras.containsKey("overview")) {
            overview = intent.getStringExtra("overview")
            labelOverview.text = overview
        }

        if (intent.extras.containsKey("backdrop_path")) {
            backdrop_path = intent.getStringExtra("backdrop_path")
            //val urlImage = "https://image.tmdb.org/t/p/w500/" + backdrop_path
            val urlImage = "https://image.tmdb.org/t/p/w1280/" + backdrop_path
            ImageFromURL(imageMovieOverview).execute(urlImage)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}
