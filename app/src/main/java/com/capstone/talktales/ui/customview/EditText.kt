package com.capstone.talktales.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.ContextThemeWrapper
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.capstone.talktales.R

class EditText : AppCompatEditText, View.OnTouchListener {

    private lateinit var actionButtonIcon: Drawable
    private lateinit var bgEditText: Drawable
    private lateinit var bgEditTextFocus: Drawable
    private lateinit var bgEditTextError: Drawable
    private lateinit var eye: Drawable
    private lateinit var eyeOff: Drawable
    private lateinit var close: Drawable
    private lateinit var type: Typeface
    private var textColor: Int = 0
    private var highlight: Int = 0
    private var hintText: Int = 0


    constructor(context: Context) : super(ContextThemeWrapper(context, R.style.EditTextStyle)) {
        init()
    }

    constructor(context: Context, attributes: AttributeSet) : super(
        ContextThemeWrapper(
            context, R.style.EditTextStyle
        ), attributes
    ) {
        init()
    }

    constructor(context: Context, attributes: AttributeSet, defStyleAttr: Int) : super(
        ContextThemeWrapper(context, R.style.EditTextStyle), attributes, defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = bgEditText
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            textCursorDrawable = null
        }
        textSize = 16f
        highlightColor = highlight
        setHintTextColor(hintText)
        typeface = type
        setTextColor(textColor)
        when (inputType) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD, InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD -> {
                showActionIcon()
            }

            else -> {}
        }
        when {
            !isFocused && error.isNullOrEmpty() -> background = bgEditText
            isFocused && error.isNullOrEmpty() -> background = bgEditTextFocus
            !error.isNullOrEmpty() -> background = bgEditTextError
        }
        alpha = if (isEnabled) 1f else 0.6f

    }

    private fun init() {
        textColor = ContextCompat.getColor(context, R.color.black)
        highlight = ContextCompat.getColor(context, R.color.orange_200)
        hintText = ContextCompat.getColor(context, R.color.grey_hint)
        bgEditText = ContextCompat.getDrawable(context, R.drawable.bg_edit_text) as Drawable
        bgEditTextFocus =
            ContextCompat.getDrawable(context, R.drawable.bg_editext_focused) as Drawable
        bgEditTextError =
            ContextCompat.getDrawable(context, R.drawable.bg_edittext_error) as Drawable
        eye = ContextCompat.getDrawable(context, R.drawable.eye) as Drawable
        eyeOff = ContextCompat.getDrawable(context, R.drawable.eye_off) as Drawable
        close = ContextCompat.getDrawable(context, R.drawable.close) as Drawable
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            type = resources.getFont(R.font.poppins_regular)
        }
        setActionIcon()

        setOnTouchListener(this)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                when (inputType) {
                    InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS or InputType.TYPE_CLASS_TEXT -> {
                        if (!s.isValidEmail()) {
                            setError(context.getString(R.string.emailFormatError), null)
                        } else {
                            setError(null, null)
                        }
                        if (s.toString().isNotEmpty()) showActionIcon() else hideActionIcon()
                    }

                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD, InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD -> {
                        if (!s.isValidPassword()) {
                            setError(
                                context.getString(R.string.passwordLengthError),
                                null
                            )
                        } else {
                            setError(null, null)
                        }
                    }

                    else -> {
                        if (s.isEmpty()) {
                            setError(context.getString(R.string.EmptyError), null)
                            hideActionIcon()
                        } else {
                            setError(null, null)
                            showActionIcon()
                        }
                    }
                }

            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    fun CharSequence.isValidEmail() =
        toString().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    fun CharSequence.isValidPassword() = toString().isNotEmpty() && this.length > 7


    private fun showActionIcon() {
        setButtonDrawables(endOfTheText = actionButtonIcon)
    }

    private fun hideActionIcon() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText, topOfTheText, endOfTheText, bottomOfTheText
        )
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] == null) return false
        val clearButtonStart: Float
        val clearButtonEnd: Float
        var isClearButtonClicked = false
        if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
            clearButtonEnd = (actionButtonIcon.intrinsicWidth + paddingStart).toFloat()
            when {
                event.x < clearButtonEnd -> isClearButtonClicked = true
            }
        } else {
            clearButtonStart = (width - paddingEnd - actionButtonIcon.intrinsicWidth).toFloat()
            when {
                event.x > clearButtonStart -> isClearButtonClicked = true
            }
        }
        if (!isClearButtonClicked) return false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                setActionIcon()
                showActionIcon()
                return true
            }

            MotionEvent.ACTION_UP -> {
                when (inputType) {
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD -> {
                        hideActionIcon()
                        inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        setActionIcon()
                        showActionIcon()
                    }

                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD -> {
                        hideActionIcon()
                        inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        setActionIcon()
                        showActionIcon()
                    }

                    else -> {
                        setActionIcon()
                        text?.clear()
                        hideActionIcon()
                    }
                }
                return true
            }

            else -> return false
        }
    }

    private fun setActionIcon() {
        actionButtonIcon = when (inputType) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD -> eye
            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD -> eyeOff
            else -> close
        }
    }

}