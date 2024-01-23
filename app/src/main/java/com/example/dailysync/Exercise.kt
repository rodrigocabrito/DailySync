package com.example.dailysync

import android.os.Parcelable
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName
import com.google.firebase.database.ValueEventListener
import java.io.Serializable
import java.time.Instant
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class Exercise(
    @PropertyName("name") val name: String,
    @PropertyName("description") val description: String = "",
    @PropertyName("time") val time: Long,
    @PropertyName("averagePace") val averagePace: Float,
    @PropertyName("distance") val distance: Float,
    @PropertyName("image") val image: Boolean,
    @PropertyName("date") val date: Long,  // Use Long to represent date as milliseconds
    @PropertyName("type") var type: String = ""
) : Serializable, Parcelable {
    constructor() : this(
        "",
        "",
        0L,
        0f,
        0f,
        false,
        0L,
        ""
    )
}

class FirebaseExerciseDataManager {

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun getExercises(userId: String, category: Int, onComplete: (List<Exercise>) -> Unit) {
        val categoryName = when (category) {
            1 -> "Run"
            2 -> "Walk"
            else -> "Cycle"
        }
        val exerciseNodeReference = databaseReference.child("users").child(userId).child(categoryName)

        exerciseNodeReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val exerciseList = mutableListOf<Exercise>()

                //Log.e("Depois", "Dentro do onDataChange do getExercises")
                for (exerciseSnapshot in dataSnapshot.children) {
                    val exercise = exerciseSnapshot.getValue(Exercise::class.java)
                    exercise?.let {
                        exerciseList.add(it)
                    }
                }

                onComplete(exerciseList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })
    }
}
