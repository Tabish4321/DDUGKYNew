package com.deendayalproject.model.response

data class LivingAreaDelete(    val wrappedList: List<Any>,
                                val errorsMap: Map<String, Any>,
                                val responseCode: Int,
                                val responseDesc: String)


