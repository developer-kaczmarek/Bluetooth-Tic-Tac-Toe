package io.github.kaczmarek.tictactoe.utils.components

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import io.github.kaczmarek.tictactoe.R

class MarkChooserButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val ivMark: ImageView
    private val tvMark: TextView

    var markType: MarkType = MarkType.CROSS
        set(value) {
            field = value
            if (field == MarkType.CROSS) {
                ivMark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_cross))
                tvMark.setText(R.string.common_host)

            } else {
                ivMark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_nought))
                tvMark.setText(R.string.common_guest)
            }
        }

    init {
        inflate(context, R.layout.layout_choose_button, this)
        ivMark = findViewById(R.id.ivMarkButton)
        tvMark = findViewById(R.id.tvMarkButton)
        context.withStyledAttributes(attrs, R.styleable.MarkChooserButton) {
            markType = MarkType.values()[getInt(
                R.styleable.MarkChooserButton_mcbMarkType,
                markType.ordinal
            )]
        }
    }
}

enum class MarkType {
    CROSS, NOUGHT
}

