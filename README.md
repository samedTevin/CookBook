<div align="center">
  <img src="photos/icon.png" alt="CookBook Logo" width="110" />

  # CookBook

  **A Whisk Worth Taking!**

  ![Kotlin](https://img.shields.io/badge/Kotlin-D85A30?style=flat&logo=kotlin&logoColor=white)
  ![Android](https://img.shields.io/badge/Android-FFFFFF?style=flat&logo=android&logoColor=D85A30&labelColor=FFFFFF)
  ![MVVM](https://img.shields.io/badge/MVVM_Architecture-D85A30?style=flat)
  ![Room](https://img.shields.io/badge/Room_Database-FFFFFF?style=flat&logo=sqlite&logoColor=D85A30&labelColor=FFFFFF)
  ![Retrofit](https://img.shields.io/badge/Retrofit_2-D85A30?style=flat)
  ![License](https://img.shields.io/badge/MIT_License-FFFFFF?style=flat&labelColor=FFFFFF)

</div>

---

## 📌 Overview

CookBook is a native Android application engineered in Kotlin for discovering, filtering, and organizing global culinary recipes. Built with modern Android architecture guidelines, the app features dynamic REST API integration, local database caching, interactive decision-making tools, and multi-language localization.

---

## 📸 App Showcase

<div align="center">
  <img src="photos/CookBook_en_overview_transparent.png" alt="CookBook Showcase" width="100%" />
</div>

---

## 📹 Video Demo

<div align="center">
  <video src="videos/CookBook_Promo.mp4" controls width="100%" poster="photos/CookBook_en_overview_transparent.png">
    Your browser does not support HTML5 video. <a href="videos/CookBook_Promo.mp4">Click here to watch the promo video</a>.
  </video>
</div>

---

## ✨ Key Modules & Features

- **Interactive Onboarding:** Guided multi-screen onboarding flow built with `ViewPager2` and animated page indicators (`DotsIndicator`).
- **Dynamic Recipe Discovery:** Fetch and browse recipes across global cuisines, categories, or specific ingredients via REST API endpoints.
- **World Cuisine Exploration:** Explore dishes by country flags (Spain, Brazil, China, France, Turkey, Japan, and more).
- **Spin the Wheel Game:** Gamified 26-letter decision wheel ("Your Lucky Letter") in `GameFragment` helping indecisive users choose a meal based on random lucky letters.
- **Roll the Dice Randomizer:** One-tap random meal generator on the Home Screen featuring custom Lottie animations for instant recipe discovery.
- **Smart Instructions Formatting:** Raw API recipe data is dynamically parsed and formatted into numbered HTML steps with bold headers, complete ingredient lists with measures, and clear formatting.
- **One-Click YouTube Video Tutorials:** Direct integration launching YouTube video guides for step-by-step visual cooking assistance.
- **Personalized Content Sections:** Dynamic "Made for You" (based on preferred cuisine) and "Explore by Ingredient" sections adapting to user choices.
- **Search & Shimmer Skeleton Loading:** Instant recipe search by keyword with real-time feedback and smooth Facebook Shimmer skeleton placeholders.
- **Local Persistence & Favorites:** Save and manage favorite recipes offline using Room Database with animated Lottie heart toggles.
- **Profile Customization & Photo Storage:** User profile photo selection from local device storage, profile information updates, and dark theme toggling.
- **Localization:** Full multi-language support across 6 languages (English, Turkish, German, French, Chinese, and Russian).
- **Secure Session Cleanup:** Complete preferences and database cleanup upon account deletion for user privacy.

---

## 🛠️ Architecture & Technology Stack

- **Language:** Kotlin
- **Architecture:** MVVM (Model-View-ViewModel), Repository Pattern, Sealed UI States
- **UI Layer:** ViewBinding, Material 3 Design, Jetpack Navigation SafeArgs, ViewPager2
- **Local Storage:** Room Database, SharedPreferences (SessionManager)
- **Networking:** Retrofit 2, Gson Converter
- **Image & UI Feedback:** Glide (with skeleton placeholders and error fallbacks), Facebook Shimmer, Lottie Animations

---

## 🚀 Setup & Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/YOUR_USERNAME/CookBook.git
   ```
2. Open the project in Android Studio.
3. Allow Gradle to sync dependencies, then run the application on an emulator or physical Android device.

---

## 📄 License

This project is open-source under the [MIT License](LICENSE).
