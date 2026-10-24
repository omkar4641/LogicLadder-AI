# LogicLadder AI

**Your personal AI-guided journey to mastering Data Structures and Algorithms.**

LogicLadder AI is a Java-based, console-driven DSA learning platform that helps learners organize daily practice, track their progress, and understand problem-solving approaches with AI assistance. It combines a structured question bank with personalized study planning and AI-generated coding contests powered by Groq.

![Java](https://img.shields.io/badge/Java-21%2B-ED8B00?logo=openjdk&logoColor=white) ![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven&logoColor=white) ![MySQL](https://img.shields.io/badge/Database-MySQL-4479A1?logo=mysql&logoColor=white) ![Groq](https://img.shields.io/badge/AI-Groq-F55036)

## Contents

- [Overview](#overview)
- [Application Modules](#application-modules)
  - [1. Initial Menu](#1-initial-menu)
  - [2. Account Creation](#2-account-creation)
  - [3. Login](#3-login)
  - [4. Onboarding](#4-onboarding)
  - [5. Main Menu](#5-main-menu)
  - [6. Today's DSA Tasks](#6-todays-dsa-tasks)
  - [7. AI DSA Mentor](#7-ai-dsa-mentor)
  - [8. DSA Progress Dashboard](#8-dsa-progress-dashboard)
  - [9. AI-Generated DSA Contest](#9-ai-generated-dsa-contest)
  - [10. Daily Study Capacity](#10-daily-study-capacity)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Setup and Run](#setup-and-run)
- [Environment Variables](#environment-variables)
- [Database Setup Note](#database-setup-note)
- [Security Notes](#security-notes)
- [Current Limitations](#current-limitations)
- [Project Status](#project-status)

## Overview

Learning Data Structures and Algorithms consistently can be difficult without a clear plan or feedback on progress. LogicLadder AI brings common study activities together in one place: choosing daily questions, recording solved problems, checking progress, and learning problem-solving approaches with AI guidance.

The application currently runs in the terminal. Java handles the application logic and user interface, MySQL stores user and problem-related data, and the Groq API generates AI-assisted explanations and contest questions.

## Application Modules

### 1. Initial Menu

The initial menu is the application's entry point. It gives the user three choices: create an account, log in to an existing account, or exit the application. The user is directed to the selected flow through the console menu.

### 2. Account Creation

The account creation module collects the information required to register a new user. It validates the password input and uses the project's `PasswordUtil` class to hash passwords with Argon2id before account data is stored. The application is designed to store a password hash rather than a plain-text password.

### 3. Login

The login module asks the user for their username and password, retrieves the stored account information, and verifies the entered password against the stored hash. When authentication succeeds, the user is taken to the main menu. Login depends on a working MySQL connection and a correctly configured database.

### 4. Onboarding

The onboarding module collects the learner's initial DSA learning preferences, including their current DSA level. This information is used by the application to personalize the learner's experience and select suitable questions from the configured question bank.

### 5. Main Menu

After a successful login, the main menu provides access to the application's primary modules:

1. **View My Progress** — review current-level and difficulty-wise progress.
2. **Today's DSA Tasks** — view the daily question plan, open problem links, mark solved questions, or use AI assistance.
3. **AI Contest** — generate a short DSA contest based on the learner's current level and recorded solving profile.
4. **Set Daily Study Hours for DSA** — update the daily study-time capacity in minutes.
5. **Logout** — leave the current user session.

The menu acts as the central navigation point for the current console-based implementation.

### 6. Today's DSA Tasks

The daily task module displays the learner's current DSA level, configured daily study limit, number of selected questions, and planned solving time. Each question is shown with its ID, title, topic, learning stage, difficulty, estimated duration, platform, and problem URL. A learning-resource URL is displayed when one is available.

The learner can choose **Mark Solved Questions** to record completed problems, **AI Assist** to request learning guidance for a selected problem, or **Exit** to return to the main menu. Daily task selection depends on the question-bank data and the application's problem-selection rules.

### 7. AI DSA Mentor

The AI DSA Mentor is available through the AI Assist flow in Today's DSA Tasks. It is intended to teach the reasoning behind a solution instead of simply returning complete solution code. The learner can choose one of three assistance modes:

- **Explain and Dry Run** — understand the problem statement and work through an example step by step.
- **Brute-Force Approach** — learn a direct approach, how its decisions are made, a detailed dry run, and its time and space complexity.
- **Optimal Approach** — study a more efficient approach, including a detailed dry run and complexity analysis.

The service sends the selected problem context to the Groq API and formats the generated response for console reading. AI output can be incorrect or incomplete, so it should be checked against the original problem statement, constraints, and test cases.

### 8. DSA Progress Dashboard

The progress dashboard presents the learner's current-level progress, including questions solved, questions remaining, and the level-completion percentage. It also shows difficulty-wise progress across the active question bank for Easy, Medium, and Hard problems.

The figures depend on the available question-bank records and the user's recorded solving progress. Marking a problem as solved updates the stored progress used by the dashboard.

### 9. AI-Generated DSA Contest

The AI Contest module generates a three-question practice contest using the learner's current DSA level and recorded solved-question profile. The profile is intended to include the total recorded solved count, difficulty distribution, practice topics/data structures, and solved patterns so the generated contest can be more relevant to the learner.

For each suggested question, the console displays the title, platform, allocated time, and problem URL. It also shows the total estimated contest time. Question selection, time estimates, and URLs are generated by AI and are not independently verified by the application; the learner should confirm the problem link and suitability before starting.

### 10. Daily Study Capacity

The study-capacity module lets the learner set a daily DSA study limit in minutes. The configured capacity is used by the daily task planning flow to determine how much question-solving work can be scheduled for the day. Enter the number of minutes as a number, without adding a unit such as `min`.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java (Java 21 compatibility) | Application logic and console interface |
| Apache Maven | Project build and dependency management |
| MySQL | Persistent user, question-bank, and progress data |
| JDBC | Java-to-MySQL connectivity |
| Groq API | AI-assisted explanations and contest generation |
| Jackson | JSON handling for API requests and responses |
| Argon2id | Password hashing |

## Project Structure

The main source code is under `src/main/java/com/omkar/dsamentor/`.

```text
src/main/java/com/omkar/dsamentor/
├── Main.java
├── model/          # User, DSA problem, progress, and contest models
├── repository/     # Database access
├── rules/          # Problem-selection rules
├── service/        # Daily tasks, progress, AI mentor, and contest logic
├── ui/             # Account, login, menus, task, progress, and contest screens
├── util/           # Password hashing and supporting utilities
└── prompts.txt     # AI prompt material
```

## Prerequisites

Install and configure the following before running the application:

- JDK 21 or newer, with Java 21 compatibility available to Maven.
- Apache Maven.
- A running MySQL server.
- A MySQL database named `dsamentor`, with the tables and question-bank data expected by the application.
- A Groq API key for AI features, available from the [Groq Console](https://console.groq.com/).

## Setup and Run

### 1. Clone the repository

```bash
git clone https://github.com/omkar4641/LogicLadder-AI.git
cd LogicLadder-AI
```

### 2. Configure environment variables

Configure the database credentials and Groq API key described in [Environment Variables](#environment-variables). Do not put real credentials directly in Java source files or commit them to Git.

### 3. Build the project

```bash
mvn clean compile
```

### 4. Run the application

```bash
mvn exec:java "-Dexec.mainClass=com.omkar.dsamentor.Main"
```

Use the console menus to create an account or log in, complete onboarding when required, review daily tasks and progress, set study capacity, and open AI assistance or the AI contest.

## Environment Variables

The application reads its database connection details and Groq API key from environment variables.

| Variable | Value |
|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/dsamentor` |
| `DB_USER` | Your MySQL username |
| `DB_PASSWORD` | Your MySQL password |
| `GROQ_API_KEY` | Your Groq API key |

### Temporary PowerShell configuration

Replace the placeholder values with your own credentials. These variables apply only to the current PowerShell session.

```powershell
$env:DB_URL = "jdbc:mysql://localhost:3306/dsamentor"
$env:DB_USER = "your_mysql_username"
$env:DB_PASSWORD = "your_mysql_password"
$env:GROQ_API_KEY = "your_groq_api_key"
```

For persistent configuration on Windows, add the variables under **User variables** in **Environment Variables**, then open a new terminal so the new session can read them.

## Database Setup Note

The application expects a populated MySQL database named `dsamentor`. The current repository does not include a complete database schema and seed-data script. A new developer must create or import the required tables and question-bank records before all database-backed features can work. This is an important setup dependency for anyone running the project on a fresh machine.

## Security Notes

- Never commit API keys, database passwords, `.env` files, or other secrets.
- The `.gitignore` file excludes `.env` files and Maven's `target/` build output.
- Configure `DB_URL`, `DB_USER`, `DB_PASSWORD`, and `GROQ_API_KEY` through environment variables.
- Use a MySQL account with only the permissions required by the application.
- If a credential has ever been committed or shared, rotate it. Removing it from the latest version alone may not remove it from Git history.

## Current Limitations

- The interface is console-based; this is not currently a web application.
- Daily task selection and progress depend on the MySQL question bank and recorded user progress being configured correctly.
- AI-generated explanations, contest questions, time estimates, and URLs are not independently validated by the application.
- A complete database schema and seed-data script are not included in the repository yet.

## Project Status

LogicLadder AI is an actively developing learning project. Its current focus is a Java/Maven foundation, a MySQL-backed learning workflow, progress reporting, and Groq-powered DSA assistance and contest generation. Future improvements can include reproducible database setup, additional automated tests, and further improvements to the learning experience.
