package com.example.apipasien

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.apipasien.model.LoginRequest
import com.example.apipasien.network.RetrofitClient
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        progressBar = findViewById(R.id.progressBar)
        btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {

        val email =
            etEmail.text.toString().trim()
        val password =
            etPassword.text.toString().trim()
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                this,
                "Input tidak boleh kosong",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        lifecycleScope.launch {
            showLoading(true)
            try {
                val request =
                    LoginRequest(email, password)
                val response =
                    RetrofitClient
                        .apiService
                        .login(request)

                if (response.isSuccessful) {
                    val token =
                        response.body()?.data?.token
                            ?: ""
                    val userName =
                        response.body()?.data?.user?.name
                            ?: ""
                    if (token.isNotEmpty()) {
                        val prefs =
                            getSharedPreferences(
                                "auth",
                                MODE_PRIVATE
                            )

                        prefs.edit()
                            .putString("token", token)
                            .putString("user", userName)
                            .apply()

                        startActivity(
                            Intent(
                                this@LoginActivity,
                                PasienActivity::class.java
                            )
                        )
                        finish()
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Login gagal",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@LoginActivity,
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                showLoading(false)
            }
        }
    }

    private fun showLoading(
        isLoading: Boolean
    ) {
        progressBar.visibility =
            if (isLoading)
                View.VISIBLE
            else
                View.GONE
        btnLogin.isEnabled = !isLoading
    }
}