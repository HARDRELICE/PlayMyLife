package com.hardrelice.playmylife

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.hardrelice.playmylife.activity.AddTask
import com.hardrelice.playmylife.activity.ExecuteTask
import com.hardrelice.playmylife.adapter.MainAdapter
import com.hardrelice.playmylife.ui.shop.ShopFragment
import com.hardrelice.playmylife.ui.task.TaskFragment
import com.hardrelice.playmylife.ui.me.MeFragment
import com.hardrelice.playmylife.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_task.*

class MainActivity : AppCompatActivity() {


    val fragments = ArrayList<Fragment>()
    val navMenuIds = ArrayList<Int>()

    private fun init(){
        GlobalActivity = this
        ApplicationContext = applicationContext
        handler = UIHandler(this)
        SettingManager.init()
        CurrencyManager.init()
        TaskManager.init()
        GoodManager.init()
        IconManager.init()
//        MigrateHelper.init()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val x = findViewById<ListView>(R.id.task_reward_list)

        // initialize
        init()

        // request permission no need here
        // DynamicPermission.get(this, 1)

        // initial layouts
        fragments.add(TaskFragment())
        fragments.add(ShopFragment())
        fragments.add(MeFragment())

        // set viewPager adapter
        view_pager.adapter = MainAdapter(supportFragmentManager, fragments)
        view_pager.offscreenPageLimit = fragments.size

        // bind id with navMenuButton
        for (item in nav_view.menu) {
            navMenuIds.add(item.itemId)
        }

        // switch navMenuButton when page changed
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                nav_view.selectedItemId = nav_view.menu[position].itemId
                val frg = fragments[position]
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })

        // switch page when navMenuButton is select
        nav_view.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                else -> {
                    view_pager.currentItem = navMenuIds.indexOf(item.itemId)
                    return@OnNavigationItemSelectedListener true
                }
            }
        })
    }
}