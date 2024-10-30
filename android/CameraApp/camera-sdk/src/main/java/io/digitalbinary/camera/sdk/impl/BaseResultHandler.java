package io.digitalbinary.camera.sdk.impl;

import android.content.Intent;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleObserver;

abstract class BaseResultHandler implements LifecycleObserver {

    /**
     * It is not mandatory to manually unregister or clean up ActivityResultLauncher to avoid memory leaks.
     * The registerForActivityResult API is lifecycle-aware, meaning it is automatically tied to the lifecycle of the Fragment or Activity that registers it.
     * When the Fragment or Activity is destroyed, the ActivityResultLauncher is automatically unregistered.
     * This lifecycle management helps prevent memory leaks without requiring explicit manual cleanup.
     */
    private final ActivityResultLauncher<Intent> activityResultLauncher;

    BaseResultHandler(@NonNull Fragment fragment) {
        this.activityResultLauncher = fragment.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleActivityResult
        );
    }

    BaseResultHandler(@NonNull FragmentActivity activity) {
        this.activityResultLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleActivityResult
        );
    }

    // This method should be called to handle the result
    protected abstract void handleActivityResult(ActivityResult result);

    // Public method to launch an intent
    protected void launchIntent(Intent intent) {
        activityResultLauncher.launch(intent);
    }

    protected void onDestroy() {
        activityResultLauncher.unregister();
    }
}