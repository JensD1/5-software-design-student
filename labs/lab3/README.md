# Lab 3 — Design Patterns: Singleton & Observer (Time Registration)

> **Course:** 5–Software Design — Lab  
> **Tools:** Java 21, IntelliJ IDEA, Maven Wrapper (`./mvnw`), Git  
> **Diagrams:** Visual Paradigm (Community Edition)

## Learning Outcomes
- Implement the **Singleton** pattern safely.
- Implement the **Observer** pattern to decouple state changes from notifications.
- Write at least one **integration test**.

## Context
You will continue a **Time Registration** app that records check‑in/out times for employees.

## Tasks
1. **Make the database a Singleton.** Ensure there is **only one** instance, with a global access method. Consider thread‑safety where relevant.
2. **Replace manual prints** with **observers** that react to database changes.
    - Create observers that:
        - (a) announce that the database was updated, and
        - (b) print the entry that was added; *(optional)* (c) include the employee name in the message.
    - Use Java’s `PropertyChangeListener` interfaces (do **not** use deprecated APIs).
3. **Testing**
    - Unit tests are provided for the Database and Controller.
    - **Add at least one integration test** of your own.

## Suggested Steps
- Introduce `getInstance()` on the DB class, hide constructors, and guard against multiple instances.
- Add observer registration to the DB; fire change events on updates.
- Wire observers in `main` and verify their output when entries are added/changed.

## Build & Test (Maven wrapper)

From the **repo root**:

- Build this lab only (and any dependent modules):
  ```bash
  ./mvnw -pl labs/lab3 -am package
  ```

- Run tests for this lab:
  ```bash
  ./mvnw -pl labs/lab3 -am test
  ```

> If the Maven tab in IntelliJ is greyed out, you likely opened the folder instead of the **root `pom.xml`**. Close the project and re-open the **`pom.xml`**.

## What to Deliver (Portfolio)

Create **one directory** for this lab in your portfolio and submit a **single zip**:

- **UML** (if applicable): export correctly from Visual Paradigm (XML export, then compress).
- **Code**: zip the IntelliJ/Maven module for this lab.

**Naming convention:** `Lab3_FirstnameLastname.zip`.

> Deadlines are announced on Blackboard. Follow the instructions there if they differ.
