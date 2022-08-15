package com.del.ta_10.ui.pemesanan

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.del.ta_10.R

class PemesananFragment : Fragment() {

    companion object {
        fun newInstance() = PemesananFragment()
    }

    private lateinit var viewModel: PemesananViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pemesanan_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PemesananViewModel::class.java)
        // TODO: Use the ViewModel
    }

}