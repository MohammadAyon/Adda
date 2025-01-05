# Adda - Social Chat Application

## Overview
Adda is a modern Android chat application designed for students and professionals to connect and communicate. The app provides a coffee house-themed interface where users can chat, share interests, and build communities.

## Features
- Google Sign-in Authentication
- Real-time Chat Functionality 
- Image File Sharing
- User Profiles with Academic/Professional Info
- Department/Field Based Networking
- Interest-based User Discovery
- User Status Indicators
- Friends List Management
- Clean and Intuitive UI

## Technical Stack
- **Platform**: Android
- **Programming Language**: Kotlin
- **Backend Services**: Firebase
  - Firebase Authentication
  - Firebase Realtime Database
  - Firebase Cloud Storage

- **Architecture**: MVVM (Model-View-ViewModel)

## Dependencies
```gradle
dependencies {
    // Firebase
    implementation 'com.google.firebase:firebase-auth-ktx'
    implementation 'com.google.firebase:firebase-database-ktx'
    implementation 'com.google.firebase:firebase-storage-ktx'
    implementation 'com.google.firebase:firebase-messaging-ktx'
    
    // Google Sign In
    implementation 'com.google.android.gms:play-services-auth'
    
    // Other dependencies will be listed here
}
```

## Setup Instructions
1. Clone the repository
2. Create a Firebase project
3. Add your `google-services.json` file
4. Configure Firebase Authentication with Google Sign-in
5. Set up Firebase Realtime Database rules
6. Build and run the project

## Database Structure
```
adda-app-8c258-default-rtdb.asia-southeast1/
├── addaUser/
│   ├── userId/
│   │   ├── name
│   │   ├── email
│   │   ├── phone
│   │   ├── gender
│   │   ├── department
│   │   ├── interests/
│   │   │   ├── movie
│   │   │   ├── music
│   │   │   └── sports
├── chats/
│   ├── chatId/
│   │   ├── messages/
│   │   │   ├── messageId/
│   │   │   │   ├── text
│   │   │   │   ├── imageUrl
│   │   │   │   ├── timestamp
│   │   │   │   └── senderId
│   │   └── participants/
│   │       ├── userId1
│   │       └── userId2
```

The database is hosted in Firebase's asia-southeast1 region and supports real-time chat functionality with both text and image messages.

## Screenshots

## Screenshots
## Screenshots

<p align="center">
  <img src="https://github.com/user-attachments/assets/db0569a3-b79c-4c3d-af5f-2e81dcfe5609" width="150" />
  <img src="https://github.com/user-attachments/assets/884e0703-52fa-49d6-a2f3-a3baec8a27fc" width="150" />
  <img src="https://github.com/user-attachments/assets/b34097e5-90ca-49f6-9952-18bb0bd07489" width="150" />
  <img src="https://github.com/user-attachments/assets/b33ccdb8-5c9c-45eb-9d59-831633d56719" width="150" />
</p>




## Features in Development
- Chats
- image sharing


