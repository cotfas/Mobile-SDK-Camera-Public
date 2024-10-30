# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


# Biometric proguard
-keep class androidx.biometric.** { *; }

# Default proguard for library API access
-keep class io.digitalbinary.camera.sdk.messages.** { *; }
-keep class io.digitalbinary.camera.sdk.PhotoSDK

# Keep the public class and its public constructor
-keep class io.digitalbinary.camera.sdk.impl.PhotoSDKImpl {
    public <init>(androidx.fragment.app.FragmentActivity);
    public <init>(androidx.fragment.app.Fragment);
}

# Keep the public methods in the PhotoSDK class
-keep class io.digitalbinary.camera.sdk.PhotoSDK {
    public void takePhoto();
    public java.util.List accessPhotos();
    public void authenticateUser();
}

# Obfuscate all other classes and methods
-keepclassmembers class io.digitalbinary.camera.sdk.impl.** {
    private *;
    protected *;
}

# Optional: If needed, keep only specific classes for debugging or reflection purposes
-keepclassmembers class io.digitalbinary.camera.sdk.impl.PhotoSDKImpl {
    public <init>(androidx.fragment.app.FragmentActivity);
    public <init>(androidx.fragment.app.Fragment);
}

