# Bank Account Withdrawal Project
### Project Overview

This project focuses on enhancing a basic banking operation involving account 
balance withdrawal and event notification. The primary goal is to improve the provided code snippet by enhancing varoius
aspects such as structure, efficiency, maintainability, and overall quality, while preserving the existing 
business logic.

### Objectives
* Refactor the existing code to improve:
  * Code structure and readability
  * Efficiency and throughput
  * maintainability and flexibility
  * Consistency and fault tolerance
  * Testability and dependency management
  * Observability and auditability
  * Portability and correctness
  * Cost efficiency and data governance
  * Interoperability and architecture
* Ensure that the fundamental business capability remains unchanged

### Approach
1. **Code Analysis:** Examine the provided code snippet to identify areas fo improvement.
2. **Refactoring:** Implement changes to enhance the code based on the identified areas, ensuring that the core 
      functionality is preserved.
3. **Documentation:** Clearly document the changes made, including the rationale behind each modification.
4. **Review:** Assess the refactored code to ensure that it meets the outlined objectives and maintains the original 
      business logic.

### Implementation Choices
*  **programming Language:** Java was chosen due to its robustness and widespread use in
   enterprise applications.
*  **Framework:** Springboot was utilized streamline the development process and leverage its features for
   building production-ready applications.
*  **Event Notification:** AWS SNS(Simple Notification Service) was selected for event notifications due to its
   reliability and scalability.

### Refactored Snippet
### Solution
* Introduced the following layers to decouple the project.
  * controller, for our entry 
  * service, for our business logic
  * event, for event entities
  * exception, for handling exception
  * repository, for database connection
  * structure.txt in the root of the project
  * Config
  * .yml file for the app properties.
  * Unit tests, not implemented
  * logging
  * Dependency Injection

### Problems
   1. Tight coupling, snsClient is initialized directly in the constructor, difficult to test.
   2. Lacks separation of concern, service, repository etc.
   3. Hardcoded values. snsTopicarn etc.
   4. Error handling eg Global exceptions
   5. Missing logging
   6. Code duplication, Repeated use of sql
   7. Non-atomic operation, transaction man.
   8. Lack of testability.

### In summary 
    This is the structure that i would use separating layers, encapsulation, moularity 
    scalability, testing, decoupling and etc. there is also the Hexagon architecture that we can use 
    depending on the organization.