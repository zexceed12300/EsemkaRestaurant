package com.zexceed.restaurant.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zexceed.restaurant.databinding.ItemTableBinding
import com.zexceed.restaurant.models.staff.TableResponseStaff
import com.zexceed.restaurant.models.staff.TableItemResponseStaff

class TableAdapter(private val list: TableResponseStaff) : RecyclerView.Adapter<TableAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableAdapter.ViewHolder {
        val binding = ItemTableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TableAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(private val binding: ItemTableBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TableItemResponseStaff) {
            binding.apply {
                tvNumber.text = "Table ${data.number}"
                tvCode.text = data.code
                tvPrice.text = "Rp. ${data.total}"

                itemView.setOnClickListener {

                }
            }
        }
    }
}