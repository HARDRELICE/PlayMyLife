package com.hardrelice.playmylife.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hardrelice.playmylife.R
import com.hardrelice.playmylife.utils.*
import kotlinx.android.synthetic.main.good_item.view.*

class GoodAdapter : RecyclerView.Adapter<GoodAdapter.GoodViewHolder>() {
    companion object {
        const val NORMAL_VIEW = 0
        const val HEADER_VIEW = 1
        const val FOOTER_VIEW = 2
        var useHeaderView = false
        var useFooterView = true
        var headerView: View? = null
        var footerView: View? = null
    }

    fun setHeaderView(view: View) {
        headerView = view
    }

    fun setFooterView(view: View) {
        footerView = view
    }

    class GoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var goodTitle: TextView? = null
        var deleteButton: ImageButton? = null
        var currencyIcon: ImageView? = null
        var priceText: TextView? = null
        var editButton: ImageButton? = null
        var buyButton: ImageButton? = null
        var viewType: Int = NORMAL_VIEW

        init {
            if (itemView == headerView) {
                viewType = HEADER_VIEW
            } else if (itemView == footerView) {
                viewType = FOOTER_VIEW
            } else {
                goodTitle = itemView.good_title
                currencyIcon = itemView.currency_icon
                priceText = itemView.good_price
                deleteButton = itemView.delete_good
//                editButton = itemView.edit_good
                buyButton = itemView.buy_good
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodViewHolder {
        return if (viewType == HEADER_VIEW && headerView != null) {
            GoodViewHolder(headerView!!)
        } else if (viewType == FOOTER_VIEW && footerView != null) {
            GoodViewHolder(footerView!!)
        } else {
            GoodViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.good_item,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            itemCount-1 -> FOOTER_VIEW
            else -> NORMAL_VIEW
        }

    }

    override fun onBindViewHolder(holder: GoodViewHolder, position: Int) {
        when (holder.viewType) {
            NORMAL_VIEW -> {
                val item = GoodManager.goods[position]

                holder.goodTitle!!.text = item.getGoodTitle()

                if (!item.canBuy()) {
                    holder.goodTitle!!.setTextColor(ColorStateList.valueOf(R.color.grey.getColor()))
                } else {
                    holder.goodTitle!!.setTextColor(ColorStateList.valueOf(R.color.white.getColor()))
                }

                holder.currencyIcon!!.setImageBitmap(IconManager.getIconBitmap(item.getGoodCurrency()))
                holder.priceText!!.text = item.getGoodPrice().toString()

                holder.deleteButton!!.onSingleClick ({
                    GoodManager.removeGood(item.getGoodId())
                    GoodManager.goods.removeAt(position)
                    this.notifyItemRemoved(position)
                    if (position != itemCount-1) {
                        notifyItemRangeChanged(position, itemCount - position -1)
                    }
//            (GlobalActivity.fragments[0] as GoodFragment).refresh()
                })

//                holder.editButton!!.onSingleClick ({
//                    val intent = Intent(ApplicationContext, EditGood::class.java)
//                    intent.putExtra("id", item.getGoodId())
//                    intent.flags = FLAG_ACTIVITY_NEW_TASK
//                    ApplicationContext.startActivity(intent)
//                })

                holder.buyButton!!.onSingleClick ({
//                    val intent = Intent(ApplicationContext, ExecuteGood::class.java)
//                    intent.putExtra("position", position)
//                    intent.flags = FLAG_ACTIVITY_NEW_TASK
//                    ApplicationContext.startActivity(intent)
                    val haveBuy = item.buyGood()
                    if (!haveBuy){
                        handler.toast(ApplicationContext.resources.getString(R.string.not_enough_money))
                    } else {
                        handler.toast(ApplicationContext.resources.getString(R.string.bought))
                    }
                })
            }
            FOOTER_VIEW -> {

            }
            HEADER_VIEW -> {

            }
        }

    }

    override fun getItemCount(): Int {
        return GoodManager.goods.size
    }
}