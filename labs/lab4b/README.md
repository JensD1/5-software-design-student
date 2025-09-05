# Lab 4b — Abstract Factory (Tesla Models)

> **Course:** 5–Software Design — Lab  
> **Tools:** Java 21, IntelliJ IDEA, Maven Wrapper (`./mvnw`), Git  
> **Diagrams:** Visual Paradigm (Community Edition)

## Learning Outcomes
- Design and implement the **Abstract Factory** pattern.
- Produce related objects (product families) without coupling clients to concrete classes.

## Scenario
Tesla sells 4 main models: **S, 3, X, Y**. Two colors to support first: **Black** and **Red**.

## Tasks
1. **Design a Class Diagram** for the abstract factory setup:
    - `TeslaFactory` (interface) with:
      ```java
      Tesla getRedCar(String name);
      Tesla getBlackCar(String name);
      ```
    - One **concrete factory per model** (e.g., `ModelSFactory`, `Model3Factory`, …).
    - A family of products per factory (e.g., `ModelS_Red` / `ModelS_Black`, etc.).
    - An optional `FactoryProvider` to choose a factory by model name.
2. **Implement the code** following your diagram.
    - Ensure each concrete `Tesla` subtype sets its model string and color.
    - Implement behavior differences per model (e.g., acceleration increments).
3. **Demo** in a small `main` class that builds a few cars via factories only.

## Testing
- Provide unit tests for at least one factory/product family.
- Keep object creation in one place; clients depend only on interfaces.

## Build & Test (Maven wrapper)

From the **repo root**:

- Build this lab only (and any dependent modules):
  ```bash
  ./mvnw -pl labs/lab4b -am package
  ```

- Run tests for this lab:
  ```bash
  ./mvnw -pl labs/lab4b -am test
  ```

> If the Maven tab in IntelliJ is greyed out, you likely opened the folder instead of the **root `pom.xml`**. Close the project and re-open the **`pom.xml`**.

## What to Deliver (Portfolio)

Create **one directory** for this lab in your portfolio and submit a **single zip**:

- **UML** (if applicable): export correctly from Visual Paradigm (XML export, then compress).
- **Code**: zip the IntelliJ/Maven module for this lab.

**Naming convention:** `Lab4b_FirstnameLastname.zip`.

> Deadlines are announced on Blackboard. Follow the instructions there if they differ.
