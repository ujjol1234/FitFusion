# FitFusion

A comprehensive health and fitness tracking Android application that connects with Bluetooth Low Energy (BLE) smart scales to provide detailed body composition analysis and health insights.

## 📱 Features

### Core Functionality
- **Smart Scale Integration**: Connect and sync data from BLE-enabled smart scales
- **Body Composition Analysis**: Track multiple health metrics including:
  - Weight and BMI
  - Body Fat Percentage
  - Body Water Percentage  
  - Skeletal Muscle Mass
  - Lean Body Mass
  - Basal Metabolic Rate (BMR)
  - Body Age calculations

### User Management
- **Multi-User Support**: Manage multiple user profiles within a single account
- **User Authentication**: Secure login with email/password and Google Sign-In
- **Profile Management**: Personalized health profiles with age, height, and gender settings

### Data Visualization
- **Interactive Charts**: Visual representation of health trends over time
- **Health Reports**: Comprehensive health analysis and recommendations
- **Historical Data**: Track progress with detailed history views

### Additional Features
- **Multi-Language Support**: Available in 15+ languages including English, Hindi, Bengali, German, Spanish, French, Japanese, Chinese, and more
- **Device Management**: Add, connect, and manage multiple smart scale devices
- **Privacy Controls**: Complete privacy policy compliance and data protection

## 🏗️ Architecture

### Tech Stack
- **Framework**: Android Native with Jetpack Compose
- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)
- **Backend**: Firebase (Authentication & Firestore)
- **Bluetooth**: BLE (Bluetooth Low Energy) integration
- **Charts**: YCharts and Composable-Graphs libraries

### Key Components
- **ViewModels**: 
  - `AuthViewModel` - User authentication management
  - `BleScanViewModel` - Bluetooth device scanning and connection
  - `UserDetailsViewModel` - User profile and health data management
- **Repositories**: 
  - `UserRepository` - Firebase user data operations
  - `UserDetailRepository` - Health metrics and user details
- **Navigation**: Jetpack Compose Navigation with animated transitions

## Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 31 or higher
- Kotlin 1.9.0+
- Google Services account for Firebase integration

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/fitfusion.git
   cd fitfusion
   ```

2. **Firebase Setup**
   - Create a new Firebase project at [Firebase Console](https://console.firebase.google.com)
   - Add your Android app to the Firebase project
   - Download `google-services.json` and place it in the `app/` directory
   - Enable Authentication (Email/Password and Google Sign-In)
   - Set up Firestore Database

3. **Configure Google Sign-In**
   - In Firebase Console, go to Authentication > Sign-in method
   - Enable Google sign-in and add your SHA-1 fingerprint
   - Update the `default_web_client_id` in your `strings.xml`

4. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```

### Configuration

#### Required Permissions
The app requires the following permissions:
- `BLUETOOTH` & `BLUETOOTH_ADMIN` - For BLE device communication
- `BLUETOOTH_CONNECT` & `BLUETOOTH_SCAN` - Android 12+ BLE permissions
- `ACCESS_FINE_LOCATION` & `ACCESS_COARSE_LOCATION` - Required for BLE scanning
- `INTERNET` - For Firebase connectivity

#### Minimum Requirements
- **Min SDK**: 31 (Android 12)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 34

## 📊 App Structure

### Main Screens
- **Home Screen**: Dashboard with latest health metrics and quick actions
- **History Screen**: Charts and historical data visualization
- **Profile Screen**: User management and app settings
- **Device Management**: BLE device pairing and management
- **Health Reports**: Detailed analysis and recommendations

### Navigation Flow
```
Intro → Authentication → Main App (Bottom Navigation)
├── Home (Health Dashboard)
├── History (Data Visualization)  
└── Me (Profile & Settings)
    ├── User Management
    ├── Device Management
    ├── Language Settings
    └── Privacy Policy
```

## 🔧 Development

### Key Dependencies
```kotlin
// Core Android
implementation "androidx.core:core-ktx:1.13.1"
implementation "androidx.activity:activity-compose:1.9.0"

// Jetpack Compose
implementation "androidx.compose.ui:ui:1.6.0-alpha08"
implementation "androidx.compose.material3:material3"
implementation "androidx.navigation:navigation-compose:2.7.5"

// Firebase
implementation "com.google.firebase:firebase-auth"
implementation "com.google.firebase:firebase-firestore"
implementation "com.google.android.gms:play-services-auth:20.6.0"

// Charts & Visualization
implementation "co.yml:ycharts:2.1.0"
implementation "com.github.jaikeerthick:Composable-Graphs:v1.2.3"

// Lifecycle & ViewModel
implementation "androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1"
```

### Building for Release
```bash
./gradlew assembleRelease
```

The app supports Android App Bundle (AAB) format for optimized distribution.

## 🌍 Localization

FitFusion supports multiple languages:
- English (default)
- Bengali (বাংলা)
- German (Deutsch)
- Spanish (Español)
- French (Français)
- Gujarati (ગુજરાતી)
- Hindi (हिन्दी)
- Japanese (日本語)
- Kannada (ಕನ್ನಡ)
- Malayalam (മലയാളം)
- Marathi (मराठी)
- Nepali (नेपाली)
- Punjabi (ਪੰਜਾਬੀ)
- Sanskrit (संस्कृत)
- Sindhi (سنڌي)
- Tamil (தமிழ்)
- Telugu (తెలుగు)
- Urdu (اردو)
- Chinese Simplified (简体中文)

## ScreenShots
![IMG-20250806-WA0013 1](https://github.com/user-attachments/assets/8b5769ad-4765-4e43-8232-4d3ebb0cfd98)
![IMG-20250806-WA0011 1](https://github.com/user-attachments/assets/dbc26011-6a81-4aae-8d75-dc3225ae970a)
![IMG-20250806-WA0016 1](https://github.com/user-attachments/assets/653f32be-557c-484f-a8da-f021a9e0dc68)
![IMG-20250806-WA0015 1](https://github.com/user-attachments/assets/cff94a9e-3dba-4d6f-828f-6921af516b6b)
![IMG-20250806-WA0014 1](https://github.com/user-attachments/assets/08d5bd80-2b32-412f-af7a-4dc556f36477)
![IMG-20250806-WA0010 1](https://github.com/user-attachments/assets/609fb5a6-7172-4b60-93da-8458dffddd06)
![IMG-20250806-WA0008 1](https://github.com/user-attachments/assets/5e581701-8c99-4daa-a427-cf876d70fd05)
![IMG-20250806-WA0069 1](https://github.com/user-attachments/assets/5eaea82e-b970-4a2d-8e04-e2bd38134ffa)
![IMG-20250806-WA0007 1](https://github.com/user-attachments/assets/2f07cbba-8442-4742-bd9d-9d2c3517fff9)

## 🔒 Privacy

FitFusion takes user privacy seriously. All health data is:
- Encrypted in transit and at rest
- Stored securely in Firebase Firestore
- Never shared with third parties without explicit consent
- Compliant with relevant data protection regulations


- The Android development community

---

**Version**: 6.0  
**Last Updated**: 2024  
**Developed with**: ❤️ and ☕
