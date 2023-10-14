package com.zexceed.restaurant

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.zexceed.restaurant.adapter.TableAdapter
import com.zexceed.restaurant.apiservices.ApiServices
import com.zexceed.restaurant.databinding.ActivityAdminBinding
import com.zexceed.restaurant.databinding.DialogAddTableBinding
import com.zexceed.restaurant.util.Constants.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding

    private lateinit var mAdapter: TableAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            val coroutineScope = CoroutineScope(Dispatchers.IO)
            coroutineScope.launch {
                val req = ApiServices(this@AdminActivity)
                val res = req.getListTableStaff()
                withContext(Dispatchers.Main) {
                    Log.d(TAG, "onCreate: ${res}")
                    mAdapter = TableAdapter(res)
                    rvTable.apply {
                        adapter = mAdapter
                        layoutManager = LinearLayoutManager(this@AdminActivity)
                        setHasFixedSize(true)
                    }
                }
            }

            btnAddTable.setOnClickListener {
                showAddTableDialog()
            }
        }
    }

    private fun showAddTableDialog() {
        val dialogBinding = DialogAddTableBinding.inflate(layoutInflater)
        val dialogBuilder = MaterialAlertDialogBuilder(this@AdminActivity)
        dialogBuilder.setView(dialogBinding.root)
        val customDialog = dialogBuilder.create()

        dialogBinding.apply {
            btnCancel.setOnClickListener {
                customDialog.dismiss()
            }
        }

        customDialog.show()
    }
}