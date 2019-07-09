package android.tairone.movies.presenter

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun api_interface(): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

}