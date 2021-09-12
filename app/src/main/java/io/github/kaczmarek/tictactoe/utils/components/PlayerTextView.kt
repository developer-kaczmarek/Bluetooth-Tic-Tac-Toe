package io.github.kaczmarek.tictactoe.utils.components

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import io.github.kaczmarek.tictactoe.R
import io.github.kaczmarek.tictactoe.utils.dpToPx
import io.github.kaczmarek.tictactoe.utils.dpToPxInt


class PlayerTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    @ColorRes
    private var borderColor = R.color.blue_349

    private val shapeAppearanceModel = ShapeAppearanceModel()
        .toBuilder()
        .setAllCorners(CornerFamily.ROUNDED, context.dpToPx(CORNERS))
        .build()

    private val shapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)

    init {
        context.withStyledAttributes(attrs, R.styleable.PlayerTextView) {
            borderColor = getResourceId(
                R.styleable.PlayerTextView_ptvBorderColor,
                R.color.blue_349
            )
        }
        ellipsize = TextUtils.TruncateAt.END
        isSingleLine = true
        setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE)
        typeface = ResourcesCompat.getFont(context, R.font.nunito_black)

        setPadding(context.dpToPxInt(PADDINGS))

        setTextColor(ContextCompat.getColorStateList(context, R.color.grey_afb))
        shapeDrawable.fillColor = ContextCompat.getColorStateList(context, R.color.white_fff)

        background = shapeDrawable
    }

    fun setName(name: String?) {
        isVisible = !name.isNullOrEmpty() || name?.isNotBlank() == true
        text = name
    }

    fun changeVisibilityBorder(shouldVisibleBorder: Boolean) {
        Log.i("MY TAG", "changeVisibilityBorder = $shouldVisibleBorder")
        if (shouldVisibleBorder) {
            shapeDrawable.setStroke(
                context.dpToPx(STROKE_WIDTH),
                ContextCompat.getColor(
                    context,
                    borderColor
                )
            )
        } else {
            shapeDrawable.strokeWidth = 0F
        }
        background = shapeDrawable
    }

    companion object {
        private const val TEXT_SIZE = 14F
        private const val PADDINGS = 16F
        private const val CORNERS = 8F
        private const val STROKE_WIDTH = 1F
    }
}

