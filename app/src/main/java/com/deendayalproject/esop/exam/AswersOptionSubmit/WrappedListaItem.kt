package com.example.esop.AswersOptionSubmit

data class WrappedListaItem(

    val result: Int,
    val totalQuestions: Int,
//    val numberofAttempt: Int,
    val wrongAns: Int,
    val scoredPercentage: Int,
    val passingPercentage: Int,
    val notattempteQuestion: Int,
    val correctAns: Int
)