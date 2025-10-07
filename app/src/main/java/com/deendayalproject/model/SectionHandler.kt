package com.deendayalproject.model

data class SectionHandler(
    val sectionCount: Int,
    val action: () -> Unit
)
