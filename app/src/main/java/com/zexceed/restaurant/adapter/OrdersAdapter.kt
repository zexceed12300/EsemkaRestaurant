package com.zexceed.restaurant.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zexceed.restaurant.databinding.ItemOrdersBinding
import com.zexceed.restaurant.models.OrdersItemResponse

class OrdersAdapter: ListAdapter<OrdersItemResponse, OrdersAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrdersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemOrdersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: OrdersItemResponse) {
            binding.apply {
                tvOrderDate.text = "Order ${position + 1} - ${data.createdAt} "
                tvStatus.text = data.status
                var menuName = ""
                var menuPrice = ""
                for (item in data.orderDetails) {
                    menuName += "${item.quantity} ${item.menu.name}\n"
                    menuPrice += "Rp. " + item.menu.price + "\n"
                }
                tvMenuName.text = menuName
                tvMenuPrice.text = menuPrice
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<OrdersItemResponse> = object: DiffUtil.ItemCallback<OrdersItemResponse>() {
            override fun areItemsTheSame(oldItem: OrdersItemResponse, newItem: OrdersItemResponse): Boolean {
                return oldItem.orderId == newItem.orderId
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: OrdersItemResponse, newItem: OrdersItemResponse): Boolean {
                return oldItem == newItem
            }

        }
    }
}