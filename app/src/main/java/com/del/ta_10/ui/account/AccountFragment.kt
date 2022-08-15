package com.del.ta_10.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.del.ta_10.BuildConfig
import com.del.ta_10.R
import com.del.ta_10.databinding.FragmentAccountBinding
import com.del.ta_10.ui.auth.MenuAuthActivity
import com.del.ta_10.util.SharedPrefLogin

class AccountFragment : Fragment() {

    private lateinit var notificationsViewModel: AccountViewModel
    private var _binding: FragmentAccountBinding? = null
    private lateinit var sharedPrefLogin: SharedPrefLogin

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sharedPrefLogin = SharedPrefLogin(binding.root.context)

        return root
    }

    fun loadData(){

        binding.logout.setOnClickListener {
            if (sharedPrefLogin.logout()){
                val intent = Intent(binding.root.context, MenuAuthActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }

        binding.apply {
            textNama.text = sharedPrefLogin.getUser().namaPetani
            Glide.with(photoUser)
                .load(BuildConfig.BASE_URL_FILE+sharedPrefLogin.getUser().potoProfil)
                .apply(RequestOptions.centerInsideTransform()).placeholder(R.drawable.dummy_apple)
                .into(photoUser)

            btnEditProfil.setOnClickListener {
                val intent = Intent(binding.root.context, EditProfilActivity::class.java)
                startActivity(intent)
            }

            btnHsitory.setOnClickListener {
                val intent = Intent(binding.root.context, HistoryActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}