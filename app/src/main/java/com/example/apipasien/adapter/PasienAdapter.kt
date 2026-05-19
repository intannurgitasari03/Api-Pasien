package com.example.apipasien.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apipasien.R
import com.example.apipasien.model.Pasien

class PasienAdapter :
    RecyclerView.Adapter<PasienAdapter.PasienViewHolder>() {
    private val pasienList =
        mutableListOf<Pasien>()
    fun setData(newList: List<Pasien>) {
        pasienList.clear()
        pasienList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PasienViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_pasien,
                    parent,
                    false
                )
        return PasienViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: PasienViewHolder,
        position: Int
    ) {
        holder.bind(pasienList[position])
    }
    override fun getItemCount(): Int {
        return pasienList.size
    }

    class PasienViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val tvNama =
            itemView.findViewById<TextView>(R.id.tvNama)
        private val tvDetail =
            itemView.findViewById<TextView>(R.id.tvDetail)
        fun bind(pasien: Pasien) {
            tvNama.text = pasien.nama
            tvDetail.text =
                "${pasien.jenis_kelamin}\n" +
                        "${pasien.tanggal_lahir}\n" +
                        "${pasien.alamat}\n" +
                        "${pasien.no_telepon}"
        }
    }
}