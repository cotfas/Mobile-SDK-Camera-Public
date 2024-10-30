package io.digitalbinary.camera.sdk.impl;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.Executor;

import io.digitalbinary.camera.sdk.messages.MessageEvent;
import io.digitalbinary.camera.sdk.messages.SuccessEvent;

class BiometricAuth {

    private static final int BIOMETRIC_TYPE = BiometricManager.Authenticators.BIOMETRIC_WEAK;

    void checkBiometricEnrollment(FragmentActivity activity) {
        BiometricManager biometricManager = BiometricManager.from(activity);

        switch (biometricManager.canAuthenticate(BIOMETRIC_TYPE)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                // Biometric is enrolled and ready to use
                // You can now proceed to show a biometric prompt
                EventBus.getDefault().post(new MessageEvent("Biometric please login"));
                biometricPrompt(activity);
                break;

            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                // Device doesn't support biometric authentication
                SDKConfig.getInstance().setAuthenticated(false);
                EventBus.getDefault().post(new MessageEvent("Device doesn't support biometric authentication, or was used too many times, retry later!"));
                break;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                // Biometric hardware is currently unavailable
                SDKConfig.getInstance().setAuthenticated(false);
                EventBus.getDefault().post(new MessageEvent("Biometric hardware is currently unavailable"));
                break;

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // User hasn't enrolled any biometric credentials, prompt enrollment
                SDKConfig.getInstance().setAuthenticated(false);
                promptBiometricEnrollment(activity);
                EventBus.getDefault().post(new MessageEvent("Starting biometric enrollment"));
                break;
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                SDKConfig.getInstance().setAuthenticated(false);
                EventBus.getDefault().post(new MessageEvent("Biometric other errors"));
                break;
        }
    }

    private void biometricPrompt(FragmentActivity activity) {
        Executor executor = ContextCompat.getMainExecutor(activity);
        BiometricPrompt biometricPrompt = new BiometricPrompt(activity, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Authentication success
                SDKConfig.getInstance().setAuthenticated(true);
                EventBus.getDefault().post(new SuccessEvent("Biometric Authentication success"));
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                // Authentication failed
                SDKConfig.getInstance().setAuthenticated(false);
                EventBus.getDefault().post(new MessageEvent("Biometric Authentication failed"));
            }

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                // Handle the error
                SDKConfig.getInstance().setAuthenticated(false);
                EventBus.getDefault().post(new MessageEvent("Biometric Authentication error: " + errString));
            }
        });

        // Remove negativeText when using BiometricManager.Authenticators.DEVICE_CREDENTIAL
        String negativeText = "Cancel";

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Authenticate to access your photos")
                .setAllowedAuthenticators(BIOMETRIC_TYPE)
                .setNegativeButtonText(negativeText)
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private void promptBiometricEnrollment(FragmentActivity activity) {
        Intent enrollIntent = new Intent(android.provider.Settings.ACTION_BIOMETRIC_ENROLL);
        enrollIntent.putExtra(android.provider.Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BIOMETRIC_TYPE);
        activity.startActivity(enrollIntent);
    }
}
