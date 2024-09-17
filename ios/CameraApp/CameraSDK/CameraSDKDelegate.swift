//
//  CameraSDKDelegate.swift
//  CameraSDK
//
//  Created by Vlad Cotfas on 17.09.2024.
//

import Foundation

public protocol CameraSDKDelegate: AnyObject {
    func didTakePhoto(success: Bool, photoURL: URL?)
    func didAccessPhotos(_ photoURLs: [URL]?)
    func didAuthenticate(success: Bool, error: Error?)
}
