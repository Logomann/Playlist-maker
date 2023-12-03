package com.practicum.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val EDIT_FIELD = "EDIT_FIELD"
    }

    private var editText = ""
    private lateinit var editField: EditText
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_FIELD, editText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editText = savedInstanceState.getString(EDIT_FIELD, "")
        editField.setText(editText)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backBtn = findViewById<ImageButton>(R.id.search_arrow_back_btn)
        backBtn.setOnClickListener {
            this.finish()
        }
        editField = findViewById(R.id.search_edit_field)
        val clearBtn = findViewById<ImageButton>(R.id.search_clear_btn)
        clearBtn.setOnClickListener {
            editField.text.clear()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(editField.windowToken, 0)
        }
        val editTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearBtn.visibility = View.VISIBLE
                editText = editField.text.toString()
                if (editField.text.isEmpty()) {
                    clearBtn.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        editField.addTextChangedListener(editTextWatcher)
    }

}