# Lab 2 — Testing + UML: Class Diagrams (Inheritance / Abstraction / Interfaces / Relations)

> **Course:** 5–Software Design — Lab  
> **Tools:** Java 21, IntelliJ IDEA, Maven Wrapper (`./mvnw`), Git  
> **Diagrams:** Visual Paradigm (Community Edition)

## Learning Outcomes
- Write **unit** and **integration** tests.
- Create **Class diagrams** with correct attributes, methods, and relationships.
- Translate diagrams to code and vice versa.
- Apply OO concepts: **inheritance**, **abstraction**, **interfaces**, **associations/aggregation/composition**.

## Part 1 — Testing (Warm‑up)
- Understand the roles of **unit tests** (isolated) versus **integration tests** (collaboration).
- Use mocks/stubs where appropriate to isolate behavior.

## Part 2 — Class Diagrams (4 Mini‑assignments)
1. **Inheritance** — Daily wage calculation for multiple employees.
    - `Programmer`: `(hourly salary * hours worked) + (bug bonus * #bugs)`
    - `CustomerService`: `(hourly salary * hours worked) + (customer bonus * #customers)`
    - `DepartmentOfficer`: `(hourly salary * hours worked) + (company bonus)`  
      → **Implement the code to make the provided tests pass.**

2. **Abstractions** — `Shape` with abstract `calcCircumference()` and `calcArea()`
    - Implement `Circle` and `Square`.
    - In `main`, instantiate multiple shapes and compute both measures.
    - **Add one new shape** of your own that derives area & circumference from a single side (e.g., equilateral triangle).
    - Provide tests.

3. **Interfaces** — *Universal remote* (`VolumeDevice` interface).
    - User can raise/lower volume of multiple devices via a `Remote`.
    - Add new devices by implementing the interface.
    - Design the class diagram first, then implement. Tests are optional but encouraged.

4. **Relations** — *Simple car* model.
    - Model accelerator/brake pedals, wipers, etc.
    - Be explicit about **association vs aggregation vs composition** and multiplicities.
    - Reflect abstract classes vs interfaces correctly in the diagram.

## Deliverables
- Class diagrams for each sub‑assignment.
- Working code and tests where required.

## Build & Test (Maven wrapper)

From the **repo root**:

- Build this lab only (and any dependent modules):
  ```bash
  ./mvnw -pl labs/lab2 -am package
  ```

- Run tests for this lab:
  ```bash
  ./mvnw -pl labs/lab2 -am test
  ```

> If the Maven tab in IntelliJ is greyed out, you likely opened the folder instead of the **root `pom.xml`**. Close the project and re-open the **`pom.xml`**.

## What to Deliver (Portfolio)

Create **one directory** for this lab in your portfolio and submit a **single zip**:

- **UML** (if applicable): export correctly from Visual Paradigm (XML export, then compress).
- **Code**: zip the IntelliJ/Maven module for this lab.

**Naming convention:** `Lab2_FirstnameLastname.zip`.

> Deadlines are announced on Blackboard. Follow the instructions there if they differ.
