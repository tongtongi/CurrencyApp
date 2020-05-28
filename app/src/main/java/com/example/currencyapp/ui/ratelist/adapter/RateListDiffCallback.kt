package com.example.currencyapp.ui.ratelist.adapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil

const val RATE_KEY = "RATE"

class RateListDiffCallback(
    private val newRateList: List<RateItemUIModel>,
    private val oldRateList: List<RateItemUIModel>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldRateList.size

    override fun getNewListSize() = newRateList.size

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ) = oldRateList[oldItemPosition].currency == newRateList[newItemPosition].currency

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ) = oldRateList[oldItemPosition] == newRateList[newItemPosition]

    override fun getChangePayload(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Any? {
        val oldItem = oldRateList[oldItemPosition]
        val newItem = newRateList[newItemPosition]

        val diffBundle = Bundle()
        if (newItem.value != oldItem.value) {
            diffBundle.putSerializable(RATE_KEY, newItem.value)
        }

        return if (diffBundle.size() == 0) null else diffBundle
    }
}