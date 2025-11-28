package com.deendayalproject.model.response

data class ModuleResponse(
    val wrappedList: List<Module>?,
    val responseCode: Int,
    val responseDesc: String,
    val responseMsg: String?,
    val errorsMap: Map<String, String>?
)

data class Module(
    val id: String? = "",
    val moduleCd: String? = "",
    val moduleName: String? = "",
    val forms: List<Form> = emptyList(),
    var isExpanded: Boolean = false
)

data class Form(
    val formUrl: String,
    val formName: String,
    val formCd: String
)
