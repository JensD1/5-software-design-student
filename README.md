# 5–Software Design — Student Starter

Welcome! This repository contains the **starter code and instructions** for the *5–Software Design* labs.
Each lab lives in `labs/labX` and has its **own README** with the tasks, tips, and the exact build commands.

---

## Prerequisites
- **Java 21** (Temurin 21 recommended, e.g., via SDKMAN)
- **Git**
- **IntelliJ IDEA** (Community or Ultimate)
- **Maven Wrapper** (already included: use `./mvnw`; you do **not** need to install Maven)

---

## Quick Start (IntelliJ)
1. **Open the project by the root `pom.xml`**  
   *File → Open…* → select the **root `pom.xml`** → *Open as Project*.
2. **Verify SDKs (Java 21 everywhere)**  
   *File → Project Structure → Project* → SDK = **21**; Language level = **21**.  
   *Project Structure → Modules* → ensure modules inherit the Project SDK.  
   *Settings → Build Tools → Maven → Importing* → JDK for importer = **21**.
3. **Reload Maven** using the **Maven** tool window (**Reload All Maven Projects**).

> If the Maven tab is greyed out, you most likely opened a folder instead of the **root `pom.xml`**. Close the project and re-open the `pom.xml`.

---

## Repository Layout (Student)
```
.
├─ labs/
│  ├─ lab1/
│  ├─ lab2/
│  └─ ...
├─ .mvn/wrapper/        # Maven wrapper files
├─ mvnw, mvnw.cmd       # Maven wrapper launchers
├─ pom.xml              # Root parent POM (Java 21, shared plugins/deps)
└─ README.md            # This file
```

---

## Build & Test

All commands are run from the **repo root** using the **Maven wrapper** (`./mvnw`).

- **Build everything (skip tests)**
  ```bash
  ./mvnw clean package -DskipTests
  ```

- **Run all tests in all labs**
  ```bash
  ./mvnw clean verify
  ```

- **Work on a single lab** (build & run that lab's tests only)
  ```bash
  ./mvnw -pl labs/labX -am test
  ```
  
  - example:
    ```bash
    ./mvnw -pl labs/lab1 -am test
    ```

- **Run a specific test class from the IDE**  
  Open the test class and click the green ▶ gutter icon.

---

## How To Get Started
- Review the presentation for each lab.
- Read the readme.md
- Places where to code is annotated with TODO's.

---

## Deliverables (Portfolio)

Read the presentations and verify how to deliver your solution (and what should be included)! 

In short:

_Portfolio:_
- **One zip** containing:
    - **Code**: this entire repository (***Remove '.IDEA' for submission***).
    - **UML exports**:
      - **One folder for *each* lab** when applicable (Visual Paradigm project exported as defined in the presentations).
    - **AI usage Document** (if AI is used)
      - A document (type of choice: word, txt, or markdown) explaining how, why, and for what tasks AI is used.
      - ***ENSURE YOU UNDERSTAND WHAT YOU DELIVER*** 
- **Naming convention:** `5SD_Portfolio_FirstnameLastname.zip` (replace with your name).
- **Deadlines & submission** are announced on Blackboard — follow any instructions there if they differ.

_Project:_

- **One zip** containing:
    - **Code**: the entire repository of your project (***Remove '.IDEA' for submission***).
    - **UML exports**.
    - **Slides**
    - **AI usage Document** (if AI is used)
      - A document (type of choice: word, txt, or markdown) explaining how, why, and for what tasks AI is used.
      - ***ENSURE YOU UNDERSTAND WHAT YOU DELIVER***
- **Naming convention:** `5SD_Project_FirstnameLastname.zip` (replace with your name).
- **Deadlines & submission** are announced on Blackboard — follow any instructions there if they differ.

---

## Assessment methods
**_Always verify on SISA for the latest updates!_**

>The practical part is evaluated in three ways:
> 
> First, presence and cooperation in practica sessions is mandatory and will be assessed during the semester. Next, the created portfolio is delivered before the 7th lab session at a specific date and time mentioned during the class. During the 7th lab session all students will be questioned individually to assess their understanding of the provided code and UML diagrams. Finally, the students deliver, present and defend their project during a time slot in the examination period.
For the first practica session, the students can submit their assignment in order to receive feedback and improve themselves towards the later practical sessions. This submission and feedback is not assessed in the final evaluations. However, they have to submit their assignments into their portfolio during the examination period. Hence, the students can improve their submissions based on the feedback they received. Students are allowed and encouraged to ask questions during the next lab sessions when the requirements are unclear.
Note that the evaluations of the presence, portfolio, project and presentation are transferred to the second assessment period. This means the practical part of the course cannot be redone in the second assessment period. 

> **AI**
> 
> The students are allowed to use generative AI. However, they must keep track of a logbook mentioning why, when and where they used AI. This use is only permitted to deepen their understanding of design patterns and assist in the debugging process. It is therefore essential that students fully understand the entire code base they submit.
> 
> WARNING: AI can hallucinate and be wrong. Always be critical of its output and do not blindly believe, copy, or use its output. 

---

## Common Issues & Tips

- **Maven tab greyed out in IntelliJ** → open the **root `pom.xml`**, not the folder.
- **JUnit 5 only** (`org.junit.jupiter.*`); avoid JUnit 4 imports.
- **Java 21** everywhere (Project SDK, module SDK, Maven importer JDK).
- If you see benign **Byte Buddy/Mockito agent** warnings on Java 21 during tests, you can ignore them.

Good luck, and have fun building clean, tested designs! 👍

---