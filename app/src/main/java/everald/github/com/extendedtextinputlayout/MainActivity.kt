package everald.github.com.extendedtextinputlayout

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.ToggleButton
import de.everald.extendedtextinputlayout.R

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val extendedTextInputLayout = findViewById(R.id.extended_text_input_layout) as ExtendedTextInputLayout
        extendedTextInputLayout.helperText = "Helper text was set for this View."
        extendedTextInputLayout.editText?.addTextChangedListener(ErrorTextWatcher(extendedTextInputLayout))

        val toggle = findViewById(R.id.toggle) as ToggleButton
        toggle.setOnCheckedChangeListener { _, isChecked ->
            extendedTextInputLayout.helperTextEnabled = isChecked
        }
    }

    class ErrorTextWatcher(val textInputLayout: TextInputLayout) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            if (s.length % 5 == 0) {
                textInputLayout.error = "Found an error I can't explain."
            } else {
                textInputLayout.error = null
                textInputLayout.isErrorEnabled = false
            }
        }
    }
}
