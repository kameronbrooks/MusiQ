**MusiQ**

Overview

MusiQ is an advanced digital music platform that provides users with access to millions of songs from its own catalog and from partners. Along with basic music functions, MusiQ also has an AI-optimized learning engine that will recommend songs based on the user’s tastes, listening habits and preferences. The main objective is to offer users a unique and personalized music listening experience.

MusiQ addresses the challenge of discovering new music that aligns with user tastes. With millions of available songs, existing music apps often fall short in providing personalized recommendations. MusiQ solves this problem by utilizing advanced machine learning algorithms to analyze user listening habits and preferences, offering tailored music suggestions. The app features a user-friendly interface and integrates seamlessly with popular music streaming platforms.

MusiQ unites the music experience across Android and iOS platforms. It combines multiple music streaming services with users' local libraries, providing a centralized hub for accessing favorite music easily. The app uses advanced AI to offer personalized playlists and music recommendations, enhancing the music discovery process.

Major Features

*   Combining various music platforms: Condenses the most popular music streaming platforms into one location for ease of use and access.
*   Access to any song: If a user cannot find their favorite song on their preferred music platform, MusiQ can pull the song from another app.
*   Playlist generation: Uses AI to generate an entire playlist based on a few user-submitted songs.
*   Song suggestions: Provides intelligent song suggestions based on listening history, tracking similar users and current trends.

Requirements

Functional Requirements

*   Third-Party processes:

*   Access and play songs from Spotify's database, receiving metadata for each song.
*   Analyze songs for BPM and key, creating playlists based on user requests and song data.

*   Local file processes:

*   Access and play local song files upon user request.
*   Analyze local songs for BPM and key to make personalized song suggestions.

Non-Functional Requirements

Performance

*   Index at least 1000 songs per minute on average devices.
*   Return local search results within one second.
*   Process songs in less than 10 seconds each.
*   Support at least 1000 concurrent users, with 95% of responses within one second.
*   Performs tasks, such as search results, fast.

Reliability

*   Reliable and functional even during high web traffic.
*   Support offline listening mode for users.

Availability

*   Support offline listening mode for users.
*   MusiQ will run 24/7.

Security

*   Encrypt all web traffic over HTTPS and use OAuth 2.0 for third-party interactions.
*   Apply secure coding practices and multi-factor authentication for users.

Maintainability

*   Emphasize modularity, clear interfaces, and comprehensive documentation.
*   Use consistent coding standards and routine testing.

Portability

*   Available on both Apple iOS and Android platforms.
*   Developed using JavaScript for the web, Java for Android, and ported Objective-C for iOS.

Database Requirements

Library data will be stored on the user’s device using an SQLite database.

Project Process Description

Gather Requirements

*   Document all functional and non-functional requirements.

Design System

*   Document all design constraints and design decisions regarding the interface, platform, and programming languages.

Code and Test System

*   Compile code and test as you go, following requirements and design decisions.

Integrate and Test System

*   Release MusiQ on both Android and iOS platforms with continuous minimal testing.

Maintain System

*   Provide continuous support by monitoring and addressing bugs, with new features and updates for MusiQ.

Team

*   Kameron Brooks: Group Lead, AI/ML Engineer, Backend Developer (Project Management, TensorFlow, API Development)
*   Spenser Catchpole: Business Logic Designer, Android Developer, & Database Integration Engineer (Java Programming, Android Development, SQL)
*   Preethi Chandrashekar: QA Engineer/Tester & Android Developer (Quality Assurance, Testing, Java Programming, Android Development)
*   Anthony Chen: API Integration Engineer & AI/ML Engineer (Java Programming, Android Development, API Integration, TensorFlow)
*   Victoria Davis: UI/UX Designer & iOS Developer (User Experience and User Interface Design, iOS Development)
*   Benevolence Ed-Malik: Business Logic Designer, Android Developer & QA Engineer/Tester (Quality Assurance, Testing, Java Programming, Android Development)

Contribution

Contributions to improve MusiQ are accepted as long as they adhere to coding standards.

License
