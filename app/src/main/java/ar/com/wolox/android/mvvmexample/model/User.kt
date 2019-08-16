package ar.com.wolox.android.mvvmexample.model

import java.io.Serializable

data class User(val id: Int, val username: String, val email: String, val password: String,
                val picture: String, val cover: String, val description: String, val location: String,
                val name: String, val phone: String): Serializable