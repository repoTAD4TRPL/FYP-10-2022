package com.del.ta_10.ui.pembeli

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.del.ta_10.R
import com.del.ta_10.ui.auth.LoginActivity
import com.del.ta_10.ui.dashboard.DashboardFragment

class PembeliActivity : AppCompatActivity(R.layout.activity_pembeli) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportActionBar?.hide()
            val bundle =
                bundleOf(DashboardFragment.ROLE to intent.getStringExtra(LoginActivity.ROLE))
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<DashboardFragment>(R.id.fragmen_pembeli, args = bundle)
            }
        }
    }
}