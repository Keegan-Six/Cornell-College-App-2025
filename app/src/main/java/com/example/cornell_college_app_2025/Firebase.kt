package com.example.cornell_college_app_2025

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

val database = Firebase.database
val studentRef = database.getReference("students")
// Write data
fun writeData() {
    val aliceSchedule = hashMapOf(
        "block1" to hashMapOf(
            "courseId" to "CSC401",
            "roomId" to "WEST301"
        ),
        "block2" to hashMapOf(
            "courseId" to "CSC402",
            "roomId" to "WEST302"
        ),
        "block3" to hashMapOf(
            "courseId" to "CSC403",
            "roomId" to "WEST303"
        ),
        "block4" to hashMapOf(
            "courseId" to "CSC404",
            "roomId" to "WEST304"
        ),
        "block5" to hashMapOf(
            "courseId" to "CSC405",
            "roomId" to "WEST305"
        ),
        "block6" to hashMapOf(
            "courseId" to "CSC406",
            "roomId" to "WEST306"
        ),
        "block7" to hashMapOf(
            "courseId" to "CSC407",
            "roomId" to "WEST307"
        ),
        "block8" to hashMapOf(
            "courseId" to "CSC408",
            "roomId" to "WEST308"
        ),
    )

    val aliceData = hashMapOf(
        "name" to "Alice Smith",
        "email" to "alice.smith@cornellcollege.edu",
        "password" to "alice123",
        "schedule" to aliceSchedule
    )
    studentRef.child("alice").setValue(aliceData)
}