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
		implementation 'com.github.Whelp-Inc:android-sdk:1.0.1'
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

----------------------------------------

Third Step : Start Whelp SDK with this example.
-----------------------------------------------

 Whelp.Builder()
                .key("key value")
                .appID("app id")
                .userCredentials(userCredentials)
                .open(this)

