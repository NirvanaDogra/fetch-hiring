# App Screen shots

## Success Case
<img src="readme%20assets/saplshscreen.png" alt="Splash Screen" width="300" height="500" />
<img src="readme%20assets/success.png" alt="Success Screen" width="300" height="500" />

## Error Cases

<img src="readme%20assets/network_error.png" alt="Network error" width="300" height="500" />
<img src="readme%20assets/timeout.png" alt="timeout error" width="300" height="500" />


# Module Architecture Overview
![img_1.png](readme%20assets/module.png)

## Fetch Hiring Feature - ViewModel and UseCase Implementation

This module is designed to fetch and process hiring data for the application. It includes a ViewModel to manage the UI state, a UseCase for fetching and processing data, and dependency injection using Dagger Hilt for managing dependencies.

### Architecture Overview
The code follows a clean architecture pattern, separating concerns between the ViewModel, UseCase, and repository. The goal is to keep each component focused on a single responsibility.

#### Key Components:
1. **FetchHiringViewModel**: A ViewModel responsible for handling the UI state (Loading, Success, Error) and interacting with the UseCase to fetch data.
2. **FetchHiringUseCase**: A use case that interacts with the repository to fetch and preprocess the hiring data.
3. **HiringRepository**: A repository responsible for interacting with the network service and the local database.
4. **HiringService**: A Retrofit service to make API calls to fetch hiring data.
5. **DI Setup (Dagger Hilt)**: The use of Dagger Hilt for dependency injection to provide instances of services, repositories, and use cases.

### Design Choices & Benefits

#### 1. **Separation of Concerns**:
- The code is split into several layers (ViewModel, UseCase, Repository) with well-defined responsibilities.
- The `ViewModel` is only concerned with managing UI state and calling the `UseCase`. It is agnostic of the business logic or data-fetching process.
- The `UseCase` handles the business logic of fetching and processing data, abstracting the details of network calls and caching.
- The `Repository` abstracts access to the data sources (network or local cache), promoting modularity and reusability.

#### 2. **State Management with Sealed Classes**:
- The `HiringState` sealed class is used to represent the different states of the UI (Loading, Success, Error).
- This approach helps in managing the UI state in a type-safe and declarative way.
- It allows the ViewModel to handle different states effectively, ensuring the UI reacts to state changes seamlessly.

#### 3. **Error Handling**:
- The `ViewModel` uses `try-catch` blocks to handle errors like timeouts, network failures, or general exceptions.
- The use of meaningful error messages, fetched from string resources, ensures that the app provides clear feedback to the user in case of failures.
- The `TimeoutCancellationException` and `IOException` are caught specifically, providing more granular control over error scenarios.

#### 4. **Network and Cache Handling**:
- The `FetchHiringUseCaseImpl` class contains logic to handle network failures and cache the data locally when appropriate.
- If the API returns a 304 status code (Not Modified), the app checks the local cache for data before making another API call. This improves app performance by reducing redundant network requests.
- The `Repository` layer handles both fetching data from the network and inserting it into a local cache.

#### 5. **Use of Coroutines for Asynchronous Operations**:
- Coroutines are used for making asynchronous API calls without blocking the main thread, improving app responsiveness.
- The `viewModelScope.launch` function ensures that the network request is performed off the main thread.
- The `withTimeout` function is used to enforce a maximum duration for the network request, preventing the app from hanging indefinitely in case of network issues.

#### 6. **Dependency Injection with Dagger Hilt**:
- Dagger Hilt is used for managing dependencies, making the code more modular and testable.
- The `@Inject` annotations allow for the automatic injection of required dependencies, reducing boilerplate code and improving maintainability.
- The `FetchHiringModule` class provides a centralized place to configure and provide instances of services, repositories, and use cases, promoting a clean and scalable dependency management approach.