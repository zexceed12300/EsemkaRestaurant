package com.zexceed.restaurant.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zexceed.restaurant.R
import com.zexceed.restaurant.apiservices.ApiServices
import com.zexceed.restaurant.databinding.DialogChangeStatusBinding
import com.zexceed.restaurant.databinding.ItemOrdersBinding
import com.zexceed.restaurant.models.OrdersItemResponse
import com.zexceed.restaurant.util.Constants.exportDataToExternalFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection

class OrdersStaffAdapter(
    private val tableId: String,
    private val onStatusChanged: () -> Unit
): ListAdapter<OrdersItemResponse, OrdersStaffAdapter.ViewHolder>(DIFF_CALLBACK) {

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

                itemView.setOnClickListener {
                    showChangeStatusOrderDialog(itemView.context, data)
                }
            }
        }
    }

    private fun showChangeStatusOrderDialog(context: Context, data: OrdersItemResponse) {
        val dialogBinding = DialogChangeStatusBinding.inflate(LayoutInflater.from(context))
        val dialogBuilder = MaterialAlertDialogBuilder(context)
        dialogBuilder.setView(dialogBinding.root)

        val customDialog = dialogBuilder.create()
        customDialog.show()
        dialogBinding.apply {
            btnCancel.setOnClickListener {
                customDialog.dismiss()
            }

            val listStatus: ArrayList<String> = arrayListOf(
                "Ordered",
                "OnCooking",
                "Cooked",
                "Done",
            )
            val arrayAdapter = ArrayAdapter(context, R.layout.item_dropdown_status, listStatus)
            dropdownStatus.setAdapter(arrayAdapter)

            btnSubmit.setOnClickListener {
                val coroutineScope = CoroutineScope(Dispatchers.IO)
                coroutineScope.launch {
                    val req = ApiServices(context)
                    val res = req.changeOrderStatus(tableId, data.orderId, dropdownStatus.text.toString())
                    if (req.responseCode == HttpURLConnection.HTTP_OK) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Status changed to ${dropdownStatus.text}", Toast.LENGTH_SHORT).show()
                        }
                    }
                    withContext(Dispatchers.Main) {
                        onStatusChanged()
                        customDialog.dismiss()
                    }
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