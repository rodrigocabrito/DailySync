package com.example.dailysync

import android.os.Parcelable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName
import com.google.firebase.database.ValueEventListener
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import java.time.Instant

@IgnoreExtraProperties
@Parcelize
data class Sleep(
    @PropertyName("bedTimeHour") val bedTimeHour: Int,
    @PropertyName("bedTimeMin") val bedTimeMin: Int,
    @PropertyName("awakeTimeHour") val awakeTimeHour: Int,
    @PropertyName("awakeTimeMin") val awakeTimeMin: Int,
    @PropertyName("hourSlept") val hourSlept: Int,
    @PropertyName("minSlept") val minSlept: Int,
    @PropertyName("date") val date: Long
): Serializable, Parcelable {
    constructor() : this(
        0,
        0,
        0,
        0,
        0,
        0,
        0L
    )
}

class FirebaseSleepDataManager {

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun getSleeps(userId: String, onComplete: (List<Sleep>) -> Unit) {
        val sleepNodeReference = databaseReference.child("users").child(userId).child("Sleep")

        sleepNodeReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val sleepList = mutableListOf<Sleep>()

                for (sleepSnapshot in dataSnapshot.children) {
                    val sleep = sleepSnapshot.getValue(Sleep::class.java)
                    sleep?.let {
                        sleepList.add(it)
                    }
                }

                onComplete(sleepList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })
    }
}

