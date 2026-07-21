package com.deendayalproject.model.response

data class EsopCandidateRes(
    val wrappedList: List<EsopCandidate>,
    val responseCode: Int,
    val responseDesc: String,
    val facilityId: Int,
    val resultImage: String?,
    val wrappedLista: Any?
) {

    data class EsopCandidate(
        val loginId: String,
        val gender: String?,
        val dob: String?,
        val mobile: String,
        val emailId: String,
        val categories: List<EsopCategory>,
        val userName: String
    )

    data class EsopCategory(
        val category: String
    )
}

//   "wrappedList": [
//        {
//            "loginId": "SUNITA",
//            "gender": null,
//            "dob": null,
//            "mobile": "8077637093",
//            "emailId": "kaushalgrameen@gmail.com",
//            "categories": [
//                {
//                    "category": "Operation"
//                }
//            ],
//            "userName": "SUNITA YADAV"
//        }
//    ],
//    "errorsMap": {},
//    "responseCode": 200,
//    "responseDesc": "OK",
//    "facilityId": 0,