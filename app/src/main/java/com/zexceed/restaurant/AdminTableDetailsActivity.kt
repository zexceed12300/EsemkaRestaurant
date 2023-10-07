package com.zexceed.restaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.zexceed.restaurant.adapter.OrdersAdapter
import com.zexceed.restaurant.apiservices.ApiServices
import com.zexceed.restaurant.databinding.ActivityAdminOrdersBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminTableDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminOrdersBinding

    private lateinit var mAdapter: OrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {



            mAdapter = OrdersAdapter()

            val coroutineScope = CoroutineScope(Dispatchers.IO)
            coroutineScope.launch {
                val req = ApiServices(this@AdminTableDetailsActivity)
                val res = req.getTableDetailsStaff(intent.getStringExtra(ADMIN_ORDERS_TABLEID).toString())
                mAdapter.submitList(res?.orders)
                withContext(Dispatchers.Main) {
                    tvTableName.text = "Table ${res?.number}"
                    tvCode.text = res?.code
                    rvOrders.apply {
                        adapter = mAdapter
                        layoutManager = LinearLayoutManager(this@AdminTableDetailsActivity)
                        setHasFixedSize(true)
                    }
                }
            }

            btnCloseTable.setOnClickListener {
                finish()
            }
        }
    }

    companion object {
        const val ADMIN_ORDERS_TABLEID = "adminOrdersTableId"
        const val ADMIN_ORDERS_TABLENAME = "adminOrdersTableName"
        const val ADMIN_ORDERS_TABLECODE = "adminOrdersTableCode"
    }
}