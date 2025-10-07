package com.deendayalproject.model.response

data class BlockResponse(val wrappedList: List<BlockModel>?,
                         val responseCode: Int,
                         val responseDesc: String)
