package jp.konami.duell.presentation.web

import android.net.Uri
import android.os.Bundle
import android.webkit.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import jp.konami.duell.R
import jp.konami.duell.database.DatabaseStorage
import jp.konami.duell.navigation.Extras
import jp.konami.duell.navigation.RouterImpl
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

class WebActivity : MvpAppCompatActivity(), WebMvpView {
    lateinit var mainWebView: WebView

    private val webPresenter by moxyPresenter {
        WebPresenter(DatabaseStorage(applicationContext), RouterImpl(this))
    }
    private var filePathCallback: ValueCallback<Array<Uri>>? = null
    private val launcher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) {
            filePathCallback?.onReceiveValue(it.toTypedArray())
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        mainWebView = findViewById(R.id.mainWebView)
        mainWebView.loadUrl(intent.getStringExtra(Extras.URL_EXTRA) ?: "https://google.com")

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (mainWebView.canGoBack()) mainWebView.goBack()
            }
        })

        mainWebView.settings.userAgentString =
            mainWebView.settings.userAgentString.replace("wv", "")
        mainWebView.settings.javaScriptEnabled = true
        mainWebView.settings.domStorageEnabled = true

        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(mainWebView, true)

        mainWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String) {
                super.onPageFinished(view, url)
                CookieManager.getInstance().flush()
                if (url == "https://sweetfall.live/") webPresenter.onDomainLink()
                else webPresenter.onLink(url)
            }
        }

        mainWebView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                this@WebActivity.filePathCallback = filePathCallback
                launcher.launch("image/*")
                return true
            }
        }
    }
}