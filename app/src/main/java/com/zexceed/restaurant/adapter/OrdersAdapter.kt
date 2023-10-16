package com.zexceed.restaurant.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zexceed.restaurant.databinding.ItemOrdersBinding
import com.zexceed.restaurant.models.OrdersItemResponse
import com.zexceed.restaurant.util.Constants.exportDataToExternalFile
import java.io.ByteArrayOutputStream
import java.io.IOException

class OrdersAdapter: ListAdapter<OrdersItemResponse, OrdersAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrdersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemOrdersBinding) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.Q)
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

                btnExport.setOnClickListener {
                    exportDataToPdf(itemView.context, data.orderId)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun exportDataToPdf(context: Context, ordersId: String) {
        try {
            val document = PdfDocument()

            val paint = Paint()
            val pageInfo1 = PdfDocument.PageInfo.Builder(250, 400, 1).create()
            val page1 = document.startPage(pageInfo1)

            document.finishPage(page1)

            val outputStream = ByteArrayOutputStream()
            document.writeTo(outputStream)
            document.close()
            exportDataToExternalFile(context, "Order-${ordersId}.pdf", outputStream.toByteArray(), "application/pdf")
        } catch (e: IOException) {
            e.printStackTrace()
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