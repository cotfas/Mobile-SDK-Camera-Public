package io.digitalbinary.camera.sdk;

import java.util.List;

public interface PhotoSDK {

    void takePhoto();
    List<String> accessPhotos();
    void authenticateUser();

}
