# Shipped to apps that depend on the Whelp SDK and is applied automatically when they
# build with R8/ProGuard (minifyEnabled true).

# API models are parsed by Moshi via reflection, matching JSON keys to field names.
# Without this, R8 renames/strips the fields and e.g. AuthResponse.url comes back null.
-keep class com.whelp.model.** { *; }

# Retrofit builds ApiService from its annotations and generic signatures at runtime.
-keep interface com.whelp.data.ApiService { *; }
-keepattributes Signature, InnerClasses, EnclosingMethod, RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations, AnnotationDefault
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
