package at.fh.swengb.he01.solution

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        val EXTRA_MOVIE_ID = "MOVIE_ID_EXTRA"
        val VIEW_DETAIL = 1
    }

    val movieAdapter = MovieAdapter {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra(EXTRA_MOVIE_ID, it.id)
        startActivityForResult(intent, VIEW_DETAIL)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        movie_recycler_view.layoutManager =  GridLayoutManager(this,3)
        movie_recycler_view.adapter = movieAdapter


        updateList()

        parseJson()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == VIEW_DETAIL) {
            updateList()

        }
    }

    private fun updateList() {
        MovieRepository.moviesList(
            success = {
                movieAdapter.updateList(it)
            },
            error = {
                Log.e("ERROR", "Something went very wrong!")
            }
        )
    }

    fun parseJson() {
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter<Movie>(Movie::class.java)
        val json = """
           
                {
                "id": "278",
                "title": "The Shawshank Redemption",
                "posterImagePath": "https://image.tmdb.org/t/p/w200/9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg",
                "backdropImagePath": "https://image.tmdb.org/t/p/w500/j9XKiZrVeViAixVRzCta7h1VU9W.jpg",
                "release": "1994-09-23",
                "reviews": [
                {
                "reviewValue": 4.5,
                "reviewText": "Great Movie"
                }
                ]
                }
                
        """
        val result = jsonAdapter.fromJson(json)
    }


}

