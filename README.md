# Appointment Scheduling System using Spring Boot

## Description

This is a web-based appointment scheduling system built using the Spring Boot framework. It allows users (patients and potentially doctors/admins) to manage appointments online.

**Please note:** This is my first project, serving as a starting point in my development journey in spring boot .

## Features

*   User Registration and Login
*   Appointment Creation
*   Viewing Appointments (potentially filtered by user/role)
*   Editing Appointments
*   Dashboard views for different user roles (e.g., Patient Dashboard, Doctor Dashboard)

## Technologies Used

*   **Backend:** Java, Spring Boot
    *   Spring Web (for handling web requests)
    *   Spring Data JPA (for database interaction)
    *   Spring Security (Likely needed for authentication/authorization, though not explicitly shown)
*   **Frontend:** Thymeleaf (for server-side HTML templating)
*   **Database:** H2 (In-memory, common default for Spring Boot) or other configured database (check `application.properties`)
*   **Build Tool:** Gradle

## Prerequisites

*   Java Development Kit (JDK) 11 or later
*   Gradle (usually handled by the included Gradle Wrapper `gradlew`)
*   An IDE like IntelliJ IDEA, Eclipse, or VS Code (recommended)

## Getting Started

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    cd Appointment-Scheduling-System-using-Spring-Boot-main
    ```
2.  **Build the project:**
    *   On Windows:
        ```bash
        .\gradlew build
        ```
    *   On macOS/Linux:
        ```bash
        ./gradlew build
        ```
3.  **Run the application:**
    *   On Windows:
        ```bash
        .\gradlew bootRun
        ```
    *   On macOS/Linux:
        ```bash
        ./gradlew bootRun
        ```
4.  **Access the application:**
    Open your web browser and navigate to `http://localhost:8080` (or the port configured in `application.properties`).

## Usage

*   Navigate to the home page.
*   Register a new user account or log in with existing credentials.
*   Based on the user role, you will be directed to the appropriate dashboard (e.g., Patient Dashboard).
*   Use the navigation options to view, create, or manage appointments.

## Folder Structure (Key Directories)

```
├── build.gradle              # Gradle build configuration
├── gradlew                   # Gradle wrapper script (Linux/macOS)
├── gradlew.bat               # Gradle wrapper script (Windows)
├── settings.gradle           # Gradle settings
├── src
│   ├── main
│   │   ├── java/com/app      # Main application Java source code
│   │   │   ├── Appointment.java
│   │   │   ├── AppointmentController.java
│   │   │   ├── AppointmentRepo.java
│   │   │   ├── AppointmentSchedulingSystemApplication.java # Main entry point
│   │   │   ├── AppointmentService.java
│   │   │   ├── User.java
│   │   │   └── UserRepo.java
│   │   └── resources
│   │       ├── application.properties # Spring Boot configuration
│   │       ├── img/                   # Static image assets
│   │       └── templates/             # Thymeleaf HTML templates
│   └── test
│       └── java/com/app      # Test source code
└── build/                    # Compiled code and build artifacts (generated)
```

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request.

## License

[Specify License Here - e.g., MIT, Apache 2.0, or leave blank if none]
