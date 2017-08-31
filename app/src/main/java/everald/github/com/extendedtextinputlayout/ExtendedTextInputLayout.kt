package everald.github.com.extendedtextinputlayout

import android.content.Context
import android.content.res.ColorStateList
import android.support.design.widget.TextInputLayout
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v4.widget.TextViewCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.widget.EditText
import android.widget.TextView
import de.everald.extendedtextinputlayout.R

open class ExtendedTextInputLayout : TextInputLayout {

    private var _errorTextEnabled = false
    private var _helperText: CharSequence = ""
    private var _helperTextAnimator: ViewPropertyAnimator? = null
    private var _helperTextAppearance = R.style.HelperTextAppearance
    private var _helperTextColor: ColorStateList? = null
    private var _helperTextEnabled = false
    private var _helperTextView = TextView(context).apply {
        setTextAppearance(_helperTextAppearance)
        this@ExtendedTextInputLayout.addView(this)
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var helperText: CharSequence
        get() = _helperText
        set(value) {
            if (_helperText == value) {
                return
            }
            _helperText = value
            _helperTextEnabled = value.isNotBlank()
            setHelperTextOnHelperView(value)
        }

    var helperTextAppearance: Int
        get() = _helperTextAppearance
        set(value) {
            _helperTextAppearance = value
            TextViewCompat.setTextAppearance(_helperTextView, value)
        }

    var helperTextColor: ColorStateList?
        get() = _helperTextColor
        set(value) {
            _helperTextColor = value
            _helperTextView.setTextColor(value)
        }

    var helperTextEnabled: Boolean
        get() = _helperTextEnabled
        set(value) {
            if (_helperTextEnabled == value) return
            _helperTextEnabled = value
            switchHelperText()
        }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        super.addView(child, params)
        if (child is EditText) {
            _helperTextView.setPaddingRelative(child.paddingStart, 0, child.paddingEnd, child.paddingBottom)
        }
    }

    override fun setErrorEnabled(enabled: Boolean) {
        if (_errorTextEnabled == enabled) return
        _errorTextEnabled = enabled

        if (_errorTextEnabled && helperTextEnabled) {
            switchHelperText()
        }

        super.setErrorEnabled(enabled)

        if (!_errorTextEnabled) {
            switchHelperText()
        }
    }

    private fun setHelperTextOnHelperView(text: CharSequence?) {
        _helperTextAnimator?.cancel()
        if (text.isNullOrBlank()) {
            _helperTextView.visibility = View.GONE
            if (_helperTextView.text == text) return
            _helperTextAnimator = _helperTextView.animate()
                    .setInterpolator(Interpolator)
                    .alpha(0f)
                    .setDuration(200)
                    .withEndAction {
                        _helperTextView.text = null
                    }

        } else {
            _helperTextView.visibility = View.VISIBLE
            _helperTextView.text = text
            _helperTextAnimator = _helperTextView.animate()
                    .setInterpolator(Interpolator)
                    .alpha(1f)
                    .setDuration(200)
        }
        _helperTextAnimator?.start()
    }

    private fun switchHelperText() {
        if (_errorTextEnabled || !_helperTextEnabled) { // hide helper text
            setHelperTextOnHelperView(null)
        } else if (!_errorTextEnabled && _helperTextEnabled) { // if there is a helper text, show it
            if (helperText.isNotBlank()) {
                setHelperTextOnHelperView(helperText)
            } else {
                setHelperTextOnHelperView(null)
            }
        }
    }

    companion object {
        val Interpolator = FastOutSlowInInterpolator()
    }
}