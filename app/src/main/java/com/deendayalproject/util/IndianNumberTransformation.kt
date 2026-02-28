package com.deendayalproject.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.NumberFormat
import java.util.Locale
/**
 * Created by Rishi Porwal
 */


class IndianNumberTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {

        val input = text.text

        if (input.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        val formatted = try {
            val parsed = input.toLong()
            NumberFormat.getNumberInstance(Locale("en", "IN"))
                .format(parsed)
        } catch (e: Exception) {
            input
        }

        return TransformedText(
            AnnotatedString(formatted),
            OffsetMapping.Identity
        )
    }
}