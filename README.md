To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  and
  
  Step 2. Add the dependency

 	 dependencies { implementation 'com.github.Whelp-Inc:android-sdk:$latest_version'}
-----------------------------------------------------------

How to start Whelp SDK
----------------------

First Step : Get Whelp SDK credentials 
----------------------------------------

1.  Sign in to the  [Whelp Web platform](https://web.getwhelp.com)  and go to the  [**Apps -> Livechat app**](https://web.getwhelp.com/apps/webchat)  page.
2.  Click on Install button, if you didn't installed livechat previously, if yes skip this step
3.  Next click  **Configure**  then  **SDK**.
4.  Copy the App ID and API key.

----------------------------------------

Second Step : Start Whelp SDK with this example.
-----------------------------------------------

-----------------------------------

Add manually AppChromeClient class to your project. Add fragment/Activity that you will be using WhelpWebView to WeakReference in AppChromeClient

	class AppChromeClient(private val fragmentWeakReference: WeakReference<"YourFragment/YourActivity">) :
    WebChromeClient() {
    private var openFileCallback: ValueCallback<Array<Uri>>? = null

    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        if (filePathCallback == null) {
            return (super.onShowFileChooser(webView, filePathCallback, fileChooserParams))
        }
        openFileCallback = filePathCallback
        val webViewFragment = fragmentWeakReference.get() ?: return false
        webViewFragment.launchGetMultipleContents("*/*")

        return true
    }

    fun receiveFileCallback(result: Array<Uri>) {
        openFileCallback?.onReceiveValue(result)
        openFileCallback = null
    }}
    
    
In your Fragment or Activity add WhelpWebView layout to your XML layout

	<com.whelp.view.WhelpWebView
          android:id="@+id/whelpView"
          android:layout_width="match_parent"
          android:layout_height="match_parent"/>

In your Fragment/Activity class declare chromeClient, contentLauncher and then add corressponding functions. 
    
    private val chromeClient = AppChromeClient(WeakReference(this))
    private var contentLauncher: ActivityResultLauncher<String> = getMultipleContentLauncher()
    
     fun launchGetMultipleContents(type: String) {
        contentLauncher.launch(type)
    }

    private fun getMultipleContentLauncher(): ActivityResultLauncher<String> {
        return this.registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { list ->
            chromeClient.receiveFileCallback(list.toTypedArray())
        }
    }

Then, initialize webview

	val userCredentials =
            UserCredentials(
                "en", Contact(
                    "user@test.com", "Name Surname",
                    "+994XXXXXXXXX"
                )
            )

        Whelp.Builder()
            .key("ey value")
            .appID("app id")
            .userCredentials(userCredentials)
            .open(this) {

                binding.whelpView.webChromeClient = chromeClient

                binding.whelpView.loadUrl(it)
            }

  
