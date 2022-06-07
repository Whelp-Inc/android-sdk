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

  dependencies {
		implementation 'com.github.Whelp-Inc:android-sdk:1.0.5'
	}
-----------------------------------------------------------

How to start Whelp SDK
----------------------

First Step : Create user object and include  language, user email, user full name and user phone.

For Example:
----------------------------
      val userCredentials =
            UserCredentials(
                "en", Contact(
                    "user@test.com", "Name Surname",
                    "+994XXXXXXXXX"
                )
            )
 -----------------------------------
  
   

Second Step : Get Whelp SDK credentials 
----------------------------------------

1.  Sign in to the  [Whelp Web platform](https://web.getwhelp.com)  and go to the  [**Apps -> Livechat app**](https://web.getwhelp.com/apps/webchat)  page.
2.  Click on Install button, if you didn't installed livechat previously, if yes skip this step
3.  Next click  **Configure**  then  **SDK**.
4.  Copy the App ID and API key.

----------------------------------------

Third Step : Start Whelp SDK with this example.
-----------------------------------------------

-----------------------------------
 	Whelp.Builder()
                .key("key value")
                .appID("app id")
                .userCredentials(userCredentials)
                .open(this)

