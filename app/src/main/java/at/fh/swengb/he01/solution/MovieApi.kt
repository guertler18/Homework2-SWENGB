package at.fh.swengb.he01.solution

import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

object MovieApi {
    const val accessToken = "f104a922-a9d7-484f-9ab5-f9d85e6840da"
    const val baseurl = "https://movies.bloder.xyz"

    val retrofit: Retrofit
    val retrofitService: MovieApiService
    init {
        val moshi = Moshi.Builder().build()
        retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(baseurl).build()
        retrofitService = retrofit.create(MovieApiService::class.java)
    }
}
interface MovieApiService {
    @GET("/${MovieApi.accessToken}/movies")
    fun movies(): Call<List<Movie>>
    @POST("/${MovieApi.accessToken}/movies/{id}/rate")
    fun reviewMovie(@Path("id") movieId: String, @Body rating: Review): Call<Unit>
    @GET("${MovieApi.baseurl}/${MovieApi.accessToken}/movies/{id}")
    fun movieById(@Query("id")  moviesId: String): Call<MovieDetail>
}