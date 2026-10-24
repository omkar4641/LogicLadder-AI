LogicLadder AI
Your personal AI-guided journey to mastering Data Structures and Algorithms.

LogicLadder AI is a Java-based, console-driven DSA learning platform that helps learners organize daily practice, track their progress, and learn problem-solving approaches with AI assistance. It combines a structured question bank with personalized task planning and AI-generated coding contests powered by Groq.
<p>
  <img alt="Java" src="https://img.shields.io/badge/Java-21%2B-ED8B00?logo=openjdk&logoColor=white" />
  <img alt="Maven" src="https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven&logoColor=white" />
  <img alt="MySQL" src="https://img.shields.io/badge/Database-MySQL-4479A1?logo=mysql&logoColor=white" />
  <img alt="Groq" src="https://img.shields.io/badge/AI-Groq-F55036" />
</p>

Contents
- Overview
- Features
- Technology Stack
- Application Structure
- Prerequisites
- Setup and Run
- Environment Variables
- Database Setup Note
- Security Notes
- Limitations
Overview
Learning DSA consistently can be difficult without a clear plan or feedback on progress. LogicLadder AI brings common study activities together in one place: selecting daily questions, tracking completed problems, reviewing progress, and getting explanations from an AI mentor.

The application currently runs as a Java console application. It uses MySQL to store and retrieve user and problem-related data, and calls the Groq API for AI-assisted learning and contest generation.
Features
Account creation and login
- Create an account and sign in with a username and password.
- Passwords are processed using Argon2id hashing rather than being stored as plain text.
- User-specific learning information supports personalized use of the application.
Personalized onboarding and study capacity
- Choose a DSA learning level during onboarding.
- Set a daily study-time capacity in minutes.
- Use the configured capacity to help plan the day's DSA work.
Today's DSA Tasks
- View questions selected for the user's current learning level.
- Review problem titles, topics, stages, difficulty, estimated solving time, platform, and problem links.
- Open available learning-resource links.
- Mark questions as solved to update recorded progress.
DSA progress dashboard
- See progress for the current learning level.
- View questions solved, questions remaining, and level-completion percentage.
- Review difficulty-wise progress across the active question bank.
AI DSA Mentor
The AI assistance flow is designed to explain problem-solving instead of simply handing over a complete solution. Available assistance modes include:
- Explain and dry run: understand the problem and walk through an example.
- Brute-force approach: explore a straightforward approach, its decisions, and complexity.
- Optimal approach: understand the more efficient strategy, with a detailed dry run and complexity analysis.
AI responses may vary and should be checked against the original problem statement and expected constraints.
AI-generated DSA contest
- Generate a three-question contest using the learner's current DSA level and recorded solved-question profile.
- The learner profile is intended to account for solved-question counts, difficulty distribution, practice areas/topics, and recorded patterns.
- Display each suggested question's title, platform, estimated time, and problem URL.
- Show the total estimated contest time before the learner begins.
Contest questions, time estimates, and links are AI-generated; the application does not independently verify that every suggested link is live or that each question is perfectly calibrated to the learner.
Technology Stack
Technology	Purpose
Java (Java 21 language target)	Application logic and console UI
Maven	Dependency management and build
MySQL	Persistent user, question-bank, and progress data
JDBC	Java-to-MySQL connectivity
Groq API	AI-assisted explanations and contest generation
Jackson	JSON handling for API requests and responses
Argon2id	Password hashing


Application Structure
The main source code is under src/main/java/com/omkar/dsamentor/.
src/main/java/com/omkar/dsamentor/
├── Main.java
├── model/          # User, DSA problem, progress, and contest models
├── repository/     # Database access
├── service/        # Daily tasks, progress, AI mentor, and AI contest logic
├── ui/             # Account, login, menus, task, progress, and contest screens
├── util/           # Utilities such as password hashing
├── prompts.txt     # AI prompt material
└── rules/
    └── problemSelectionRules.txt
Prerequisites
Install and configure:
- JDK 21 or newer, with Java 21 compatibility available to Maven.
- Apache Maven.
- A running MySQL server.
- A MySQL database named dsamentor with the tables and question data expected by the application.
- A Groq API key for AI features. Create one from the Groq Console.
Setup and Run
1. Clone the repository
git clone https://github.com/omkar4641/LogicLadder-AI.git
cd LogicLadder-AI
2. Configure environment variables
The application reads database credentials and the Groq API key from environment variables. Do not put real credentials directly in Java files or commit them to Git.

Set these variables in your operating system:
Variable	Example / value
DB_URL	jdbc:mysql://localhost:3306/dsamentor
DB_USER	Your MySQL username
DB_PASSWORD	Your MySQL password
GROQ_API_KEY	Your Groq API key


For a temporary PowerShell session, set them before running the application (replace the placeholders with your own values):
$env:DB_URL = "jdbc:mysql://localhost:3306/dsamentor"
$env:DB_USER = "your_mysql_username"
$env:DB_PASSWORD = "your_mysql_password"
$env:GROQ_API_KEY = "your_groq_api_key"
These PowerShell values apply only to that terminal session. For persistent configuration on Windows, add the variables under User variables in Windows Environment Variables, then open a new terminal.
3. Build the project
mvn clean compile
4. Run the application
mvn exec:java "-Dexec.mainClass=com.omkar.dsamentor.Main"
Use the console menu to create an account or log in, configure your study capacity, review daily tasks and progress, and open the AI contest or AI assistance flows.
Database Setup Note
The application expects a populated dsamentor MySQL database. The repository currently does not provide a complete database schema and seed-data script, so a new user cloning the project must create/import the required tables and question-bank records separately before all database-backed features will work. This is an important setup dependency for anyone running the project on a fresh machine.
Security Notes
- Never commit API keys, database passwords, .env files, or other secrets.
- The project .gitignore excludes .env files and Maven's target/ build output.
- Set DB_USER, DB_PASSWORD, DB_URL, and GROQ_API_KEY through environment variables.
- Use a database account with only the permissions the application requires.
- If a credential has ever been committed or shared, rotate it; deleting it from the latest version alone may not remove it from Git history.
Limitations
- The interface is currently console-based; it is not a web application.
- Daily task selection and progress depend on the MySQL question bank and recorded user progress being set up correctly.
- AI-generated contest suggestions, estimates, and URLs are not independently validated by the application.
- Database schema and seed scripts are not yet included in this repository.
Project Status
LogicLadder AI is an actively developing learning project. Its current focus is a working Java/Maven foundation, MySQL-backed learning workflow, progress reporting, and Groq-powered DSA assistance and contest generation. Future improvements can include making database setup reproducible, expanding testing, and improving the learning experience.