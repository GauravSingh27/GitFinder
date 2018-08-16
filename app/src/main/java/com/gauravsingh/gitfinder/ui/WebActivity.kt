package com.gauravsingh.gitfinder.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.gauravsingh.gitfinder.R
import kotlinx.android.synthetic.main.activity_web.*


internal class WebActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        initToolbar()
        initWebView()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            android.R.id.home -> onBackPressed()

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {

        if (web_view.canGoBack()) {
            web_view.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun initToolbar() {

        setSupportActionBar(toolbar_web)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    private fun initWebView() {

        web_view.apply {
            loadUrl(intent.getStringExtra("url"))
            this.webViewClient = webClient
        }
    }

    private val webClient = object : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {

            view?.loadUrl(request?.url.toString())
            return true
        }
    }
}