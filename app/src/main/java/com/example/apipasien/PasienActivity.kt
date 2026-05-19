package com.example.apipasien

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apipasien.adapter.PasienAdapter
import com.example.apipasien.network.RetrofitClient
import kotlinx.coroutines.launch

class PasienActivity : AppCompatActivity() {
    private lateinit var tvUser: TextView
    private lateinit var rvPasien: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: PasienAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pasien)
        tvUser = findViewById(R.id.tvUser)
        rvPasien = findViewById(R.id.rvPasien)
        progressBar = findViewById(R.id.progressBar)

        val prefs =
            getSharedPreferences(
                "auth",
                MODE_PRIVATE
            )

        val userName =
            prefs.getString("user", "")
        tvUser.text =
            "Selamat datang, $userName"
        adapter = PasienAdapter()
        rvPasien.layoutManager =
            LinearLayoutManager(this)
        rvPasien.adapter = adapter
        loadPasien()
    }

    private fun loadPasien() {
        lifecycleScope.launch {
            showLoading(true)
            try {
                val prefs =
                    getSharedPreferences(
                        "auth",
                        MODE_PRIVATE
                    )
                val token =
                    prefs.getString(
                        "token",
                        ""
                    ) ?: ""
                val response =
                    RetrofitClient
                        .apiService
                        .getPasien(
                            "Bearer $token"
                        )

                if (response.isSuccessful) {
                    val pasienList =
                        response.body()?.data
                            ?: emptyList()
                    adapter.setData(pasienList)

                } else {
                    Toast.makeText(
                        this@PasienActivity,
                        "Gagal mengambil data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@PasienActivity,
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
    }
}