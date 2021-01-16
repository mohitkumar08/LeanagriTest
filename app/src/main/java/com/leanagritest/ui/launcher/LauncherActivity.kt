package com.leanagritest.ui.launcher

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.leanagritest.R
import com.leanagritest.ui.movielist.MoviesListActivity

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        Handler(Looper.getMainLooper()).postDelayed({
            MoviesListActivity.startMoviesListActivity(this)
            finish()
        }, 1500)
    }
}