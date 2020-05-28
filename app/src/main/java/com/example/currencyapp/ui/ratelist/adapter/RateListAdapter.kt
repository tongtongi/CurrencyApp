package com.example.currencyapp.ui.ratelist.adapter

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class RateListAdapter(
    private val itemClickListener: (RateItemUIModel) -> Unit,
    private val textChangedListener: (RateItemUIModel, String) -> Unit
) : RecyclerView.Adapter<RateItemViewHolder>() {

    private val items = mutableListOf<RateItemUIModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateItemViewHolder {
        return RateItemViewHolder(
            parent,
            itemClickListener,
            textChangedListener
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RateItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onBindViewHolder(
        holder: RateItemViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val bundle = payloads[0] as Bundle
            holder.updateCurrency(bundle, position)
        }
    }

    fun showRateList(newData: List<RateItemUIModel>) {
        val diffResult = DiffUtil.calculateDiff(RateListDiffCallback(newData, items))
        diffResult.dispatchUpdatesTo(this)
        this.items.clear()
        this.items.addAll(newData)
    }
}