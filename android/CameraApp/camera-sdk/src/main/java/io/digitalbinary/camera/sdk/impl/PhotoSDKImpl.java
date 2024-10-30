package io.digitalbinary.camera.sdk.impl;

import android.util.Log;

import io.digitalbinary.camera.sdk.PhotoSDK;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import java.lang.ref.WeakReference;
import java.util.List;

public class PhotoSDKImpl implements PhotoSDK, DefaultLifecycleObserver {

    private final WeakReference<FragmentActivity> activityRef;
    private final BiometricAuth biometricAuth;
    private final SDKInternal sdkInternal;

    public PhotoSDKImpl(FragmentActivity activity) {
        this.activityRef = new WeakReference<>(activity);
        this.biometricAuth = new BiometricAuth();
        this.sdkInternal = new SDKInternal(activityRef.get());
        activity.getLifecycle().addObserver(this);
    }

    public PhotoSDKImpl(Fragment fragment) {
        this.activityRef = new WeakReference<>(fragment.getActivity());
        this.biometricAuth = new BiometricAuth();
        this.sdkInternal = new SDKInternal(fragment);
        fragment.getLifecycle().addObserver(this);
    }

    @Override
    public void takePhoto() {
        FragmentActivity activity = activityRef.get();
        if (activity != null) {
            sdkInternal.takePhotoInternal(activity);
        } else {
            Log.e("camera-sdk", "Activity no longer available!");
        }
    }

    @Override
    public List<String> accessPhotos() {
        FragmentActivity activity = activityRef.get();
        if (activity != null) {
            return sdkInternal.accessPhotosInternal(activity);
        } else {
            Log.e("camera-sdk", "Activity no longer available!");
            return null;
        }
    }

    @Override
    public void authenticateUser() {
        FragmentActivity activity = activityRef.get();
        if (activity != null) {
            sdkInternal.authenticateUserInternal(activity, biometricAuth);
        } else {
            Log.e("camera-sdk", "Activity no longer available!");
        }
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        sdkInternal.onDestroy();
        activityRef.clear();
        owner.getLifecycle().removeObserver(this);
    }
}
