package com.example.quanlykhohang1
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = DatabaseHelper(this)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val tvError = findViewById<TextView>(R.id.tvError)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                tvError.text = "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu"
                tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if (dbHelper.checkLogin(username, password)) {
                tvError.visibility = View.GONE
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                tvError.text = "Sai tên đăng nhập hoặc mật khẩu"
                tvError.visibility = View.VISIBLE
            }
        }
    }
}