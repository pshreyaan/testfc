## 🤖 Supplier Portal - Forecast Collaboration Automation
A robust, modular, and scalable Selenium/TestNG-based automation framework for end-to-end web testing across multiple browsers. The framework is designed for maintainability, configurability, and easy test case expansion, following best practices and clean code principles.

## ⚙️ Preferred Tools
- Github Desktop
- Eclipse IDE

## 📂 Project Structure

```plaintext
suppplierportal-fc-automation/
│
├── pom.xml                   # Maven project descriptor
├── README.md                 # Project overview and setup
│
├── src/
│   ├── main/java/
│   │   ├── components/
│   │   │   └── login/        # Contains UI component logic and interaction methods for your app screens; Page Object Model (POM)
│   │   ├── pages/            # Orchestrates flows or scenarios using one or more components. (e.g., UploadSample...)
│   │   ├── utils/            # Common utilities (Methods, ConfigReader...)
│   │   └── webdriverfactory/ # WebDriver provider logic
│   │
│   └── resources/
│       └── config/
│           └── config.properties   # Environment configuration
│
│
├── src/test/java/
│   ├── base/                 # Test base classes (DriverManagerBase)
│   ├── uploadSample/         # Functional 
│   └── check/                # Additional test samples
│
├── reports/                  # Test execution reports
├── suite/                    # TestNG suite files (testng.xml)
├── resources/                # External data, xml, etc.
│
└── target/                   # Compiled outputs (auto-generated)
```

## 🚀 Getting Started

1.  **Install Java (>=8) & Maven**

3. **Install TestNG Plugin from the Market Place in Eclipse**
    
4.  **Clone the repo, Use github Desktop**
    
5.  **Install dependencies:**
   
  ```plaintext

    mvn clean install

   ```
5. **Configure environment:**

    - **Update src/main/resources/config/config.properties with your URLs, credentials, etc.**
      
## 🧩 Key Components

-   **WebDriverFactory:** Central browser driver management (multi-browser support)
    
-   **ConfigReader:** Loads property values from config
    
-   **Methods:** Selenium utility methods for actions/waits
    
-   **DriverManagerBase:** Base class for test classes (setup/teardown)
    
-   **Page Objects (POM):** For login flows and business flows 

## ✅ How To Add a New Test

1.  **Create the components required to complete the flow** in `src/main/java/components`

2.  **Create a business logic/flow class** in `src/main/java/pages/...`
    
3.  **Write a test class** in `src/test/java/...` extending `DriverManagerBase`
    
4.  **Add the test to** `**suite/testng.xml**` if using suites
    
5.  **Run with Maven or your IDE**


## 📦 Project Structure & Layer Responsibilities

### 1. **components/**

-   **Role:**  
    Contains **UI component logic** and **interaction methods** for your app screens (typically mapped 1:1 with a page or significant UI area).
    
-   **Technical term:**  
    “Page Object Model (POM)” or “Page Objects.”
    
-   **What’s inside:**  
    Methods for interacting with elements (login, click, fill fields, etc.)—**encapsulating all UI actions**.
    
-   **Example:**  
    `Network.java` handles login and message upload actions for the "Network" app.

### 2. **pages/**

-   **Role:**  
    Orchestrates **flows or scenarios** using one or more components.  
    Think of it as a “story” that strings together steps to achieve a business objective.
    
-   **Technical term:**  
    “Test Flows,” “Business Scenarios,” or “Workflow Classes.”
    
-   **What’s inside:**  
    Methods that sequence component actions for end-to-end automation (ex: login + upload message).
    
-   **Example:**  
    `UploadSampleNetwork.java` combines all necessary actions to log in and upload a sample.

### 3. **test/java/functional/**

-   **Role:**  
    Contains **TestNG test classes**—the _actual_ test scripts that verify your app by running various flows.
    
-   **Technical term:**  
    “Test Suite,” “Functional Tests,” “End-to-End Tests.”
    
-   **What’s inside:**  
    Methods annotated with `@Test` (TestNG) that call page flows and assert expected results.
    
-   **Example:**  
    `UploadSample.java` runs test cases like login + upload on the Network app and asserts on-screen messages.

## **In Practice**

-   **components:**  
    “How do I click, fill, or interact with UI stuff?”
    
-   **pages:**  
    “What’s the complete flow or story I want to automate?”
    
-   **test/java/functional:**  
    “What scenario do I want to verify, and how do I check the results?”
    

----------
