package com.example.socialnetworkmobile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.socialnetworkmobile.databinding.ActivityMainBinding
import com.example.socialnetworkmobile.model.User
import com.google.android.material.navigation.NavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private val mainActivityViewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences: SharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val bottomNavView: BottomNavigationView = binding.bottomNavView
        val drawerNavView: NavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_conversations, R.id.navigation_notifications
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavView.setupWithNavController(navController)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        drawerNavView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_settings -> {
                    navController.navigate(R.id.nav_settings)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_logout -> {
                    navController.navigate(R.id.nav_logout)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_friends-> {
                    navController.navigate(R.id.nav_friends)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
        }
        bottomNavView.setOnItemSelectedListener  { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home)
                    true
                }
                R.id.navigation_conversations -> {
                    navController.navigate(R.id.navigation_conversations)
                    true
                }
                R.id.navigation_notifications -> {
                    navController.navigate(R.id.navigation_notifications)
                    true
                }
                else -> false
            }
        }



        val headerView = drawerNavView.getHeaderView(0)
        val imageView = headerView.findViewById<ImageView>(R.id.imageView)

        Glide.with(this)
            .load(R.drawable.background)
            .apply(RequestOptions.circleCropTransform())
            .into(imageView)

        username?.let {
            mainActivityViewModel.getUserId(it)
        }

        mainActivityViewModel.userId.observe(this, Observer { userId ->
            userId?.let {
                mainActivityViewModel.getUserDetails(it)
            }
        })

        mainActivityViewModel.userDetails.observe(this, Observer { userDetails ->
            userDetails?.let {
                updateDrawerHeader(it)
                saveUserDetailsToPreferences(it)
            }
        })

        logAllSharedPreferences(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    private fun updateDrawerHeader(user: User) {
        val headerView = findViewById<NavigationView>(R.id.nav_view).getHeaderView(0)
        val usernameTextView = headerView.findViewById<TextView>(R.id.drawer_username)
        val emailTextView = headerView.findViewById<TextView>(R.id.drawer_email)
        usernameTextView.text = user.username
        emailTextView.text = user.email
    }
    private fun saveUserDetailsToPreferences(user: User) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putLong("user_id", user.id)
        editor.putString("email", user.email)
        editor.apply()
    }

    private fun logAllSharedPreferences(context: Context) {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val allEntries: Map<String, *> = sharedPreferences.all

        for ((key, value) in allEntries) {
            Log.d("SharedPreferences", "$key: $value")
        }
    }
}