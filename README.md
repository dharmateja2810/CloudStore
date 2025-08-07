# CloudStore

A modern cloud storage platform built with Spring Boot and AWS S3, offering seamless file management and secure user access.

## Technologies Used

- Backend: Spring Boot 3.1.4
- Database: PostgreSQL
- Storage: AWS S3
- Security: Spring Security
- Frontend: Thymeleaf, Bootstrap

## Features

- Secure user authentication and authorization
- File upload and download functionality
- Integration with AWS S3 for scalable storage
- Clean and intuitive user interface
- Environment-based configuration

## Technical Implementation

- **Spring Boot**: Utilized for the backend framework, providing robust REST APIs
- **AWS S3**: Implemented for scalable file storage with proper security configurations
- **PostgreSQL**: Used for storing user data and file metadata
- **Spring Security**: Implemented for secure user authentication and authorization
- **Environmental Configuration**: Used .env for secure credential management

## Running Locally

1. Clone the repository
2. Configure environment variables in `.env`:
   ```properties
   POSTGRES_USERNAME=your_username
   POSTGRES_PASSWORD=your_password
   AWS_ACCESS_KEY=your_aws_key
   AWS_SECRET_KEY=your_aws_secret
   AWS_BUCKET=your_bucket_name
   AWS_REGION=your_region
   ```
3. Run PostgreSQL database
4. Execute: `mvn spring-boot:run`
5. Access the application at: `http://localhost:8081`

## Database Schema

- Users Table: Stores user authentication details
- Files Table: Manages file metadata and S3 references

## Security Measures

- Secure password handling with BCrypt encryption
- Protected API endpoints with Spring Security
- Environment-based configuration for sensitive data
- AWS S3 secure file storage

## Deployment

### Local Development Setup

1. **Prerequisites:**
   - Java 21 or later
   - Maven
   - PostgreSQL installed and running
   - AWS Account with S3 access

2. **Setup Steps:**
   ```bash
   # Clone the repository
   git clone https://github.com/dharmateja2810/CloudStore
   cd CloudStore

   # Create .env file and add your configuration
   cp .env.example .env
   # Edit .env with your credentials

   # Build and run
   mvn clean install
   mvn spring-boot:run
   ```

### Production Deployment

1. **Prerequisites:**
   - AWS Account
   - AWS CLI installed and configured
   - PostgreSQL RDS instance
   - S3 bucket created

2. **Deployment Steps:**
   - Package application: `mvn clean package -DskipTests`
   - Create Elastic Beanstalk application
   - Upload JAR file: `target/file-manager-1.0-SNAPSHOT.jar`
   - Configure environment variables in Elastic Beanstalk
   - Update security groups and networking settings
   - Application will be available at your Elastic Beanstalk URL

## Future Enhancements

- File sharing functionality
- Advanced search capabilities
- File versioning
- Bulk upload/download features

## Contact

Email : dharmanarisetty@gmail.com
