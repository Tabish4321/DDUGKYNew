package com.deendayalproject.model.request

data class saveTrainerClassObservationInspectionReq(

    val appVersion: String,
    val inspectionId: Int,
    val subject: String,

    val trainerFacingClassQid: Int,
    val trainerFacingClass: String,
    val trainerFacingClassRemark: String,

    val trainerAddressingCandidatesQid: Int,
    val trainerAddressingCandidates: String,
    val trainerAddressingCandidatesRemark: String,

    val sessionAsPerLessonPlanQid: Int,
    val sessionAsPerLessonPlan: String,
    val sessionAsPerLessonPlanRemark: String,

    val maintainsClassDisciplineQid: Int,
    val maintainsClassDiscipline: String,
    val maintainsClassDisciplineRemark: String,

    val trainerConfidentCommunicationQid: Int,
    val trainerConfidentCommunication: String,
    val trainerConfidentCommunicationRemark: String,

    val trainerWithoutMaterialRefQid: Int,
    val trainerWithoutMaterialRef: String,
    val trainerWithoutMaterialRefRemark: String,

    val usesAudiovisualAidsQid: Int,
    val usesAudiovisualAids: String,
    val usesAudiovisualAidsRemark: String,

    val sessionInteractiveQid: Int,
    val sessionInteractive: String,
    val sessionInteractiveRemark: String,

    val encouragesCandidateQuestionsQid: Int,
    val encouragesCandidateQuestions: String,
    val encouragesCandidateQuestionsRemark: String,

    val answersQueriesClearlyQid: Int,
    val answersQueriesClearly: String,
    val answersQueriesClearlyRemark: String,

    val usesExamplesMethodsQid: Int,
    val usesExamplesMethods: String,
    val usesExamplesMethodsRemark: String,

    val internalAssessmentOnScheduleQid: Int,
    val internalAssessmentOnSchedule: String,
    val internalAssessmentOnScheduleRemark: String,

    val evaluatesPerformanceFeedbackQid: Int,
    val evaluatesPerformanceFeedback: String,
    val evaluatesPerformanceFeedbackRemark: String,

    val guidesJobReadinessQid: Int,
    val guidesJobReadiness: String,
    val guidesJobReadinessRemark: String
)
