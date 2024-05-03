package com.example.cliente

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.google.android.material.textfield.TextInputEditText

class CustomDialogSignUp : AppCompatDialogFragment() {

    private var listener: ExampleDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.layout_customdialogsignup, null)

        val new_email = view.findViewById<TextInputEditText>(R.id.new_email)
        val new_password = view.findViewById<TextInputEditText>(R.id.new_password)

        return AlertDialog.Builder(requireActivity())
            .setView(view)
            .setTitle("Registrar cuenta")
            .setNegativeButton("Cancel") {_, _, ->}
            .setPositiveButton("Ok") {_, _, ->
                val email = new_email.text.toString()
                val password = new_password.text.toString()
                listener!!.applyTexts(email, password)
            }
            .create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = try {
            context as ExampleDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                context.toString() + "must implement ExampleDialogListener"
            )
        }
    }

    interface ExampleDialogListener {
        fun applyTexts(email: String?, password: String?)
    }
}