package com.hardrelice.playmylife.activity

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.hardrelice.playmylife.R
import com.hardrelice.playmylife.adapter.CurrencyAdapter
import com.hardrelice.playmylife.datatype.GoodItem
import com.hardrelice.playmylife.ui.shop.ShopFragment
import com.hardrelice.playmylife.ui.task.TaskFragment
import com.hardrelice.playmylife.utils.*
import kotlinx.android.synthetic.main.activity_add_good.*
import kotlinx.android.synthetic.main.fragment_shop.*
import kotlinx.android.synthetic.main.fragment_task.*
import kotlinx.android.synthetic.main.select_currency_dialog.*

class AddGood : AppCompatActivity() {
    data class TempCurrency(var currency: String = "点数")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_good)
        val tempCurrency = TempCurrency()
        currency_icon.onSingleClick({
            val builder = AlertDialog.Builder(this, R.style.RoundDialogTheme)
            builder.setView(R.layout.select_currency_dialog)
            val dialog = builder.create()
            dialog.show()
            dialog.currency_list.layoutManager = LinearLayoutManager(this)
            dialog.currency_list.adapter = CurrencyAdapter(tempCurrency, currency_icon)
        })

        add_good_tool_bar_cancel_button.onSingleClick({
            this.finish()
        })

        add_good_tool_bar_ok_button.onSingleClick({
            val item = GoodItem()
            item["goodId"] = GoodManager.generateId()
            item["goodTitle"] = new_good_name_input.text.toString()
            item["goodCurrency"] = tempCurrency.currency
            val priceString = new_good_price_input.text.toString()
            item["goodPrice"] = if (priceString == "") 0 else priceString.toInt()
            if (item.getGoodTitle() != "") {
                this.window.decorView.closeKeyboard()
                this.window.decorView.isEnabled = false
                GoodManager.saveGood(item)
                (GlobalActivity.fragments[1] as ShopFragment).good_list.adapter!!.notifyItemInserted(
                    GoodManager.goods.size - 2
                )
                handler.postDelayed({
                    this.finish()
                }, 200)
            }
        })
    }


}