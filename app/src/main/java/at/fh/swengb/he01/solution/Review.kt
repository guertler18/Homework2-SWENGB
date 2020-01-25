package at.fh.swengb.he01.solution

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

open class Review (val reviewValue: Double, val reviewText: String){
}