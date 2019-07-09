package android.tairone.movies.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel

class PageViewModel : ViewModel() {

    private val _index = MutableLiveData<Int>()
    val text: LiveData<String> = Transformations.map(_index) {
        input -> getGenreByTab(input!!)
    }

    fun setIndex(index: Int) {
        _index.value = index
    }

    fun getGenreByTab(i: Int): String {
        val genre: String
        when (i) {
            0 // Acao
            -> genre = "28"
            1 // Drama
            -> genre = "18"
            2 // Fantasia
            -> genre = "14"
            3 // Ficcao cientifica
            -> genre = "878"
            else -> genre = "28"
        }
        return genre
    }
}