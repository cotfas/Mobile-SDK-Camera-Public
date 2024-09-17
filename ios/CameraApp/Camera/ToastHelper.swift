//
//  UIExtensions.swift
//  Camera
//
//  Created by Vlad Cotfas on 17.09.2024.

import UIKit

class ToastHelper {
    static func showToast(_ message: String, duration: TimeInterval = 4.0) {
        DispatchQueue.main.async {
            guard let keyWindow = UIApplication.shared.windows.filter({ $0.isKeyWindow }).first,
                  let rootViewController = keyWindow.rootViewController else {
                return
            }

            let view = rootViewController.view ?? keyWindow
            let toastLabel = UILabel()
            toastLabel.numberOfLines = 0 // Allow multiple lines
            toastLabel.lineBreakMode = .byWordWrapping
            toastLabel.textAlignment = .center
            toastLabel.textColor = UIColor.white
            toastLabel.backgroundColor = UIColor.black.withAlphaComponent(0.6)
            toastLabel.text = message
            toastLabel.font = UIFont.systemFont(ofSize: 12.0)
            toastLabel.alpha = 1.0
            toastLabel.layer.cornerRadius = 10
            toastLabel.clipsToBounds = true

            // Set up auto-layout constraints
            toastLabel.translatesAutoresizingMaskIntoConstraints = false
            view.addSubview(toastLabel)

            // Add constraints
            NSLayoutConstraint.activate([
                toastLabel.centerXAnchor.constraint(equalTo: view.centerXAnchor),
                toastLabel.bottomAnchor.constraint(equalTo: view.bottomAnchor, constant: -50), // Adjusted to move closer to the bottom
                toastLabel.widthAnchor.constraint(lessThanOrEqualToConstant: 300), // Adjust width as needed
                toastLabel.heightAnchor.constraint(greaterThanOrEqualToConstant: 35) // Adjust height as needed
            ])
            
            // Calculate the actual size of the toastLabel based on its content
            let maxSize = CGSize(width: 300, height: CGFloat.greatestFiniteMagnitude)
            let textSize = toastLabel.sizeThatFits(maxSize)
            toastLabel.frame.size = CGSize(width: min(textSize.width + 20, 300), height: textSize.height + 20)

            UIView.animate(withDuration: duration, delay: 0.1, options: .curveEaseOut, animations: {
                toastLabel.alpha = 0.0
            }, completion: { _ in
                toastLabel.removeFromSuperview()
            })
        }
    }
}
