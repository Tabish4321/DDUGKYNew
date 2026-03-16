package com.deendayalproject.fragments.composeui.trainer

data class TrainerQuestion(
    val id: Int,
    val question: String
)

val trainerQuestionList = listOf(

    TrainerQuestion(
        1,
        "Attendance summary of trainers matching with the biometric attendance?"
    ),

    TrainerQuestion(
        2,
        "If attendance is <80%, has counselling been arranged for that particular trainer?"
    ),

    TrainerQuestion(
        3,
        "Does the trainer enter & exit the class at the time mentioned in the ACLP?"
    )
)
