import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

fun openPaymentUrl(context: Context, url: String) {
    val uri = Uri.parse(url)
    try {
        // Preferred: Chrome Custom Tabs
        CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()
            .launchUrl(context, uri)
    } catch (e: ActivityNotFoundException) {
        // Fallback: regular browser intent
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
