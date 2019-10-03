package com.odstudio.ourdiet

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mDrawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_menu_white_24dp)
        }
        mDrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // set item as selected to persist highlight
            menuItem.isChecked = true
            // close drawer when item is tapped
            mDrawerLayout.closeDrawers()

            // Handle navigation view item clicks here.
            when (menuItem.itemId) {

                R.id.nav_gallery -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.nav_gallery)
                }
                R.id.nav_slideshow -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.nav_slideshow)
                }
                R.id.nav_tools -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.nav_tools)
                }
                R.id.nav_share -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.nav_share)
                }
                R.id.nav_send -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.nav_send)
                }
            }
            true
        }
        val bottomNavView: BottomNavigationView = findViewById(R.id.navigation)
        bottomNavView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

  //  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    //    Inflate the menu; this adds items to the action bar if it is present.
      //  menuInflater.inflate(R.menu.main, menu)
        //return true
   // }

    //appbar - toolbar button click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout.openDrawer(GravityCompat.START)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.nav_home)
                }
                R.id.navigation_groups -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.nav_groups)
                }
                R.id.navigation_friends -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.nav_friends)
                }
            }
            false
        }
}
