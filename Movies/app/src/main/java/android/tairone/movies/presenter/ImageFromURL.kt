package android.tairone.movies.presenter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.tairone.movies.R
import android.widget.ImageView
import java.io.IOException

class ImageFromURL (private val imageView: ImageView) : AsyncTask<String, Void, Bitmap>() {
    var bitmap: Bitmap? = null

    override fun doInBackground(vararg urls: String?): Bitmap? {
        val urlImage = urls[0]

        try {
            val inputStream = java.net.URL(urlImage).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        bitmap?.let {
            return it
        }
        return null
    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        if (result != null)
            imageView.setImageBitmap(bitmap)
        else
            imageView.setImageResource(R.drawable.ic_cloud_off_black)
    }
}