package br.edu.ifrn.imoveis.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import br.edu.ifrn.imoveis.R
import br.edu.ifrn.imoveis.activity.dialogs.AboutDialog
import br.edu.ifrn.imoveis.extensions.setupToolbar
import kotlinx.android.synthetic.main.activity_site_ifrn.*

class SiteIfrnActivity : BaseActivity() {
    private val URL_SOBRE = "http://portal.ifrn.edu.br"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site_ifrn)
        val actionBar = setupToolbar(R.id.toolbar)
        actionBar.setDisplayHomeAsUpEnabled(true)
        // WebView
        setWebViewClient(webview)
        webview.loadUrl(URL_SOBRE)
        // Swipe to Refresh
        swipeToRefresh.setOnRefreshListener {
            webview.reload()
        }
        // Cores da animação
        swipeToRefresh.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3)

        //webview.loadUrl("javascript:alert('Olá')")

    }

    private fun setWebViewClient(webview: WebView) {
        webview.setWebViewClient(object : WebViewClient() {
            override fun onPageStarted(webview: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(webview, url, favicon)
                // Liga o progress
                progress.visibility = View.VISIBLE
            }

            override fun onPageFinished(webview: WebView, url: String) {
                // Desliga o progress
                progress.visibility = View.INVISIBLE
                // Termina a animação do Swipe to Refresh
                swipeToRefresh.isRefreshing = false
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()
                if (url.endsWith("sobre.htm")) {
                    // Alerta customizado
                    AboutDialog.showAbout(supportFragmentManager)
                    // Retorna true para informar que interceptamos o evento
                    return true
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        })
    }
}
