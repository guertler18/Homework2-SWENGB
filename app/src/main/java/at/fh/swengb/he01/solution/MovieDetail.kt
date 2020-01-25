package at.fh.swengb.he01.solution

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

class MovieDetail(val plot: String,
                  val genres: MutableList<String>,
                  val director: Person,
                  val actors: List<Person>,
                  id: String,
                  title: String,
                  posterImagePath: String,
                  backdropImagePath: String,
                  release: String,
                  reviews: MutableList<Review>): Movie(id,title,posterImagePath,backdropImagePath,release,reviews)  {
}