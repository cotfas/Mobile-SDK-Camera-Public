//
//  ContentView.swift
//  Camera
//
//  Created by Vlad Cotfas on 17.09.2024.
//

import SwiftUI

struct ContentView: View {
    @StateObject private var cameraManager = CameraManager()

    var body: some View {
        VStack {
            Button(action: {
                cameraManager.takePhoto()
            }) {
                Text("   Take Photo   ")
                    .padding()
                    .background(Color.blue)
                    .foregroundColor(.white)
                    .cornerRadius(8)
            }
            
            Button(action: {
                cameraManager.authenticateUser()
            }) {
                Text("  Authenticate  ")
                    .padding()
                    .background(Color.green)
                    .foregroundColor(.white)
                    .cornerRadius(8)
            }
            
            Button(action: {
                cameraManager.accessPhotos()
            }) {
                Text("Access photos")
                    .padding()
                    .background(Color.orange)
                    .foregroundColor(.white)
                    .cornerRadius(8)
            }

            if cameraManager.isAuthenticated {
                List(cameraManager.images, id: \.self) { imageURL in
                    if let image = UIImage(contentsOfFile: imageURL.path) {
                        HStack {
                            Spacer()
                            Image(uiImage: image)
                                .resizable()
                                .scaledToFit()
                                .frame(height: 150)
                            Spacer()
                        }
                    }
                }
            } else {
                Text("Authenticate to view photos")
                    .padding()
            }
        }
        .onAppear {
            // No need to set delegate here if `cameraManager` is already an `@StateObject`.
            // `@StateObject` handles the initialization and lifecycle of `cameraManager`.
        }
    }
}

#Preview {
    ContentView()
}
