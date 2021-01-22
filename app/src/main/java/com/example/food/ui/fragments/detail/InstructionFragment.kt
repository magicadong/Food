package com.example.food.ui.fragments.detail

import android.content.Context
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.example.food.R
import com.example.food.data.model.Result
import com.example.food.databinding.FragmentInstructionBinding


class InstructionFragment : Fragment() {
    private var _binding: FragmentInstructionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInstructionBinding.inflate(inflater,container,false)

        val result:Result? = arguments?.getParcelable("result")
        val url = result!!.sourceUrl
        Log.v("ppp","SourceUrl:$url")
        binding.webView.loadUrl(url)
        binding.webView.webViewClient = object : WebViewClient(){

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.progressBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progressBar.visibility = View.INVISIBLE
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}