package br.edu.utfpr.cadastropessoascmp.ui.form.visualtransformation

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class MaskVisualTransformation(
    private val mask: String,
    private val maskChar: Char = '#'
) : VisualTransformation {

    private val maxDigits = mask.count { it == maskChar }

    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.take(maxDigits)
        val formatted = StringBuilder()
        val originalToTransformedOffsets = IntArray(text.text.length + 1)
        val transformedToOriginalOffsets = mutableListOf<Int>()

        var maskIndex = 0
        var digitIndex = 0

        while (digitIndex < trimmed.length && maskIndex < mask.length) {
            if (mask[maskIndex] == maskChar) {
                originalToTransformedOffsets[digitIndex] = formatted.length
                transformedToOriginalOffsets.add(digitIndex)
                formatted.append(trimmed[digitIndex])
                digitIndex++
            } else {
                transformedToOriginalOffsets.add(digitIndex)
                formatted.append(mask[maskIndex])
            }
            maskIndex++
        }

        val endTransformedOffset = formatted.length
        for (i in digitIndex..text.text.length) {
            originalToTransformedOffsets[i] = endTransformedOffset
        }
        transformedToOriginalOffsets.add(trimmed.length)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return originalToTransformedOffsets[
                        offset.coerceIn(0, originalToTransformedOffsets.lastIndex)
                ]
            }

            override fun transformedToOriginal(offset: Int): Int {
                return transformedToOriginalOffsets[
                        offset.coerceIn(0, transformedToOriginalOffsets.lastIndex)
                ]
            }
        }

        return TransformedText(
            text = AnnotatedString(formatted.toString()),
            offsetMapping = offsetMapping
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaskVisualTransformation) return false
        return mask == other.mask && maskChar == other.maskChar
    }

    override fun hashCode(): Int {
        var result = mask.hashCode()
        result = 31 * result + maskChar.hashCode()
        return result
    }

    companion object {
        const val CPF_MASK = "###.###.###-##"
        const val CEP_MASK = "#####-###"
        const val TELEFONE_FIXO_MASK = "(##) ####-####"
        const val TELEFONE_CELULAR_MASK = "(##) #####-####"

        val Cpf = MaskVisualTransformation(CPF_MASK)
        val Cep = MaskVisualTransformation(CEP_MASK)
    }
}

object TelefoneVisualTransformation : VisualTransformation {
    private val fixoTransformation = MaskVisualTransformation(
        MaskVisualTransformation.TELEFONE_FIXO_MASK
    )
    private val celularTransformation = MaskVisualTransformation(
        MaskVisualTransformation.TELEFONE_CELULAR_MASK
    )

    override fun filter(text: AnnotatedString): TransformedText {
        return if (text.text.length <= 10) {
            fixoTransformation.filter(text)
        } else {
            celularTransformation.filter(text)
        }
    }
}