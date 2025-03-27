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
        )
    )
    val aliceData = hashMapOf(
        "name" to "Alice Smith",
        "email" to "asmith25@cornellcollege.edu",
        "username" to "asmith25",
        "password" to "alice123",
        "schedule" to aliceSchedule
    )
    val keeganSchedule = hashMapOf(
        "Block 1" to hashMapOf(
            "course" to "CSC401",
            "room" to "WEST301"
        ),
        "Block 2" to hashMapOf(
            "course" to "CSC402",
            "room" to "WEST302"
        ),
        "Block 3" to hashMapOf(
            "course" to "CSC403",
            "room" to "WEST303"
        ),
        "Block 4" to hashMapOf(
            "course" to "CSC404",
            "room" to "WEST304"
        ),
        "Block 5" to hashMapOf(
            "course" to "CSC405",
            "room" to "WEST305"
        ),
        "Block 6" to hashMapOf(
            "course" to "CSC406",
            "room" to "WEST306"
        ),
        "Block 7" to hashMapOf(
            "course" to "CSC407",
            "room" to "WEST307"
        ),
        "Block 8" to hashMapOf(
            "course" to "CSC408",
            "room" to "WEST308"
        )
    )
    val keeganData = hashMapOf(
        "name" to "Keegan",
        "email" to "ksmith25@cornellcollege.edu",
        "username" to "ksmith25",
        "password" to "keegan123",
        "schedule" to keeganSchedule
    )
    studentRef.child("alice").setValue(aliceData)
    studentRef.child("keegan").setValue(keeganData)

}