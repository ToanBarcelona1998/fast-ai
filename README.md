# Fastai

Fastai is a Kotlin-based backend server built using Ktor. It serves as the backend for a client application, providing essential functionalities such as user authentication, purchase management, and integration with a third-party AI API for image generation.

## Features

- **User Authentication**: Secure user registration and login processes.
- **PostgreSQL Database**: Utilizes PostgreSQL for efficient data storage and management.
- **Layered Architecture**: Follows a layered architecture pattern for better organization and maintainability.
- **API Endpoints**:
    - User registration and login
    - Purchase control
    - Order management
    - Purchase history retrieval
    - Image generation through third-party AI API

## Getting Started

### Prerequisites

- Kotlin 1.5 or higher
- Ktor 1.6 or higher
- PostgreSQL database

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/fastai.git
   cd fastai
   ```

2.	Set up your PostgreSQL database:
      •	Create a new database for the Fastai project.
      •	Update the database connection settings in your application configuration file.

3.	Build the project:
      ```bash
      ./gradlew build
      ```

4.	Run the server:
      ```bash
      ./gradlew run  
      ```
### Usage

Once the server is running, you can access the API endpoints. Refer to the documentation for details on how to interact with each endpoint for user registration, login, purchasing, and image generation.

### API Documentation

Refer to the API Documentation for detailed information on the available endpoints and their usage.

### Contributing

- Contributions are welcome! If you have suggestions for improvements or want to report a bug, please open an issue or submit a pull request.

### License

This project is licensed under the MIT License - see the LICENSE file for details.

### Acknowledgements

	•	Ktor: A framework for building asynchronous servers and clients in connected systems.
	•	PostgreSQL: An open-source relational database management system.
	•	[Third-Party AI API Name]: For image generation capabilities.