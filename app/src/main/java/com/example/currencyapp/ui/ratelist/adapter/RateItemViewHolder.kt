package com.example.currencyapp.ui.ratelist.adapter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyapp.R
import com.example.currencyapp.util.ResourceUtil
import com.example.currencyapp.util.extensions.formatToTwoDecimal
import com.example.currencyapp.util.extensions.hideKeyboard
import com.example.currencyapp.util.extensions.showKeyboard
import kotlinx.android.synthetic.main.item_currency.view.*
import java.math.BigDecimal

class RateItemViewHolder(
    parent: ViewGroup,
    private val onItemClicked: (RateItemUIModel) -> Unit,
    private val onItemTextChanged: (RateItemUIModel, String) -> Unit
) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_currency, parent, false)
    ) {

    private lateinit var item: RateItemUIModel
    private var isBind = false

    private val listener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (!isBind) {
                onItemTextChanged(item, s.toString())
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    fun bind(item: RateItemUIModel) {
        isBind = true

        this.item = item

        setCurrencyIcon()
        setCurrency()
        setCurrencyName()

        itemView.edit_text_currency.setText(item.value.formatToTwoDecimal(2))
        setOnFocusChangeListener()
        itemView.setOnClickListener {
            onClick()
        }
        isBind = false
    }

    private fun onClick() {
        removeTextChangedListener()
        val selectedItem =
            item.copy(value = BigDecimal(itemView.edit_text_currency.text.toString()))
        onItemClicked(selectedItem) // This will update the base currency since it is changed.
        setTextChangedListener()
        itemView.edit_text_currency.requestFocus()
    }

    private fun setOnFocusChangeListener() {
        itemView.edit_text_currency.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                view.showKeyboard()
                onClick()
            } else {
                view.hideKeyboard()
            }
        }
    }

    private fun setTextChangedListener() {
        itemView.edit_text_currency.addTextChangedListener(listener)
    }

    private fun removeTextChangedListener() {
        itemView.edit_text_currency.removeTextChangedListener(listener)
    }

    fun updateCurrency(bundle: Bundle, position: Int) {
        isBind = true
        if (bundle.containsKey(RATE_KEY)) {
            val rate = bundle.getSerializable(RATE_KEY) as BigDecimal
            setCurrencyValue(rate, position)
        }
        isBind = false
    }

    private fun setCurrencyIcon() {
        itemView.image_view_currency_icon.setImageResource(ResourceUtil.getImageFlag(item.currency))
    }

    private fun setCurrency() {
        itemView.text_view_currency.text = item.currency
    }

    private fun setCurrencyName() {
        itemView.text_view_currency_name.text =
            itemView.resources.getString(ResourceUtil.getName(item.currency))
    }

    private fun setCurrencyValue(value: BigDecimal, position: Int) {
        if (position != 0) {
            itemView.edit_text_currency.setText(value.formatToTwoDecimal(2))
        }
        itemView.edit_text_currency.setSelection(itemView.edit_text_currency.text?.length ?: 0)
    }
}