package io.digitalbinary.camera.sdk.impl;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.digitalbinary.camera.sdk.messages.MessageEvent;
import io.digitalbinary.camera.sdk.messages.SuccessEvent;

class SDKInternal extends BaseResultHandler {

    SDKInternal(Fragment fragment) {
        super(fragment);
    }

    SDKInternal(FragmentActivity activity) {
        super(activity);
    }

    @Override
    protected void handleActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            // Photo was taken successfully
            EventBus.getDefault().post(new MessageEvent("Photo taken successfully!"));
        } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
            // User canceled the photo-taking process
            EventBus.getDefault().post(new MessageEvent("User canceled the photo-taking process"));
        }
    }

    void takePhotoInternal(FragmentActivity activity) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = prepareImageFile(activity);
        String authority = activity.getPackageName() + ".fileprovider";
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(activity, authority, photoFile));
        takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_FRONT); // this can be changed with androidx.camera.camera2 and androidx.camera.lifecycle

        // Launch the intent
        launchIntent(takePictureIntent);
    }

    List<String> accessPhotosInternal(FragmentActivity activity) {
        List<String> results = new ArrayList<>();

        if (!SDKConfig.getInstance().isAuthenticated()) {
            EventBus.getDefault().post(new MessageEvent("Returning empty! Please authenticate!"));
            return results;
        }

        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir != null && storageDir.listFiles() != null) {
            for (File photo : Objects.requireNonNull(storageDir.listFiles())) {
                Log.d("PhotoSDK", "Photo: " + photo.getAbsolutePath());
                results.add(photo.getAbsolutePath());
            }
        }

        if (results.isEmpty()) {
            EventBus.getDefault().post(new MessageEvent("No photos to show! Please take some pictures!"));
            return results;
        }

        return results;
    }

    void authenticateUserInternal(FragmentActivity activity, BiometricAuth biometricAuth) {
        if (!SDKConfig.getInstance().isAuthenticated()) {
            biometricAuth.checkBiometricEnrollment(activity);
        } else {
            EventBus.getDefault().post(new SuccessEvent("Biometric already authenticated"));
        }
    }


    private File prepareImageFile(FragmentActivity activity) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(storageDir, "JPEG_" + timeStamp + ".jpg");
    }
}
