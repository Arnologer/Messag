# Add project specific ProGuard rules here.
-keep class org.signal.** { *; }
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}
