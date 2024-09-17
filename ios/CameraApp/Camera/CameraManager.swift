//
//  CameraManager.swift
//  Camera
//
//  Created by Vlad Cotfas on 17.09.2024.
//

import Foundation
import CameraSDK
import SwiftUI

class CameraManager: ObservableObject, CameraSDKDelegate {
    private let cameraSDK: CameraSDK

    @Published var images: [URL] = []
    @Published var isAuthenticated = false

    init() {
        cameraSDK = CameraSDK()
        cameraSDK.delegate = self
    }

    func takePhoto() {
        cameraSDK.takePhoto()
    }

    func authenticateUser() {
        cameraSDK.authenticateUser()
    }

    func accessPhotos() {
        cameraSDK.accessPhotos()
    }

    func didTakePhoto(success: Bool, photoURL: URL?) {
        if success, let photoURL = photoURL {
            print("Photo taken successfully: \(photoURL)")
            ToastHelper.showToast("Photo taken successfully: \(photoURL)")
            
            // If isAuthenticated refresh UI list
            if (isAuthenticated) {
                accessPhotos()
            }
        } else {
            print("Failed to take photo")
            ToastHelper.showToast("Failed to take photo")
        }
    }

    func didAccessPhotos(_ photoURLs: [URL]?) {
        if let photoURLs = photoURLs {
            DispatchQueue.main.async {
                self.images = photoURLs
            }
        } else {
            print("Failed to access photos or not authenticated")
            ToastHelper.showToast("Failed to access photos or not authenticated")
        }
    }

    func didAuthenticate(success: Bool, error: Error?) {
        DispatchQueue.main.async {
            self.isAuthenticated = success
        }
        if success {
            print("Authentication successful")
            ToastHelper.showToast("Authentication successful")
            accessPhotos()
        } else {
            print("Authentication failed: \(error?.localizedDescription ?? "Unknown error")")
            ToastHelper.showToast("Authentication failed: \(error?.localizedDescription ?? "Unknown error")")
        }
    }
}
