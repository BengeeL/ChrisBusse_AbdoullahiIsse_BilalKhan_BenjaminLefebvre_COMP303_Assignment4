# Blood Bank Information System - Microservices Assignment

**Course**: COMP303  
**Assignment**: Microservices Development  
**Due Date**: Week 13  

## Purpose

This assignment will help you practice the following:

1. **Developing, coding, and testing microservices** for specified requirements.
2. Creating **REST APIs** integrated with a **MySQL or MongoDB** database.
3. Implementing **React.js** to consume REST services effectively.

## Instructions

### General Rules
- Work in **pairs or groups** (maximum 4 students per group with equal contributions).
- Solutions must be demonstrated in a **scheduled lab session** (Week 12/13).
- Submit the project via the **e-centennial assignment dropbox** before the demonstration.
- All group members must submit and demonstrate the assignment solution.

### Project Naming
Name your project as follows:  
`Student Names_COMP303_AssignmentNumber`  
*Example*: `John_Smith_COMP303_Assignment4`

### Code Guidelines
- Each file should include:
  - **Student Name**
  - **Student Number**
  - **Submission Date**
- Add comments to functions or predicates to explain their purpose and logic clearly.

### Assignment Description
You are tasked with developing microservices for a **Blood Bank Information System**. The system includes functionalities to manage donors, blood banks, and bloodstock information.

#### Components

1. **Eureka Server**  
   Set up a **service registry** (Eureka server) to manage microservices communication.  
   References: [Spring Service Registration and Discovery](https://spring.io/guides/gs/service-registration-and-discovery)

2. **Microservice 1**  
   A Spring Boot application that:
   - Provides REST APIs to:
     - Add, view, and edit donor details.
     - Add, view, and edit blood bank and stock details.
     - Check blood group availability.
     - View donor history (past donations).
   - Uses **MySQL** for data storage with the following entities:
     - **Donor**  
       Fields: FirstName, LastName, Age/DOB, Gender, BloodGroup, City, Phone  
     - **BloodBank**  
       Fields: BloodbankName, Address, City, Phone, Website, Email  
     - **BloodStock**  
       Fields: BloodGroup, Quantity, BestBefore(Date), Status  
   - Database name: `BloodDB`.

3. **Microservice 2**  
   A **React.js** or **Swagger** web application to:
   - Consume the REST APIs from Microservice 1.
   - Provide functionalities for:
     - Donor registration and login.
     - View and edit donor profile.
     - Schedule a blood donation date.
     - View donation history.

#### Innovation Suggestions
- Add new features to the REST API or React app.
- Implement **Spring Security** (e.g., login authentication).
- Use **Spring Cloud** features.
- Any relevant enhancements!

---

## Project Structure

### Eureka Server
- **Folder Name**: `eureka-server`
- Contains the Eureka Service Registry.

### Microservice 1
- **Folder Name**: `blood-bank-service`
- Implements the core functionalities for donor, blood bank, and stock management.

### Microservice 2
- **Folder Name**: `blood-bank-webapp`
- React-based web application to interact with Microservice 1.

---

## Submission Instructions
1. Ensure all code files include:
   - Student Name(s)
   - Student Number(s)
   - Submission Date
2. Validate user inputs and use proper coding standards.
3. Submit the project in the specified format to the assignment dropbox.
4. Prepare for a **virtual demonstration or recorded video submission**.

---

## Assessment Rubrics

| **Criteria**                              | **Points** |  
|-------------------------------------------|------------|  
| Spring microservices development          | 40 points  |  
| DB implementation and entity validations  | 35 points  |  
| UI friendliness and code standards        | 5 points   |  
| Innovation                                | 10 points  |  
| Demonstration or recorded submission      | 10 points  |  
| **Total**                                 | **100 points** |

---

## References
- **Lecture Slides**: Weeks 9–12  
- **Lab Exercises**: Weeks 9–12  

For additional guidance, refer to the following resources:
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React.js Documentation](https://reactjs.org/docs/getting-started.html)
- [Eureka Server Guide](https://spring.io/guides/gs/service-registration-and-discovery)

---

### Contributors
List group members here:
- **Name 1** (Student Number)
- **Name 2** (Student Number)
- **Name 3** (Student Number)
- **Benjamin Lefebvre** (301234587)
