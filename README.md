# Description
- This Application have two pages (MainActivity and MessageActivity)
- **MainActivity**:
    - **Nickname EditText:** A nickname of at least 3 characters is required.
    - **Continue Button:** opens the MessageActivity with the entered nickname.
    - **DELETE DATABASE DATA Button:** deletes all user and message data.(as if the app was installed for the first time)
- **MessageActivity**: The user can send messages. Displays own messages on the right and other messages on the left
- When the application is first logged in or the database is cleaned, the data is taken from the server once and saved in the application database.
- You need to clean the database for get the data from the remote server.
- Once data is received from the remote server, the local server will be used until the database is cleaned. remote server will not be used
- You can login with the user who registered before. Old messages continue to appear on the right.


# Used Technologies
- Kotlin
- Clean Architecture
    - LocaleDataSource
    - RemoteDataSource
    - UseCase
    - MVVM
- Room
- Hilt
- Retrofit
- Moshi
- Glide
- DataBinding
- Unit Test
    - Robolectric