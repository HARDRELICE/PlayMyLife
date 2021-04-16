package com.hardrelice.playmylife.ui.shop

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hardrelice.playmylife.R
import com.hardrelice.playmylife.activity.AddGood
import com.hardrelice.playmylife.adapter.GoodAdapter
import com.hardrelice.playmylife.utils.ApplicationContext
import com.hardrelice.playmylife.utils.onSingleClick
import kotlinx.android.synthetic.main.fragment_shop.view.*

class ShopFragment : Fragment() {

    private lateinit var shopViewModel: ShopViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        shopViewModel = ViewModelProvider(this).get(ShopViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_shop, container, false)

        // initial recycler view
        val adapter = GoodAdapter()
        root.good_list.layoutManager = LinearLayoutManager(this.activity)
        adapter.setFooterView(
            LayoutInflater.from(this.context).inflate(
                R.layout.item_footer,
                root.good_list,
                false
            )
        )
        root.good_list.adapter = adapter
        root.good_list.setHasFixedSize(false)
        // set listener on add task button
        root.add_good_button.onSingleClick({
            val intent = Intent(ApplicationContext, AddGood::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            ApplicationContext.startActivity(intent)
        })
        return root
    }
}