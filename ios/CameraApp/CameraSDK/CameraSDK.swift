//
//  CameraSDK.swift
//  CameraSDK
//
//  Created by Vlad Cotfas on 17.09.2024.
//

import Foundation
import AVFoundation
import LocalAuthentication
import UIKit

public class CameraSDK: NSObject, CameraSDKProtocol, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    public weak var delegate: CameraSDKDelegate?
    private var isAuthenticated: Bool = false
    
    private let biometricPolicy: LAPolicy = .deviceOwnerAuthenticationWithBiometrics

    public override init() {}
    
 
    public func takePhoto() {
        let cameraAuthorizationStatus = AVCaptureDevice.authorizationStatus(for: .video)
        
        switch cameraAuthorizationStatus {
        case .denied, .restricted:
            // Access denied or restricted, redirect user to settings
            print("Camera needs permission, please enable it from settings")
            openSettings()
        case .notDetermined, .authorized:
            // Check if camera is available
            if UIImagePickerController.isSourceTypeAvailable(.camera) {
                openCamera()
            } else {
                // Camera is not available
                print("Camera is not available")
            }
        @unknown default:
            // Other errors
            print("Other errors on takePhoto")
        }
    }
    
    public func accessPhotos() {
        if !isAuthenticated {
            delegate?.didAccessPhotos(nil)
            return
        }

        let fileManager = FileManager.default
        do {
            let photoURLs = try fileManager.contentsOfDirectory(at: photoDirectory, includingPropertiesForKeys: nil)
            
            // Sort photoURLs by creation date in descending order
            let sortedPhotoURLs = photoURLs.sorted {
                let creationDate1 = (try? fileManager.attributesOfItem(atPath: $0.path)[.creationDate] as? Date) ?? Date.distantPast
                let creationDate2 = (try? fileManager.attributesOfItem(atPath: $1.path)[.creationDate] as? Date) ?? Date.distantPast
                return creationDate1 > creationDate2
            }
            
            delegate?.didAccessPhotos(sortedPhotoURLs)
        } catch {
            delegate?.didAccessPhotos(nil)
        }
    }
    
    public func authenticateUser() {
        let context = LAContext()
        var error: NSError?

        if context.canEvaluatePolicy(biometricPolicy, error: &error) {
            let reason = "Authenticate to access photos"
            context.evaluatePolicy(biometricPolicy, localizedReason: reason) { success, error in
                DispatchQueue.main.async {
                    if success {
                        self.isAuthenticated = true
                        self.delegate?.didAuthenticate(success: true, error: nil)
                    } else {
                        self.isAuthenticated = false
                        self.delegate?.didAuthenticate(success: false, error: error)
                    }
                }
            }
        } else {
            self.delegate?.didAuthenticate(success: false, error: error)
        }
    }
    
    private func openCamera() {
        DispatchQueue.main.async {
            if let rootViewController = UIApplication.shared.windows.first?.rootViewController {
                let imagePickerController = UIImagePickerController()
                imagePickerController.delegate = self
                imagePickerController.sourceType = .camera
                imagePickerController.cameraDevice = .front
                imagePickerController.mediaTypes = ["public.image"]
                
                rootViewController.present(imagePickerController, animated: true, completion: nil)
            }
        }
    }
    
    public func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        picker.dismiss(animated: true) {
            if let image = info[.originalImage] as? UIImage {
                // Save image to disk
                self.saveImage(image)
            } else {
                self.delegate?.didTakePhoto(success: false, photoURL: nil)
            }
        }
    }
    
    public func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        picker.dismiss(animated: true, completion: nil)
        self.delegate?.didTakePhoto(success: false, photoURL: nil)
    }

    private func saveImage(_ image: UIImage) {
        let timestamp = Date().timeIntervalSince1970
        let filename = "selfie_\(timestamp).jpg"
        let fileURL = photoDirectory.appendingPathComponent(filename)
        
        guard let imageData = image.jpegData(compressionQuality: 1.0) else {
            delegate?.didTakePhoto(success: false, photoURL: nil)
            return
        }
        
        do {
            try imageData.write(to: fileURL)
            delegate?.didTakePhoto(success: true, photoURL: fileURL)
        } catch {
            delegate?.didTakePhoto(success: false, photoURL: nil)
        }
    }
    
    private var photoDirectory: URL {
        let fileManager = FileManager.default
        let urls = fileManager.urls(for: .documentDirectory, in: .userDomainMask)
        let dir = urls[0].appendingPathComponent("SelfiePhotos")
        if !fileManager.fileExists(atPath: dir.path) {
            try? fileManager.createDirectory(at: dir, withIntermediateDirectories: true, attributes: nil)
        }
        return dir
    }
    
    private func openSettings() {
        if let settingsUrl = URL(string: UIApplication.openSettingsURLString) {
            if UIApplication.shared.canOpenURL(settingsUrl) {
                UIApplication.shared.open(settingsUrl, options: [:], completionHandler: nil)
            }
        }
    }
}
