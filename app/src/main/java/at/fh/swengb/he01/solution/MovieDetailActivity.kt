package at.fh.swengb.he01.solution

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.item_movie.view.*
import java.math.BigDecimal
import java.math.RoundingMode

class MovieDetailActivity : AppCompatActivity() {

    companion object {
        val REVIEW_MOVIE_REQUEST = 2
        val EXTRA_MOVIE_ID = "MOVIE_ID_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        val movieId = intent.getStringExtra(MainActivity.EXTRA_MOVIE_ID)
        updateUi(movieId)

        parseJson()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REVIEW_MOVIE_REQUEST && resultCode == Activity.RESULT_OK){
            val movieId = intent.getStringExtra(MainActivity.EXTRA_MOVIE_ID)
            updateUi(movieId)
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
        }
    }



    private fun updateUi(movieId: String?) {
        if (movieId == null) {
            handleMovieNotFound()
            return
        }

        else {
             MovieRepository.movieById(movieId, success = {
                val movie = it
                 Glide.with(this).load(it.backdropImagePath).into(movie_backdrop_image)
                 Glide.with(this).load(it.posterImagePath).into(movie_detailposter_image)
                 movie_detail_header.text = movie.title
                 movie_detail_director.text = movie.director.name
                 movie_detail_actors.text = movie.actors.joinToString(", ") { it.name }
                 movie_detail_genre.text = movie.genres.joinToString()
                 movie_detail_rating_bar.rating = movie.reviewAverage().toFloat()
                 movie_detail_rating_value.text = BigDecimal(movie.reviewAverage()).setScale(1, RoundingMode.HALF_EVEN).toString()
                 movie_detail_review_count.text = movie.reviews.count().toString()
                 movie_detail_release.text = movie.release
                 movie_detail_plot.text = movie.plot
             },error = {
                Log.e("ERROR",it)
            })





            open_review.setOnClickListener {
                val ratingIntent = Intent(this, MovieReviewActivity::class.java)
                val extra = intent.getStringExtra(MainActivity.EXTRA_MOVIE_ID)
                ratingIntent.putExtra(EXTRA_MOVIE_ID, movieId)
                startActivityForResult(ratingIntent, REVIEW_MOVIE_REQUEST)
            }
        }
    }

    private fun handleMovieNotFound() {
        Toast.makeText(this, "Movie could not be found", Toast.LENGTH_LONG).show()
        finish()
    }

    fun parseJson() {
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter<MovieDetail>(MovieDetail::class.java)
        val json = """
            {
                "id": "278",
                "title": "The Shawshank Redemption",
                "posterImagePath": "https://image.tmdb.org/t/p/w200/9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg",
                "backdropImagePath": "https://image.tmdb.org/t/p/w500/j9XKiZrVeViAixVRzCta7h1VU9W.jpg",
                "release": "1994-09-23",
                "plot": "Framed in the 1940s for the double murder of his wife and her lover...",
                "actors": [
                {
                "name": "Tim Robbins",
                "profileImagePath": "https://image.tmdb.org/t/p/w200/9DujxnBMVkizaeIyM0eXPMfXxR.jpg"
                },
                {
                "name": "Morgan Freeman",
                "profileImagePath": "https://image.tmdb.org/t/p/w200/oGJQhOpT8S1M56tvSsbEBePV5O1.jpg"
                }
                ],
                "director": {
                "name": "Frank Darabont",
                "profileImagePath": "https://image.tmdb.org/t/p/w200/9KVvZtDyy8DXacw2TTsjC9VLxQi.jpg"
                },
                "genres": [
                "Drama",
                "Crime"
                ],
                "reviews": [
                {
                "reviewValue": 4.5,
                "reviewText": ""
                }
                ]
            }
        """
        val result = jsonAdapter.fromJson(json)
    }
}
