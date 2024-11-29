package com.bangkit.letmecook.ui.usersAuth

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.letmecook.R
import com.bangkit.letmecook.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // Teks untuk bagian "Already Have Account? Sign In"
        val fullText = "Already Have Account? Sign In"
        val spannableString = SpannableString(fullText)

        // Menambahkan warna pada bagian "Sign In"
        val colorSpan = ForegroundColorSpan(ContextCompat.getColor(this, R.color.purple_500))
        spannableString.setSpan(colorSpan, 21, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Membuat teks "Sign In" menjadi dapat diklik
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Berpindah ke halaman SignInActivity ketika diklik
                val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                startActivity(intent)
            }

            // Menghilangkan garis bawah pada teks yang diklik
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }

        // Menambahkan span clickable pada teks "Sign In"
        spannableString.setSpan(clickableSpan, 21, 29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Menampilkan teks pada TextView
        binding.textViewSignUp.text = spannableString
        // Memungkinkan interaksi klik pada teks
        binding.textViewSignUp.movementMethod = android.text.method.LinkMovementMethod.getInstance()
        binding.textViewSignUp.highlightColor = android.graphics.Color.TRANSPARENT

        // Button untuk Sign Up
        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {
                    // Mendaftar user baru dengan email dan password
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, SignInActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
