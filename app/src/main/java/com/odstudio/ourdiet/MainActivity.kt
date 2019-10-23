package com.odstudio.ourdiet

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class MainActivity : AppCompatActivity() {
    //Global Declare
    private lateinit var mDrawerLayout: DrawerLayout
    private var finished: Boolean = false
    //Starting OnCreate Below
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        verifyAlreadyLoggedout()
        verifyUserIsLoggedIn()
        //Below Staring Drawer Navigation
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

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

                R.id.nav_personal -> {
                    val intent = Intent(this, PersonalActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_slideshow -> {
                    val intent = Intent(this, DataAddingActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_tools -> {
                    val intent = Intent(this, RecordingActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_share -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.nav_share)
                }
                R.id.nav_logout -> {
                    logout()
                }
            }
            true
        }
        //Below Starting Bottom Navigation
        val bottomNavView: BottomNavigationView = findViewById(R.id.navigation)
        bottomNavView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        //Update
        updateonHeader()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

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

    private fun logout() {
        finished = true
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)

            }
    }

    private fun verifyAlreadyLoggedout() {
        if (finished) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun verifyUserIsLoggedIn() {
        val logged = FirebaseAuth.getInstance().currentUser
        if (logged == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }


    private fun updateonHeader() {
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val headerView: View = navigationView.getHeaderView(0)
        val logged = FirebaseAuth.getInstance().currentUser
        if (logged != null) {
            var avatarImg =
                headerView.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.head_imageView)
            val filename = FirebaseAuth.getInstance().currentUser!!.uid + "_avatar"
            var avatarRef = FirebaseStorage.getInstance().getReference("/images/$filename")
                .downloadUrl.addOnSuccessListener {
                Glide.with(applicationContext)
                    .asBitmap()
                    .load(it)
                    .into(avatarImg)
            }.addOnFailureListener {
                //Do Nothing

            }
            //To use Glide
            avatarRef.isSuccessful
            //
            var uid = FirebaseAuth.getInstance().currentUser!!.uid
            var headNick: TextView = headerView.findViewById(R.id.head_name)
            var headEmail: TextView = headerView.findViewById(R.id.head_email)
            FirebaseFirestore.getInstance().collection("Users").whereEqualTo("uid", uid).get()
                .addOnSuccessListener {
                    it.forEach {
                        headNick.apply {
                            this.text = (it.get("nick")?.toString() ?: "Unknown Asshole")
                            if(this.text == "Unknown Asshole")
                            {
                                goPersonal()
                            }
                        }
                        headEmail.apply {
                            this.text = (it.get("email")?.toString() ?: "")
                        }
                    }
                }.addOnFailureListener {
                    //Do Nothing
                }
        }
    }
    private  fun goPersonal(){
        val intent = Intent(this, PersonalActivity::class.java)
        startActivity(intent)
    }
}