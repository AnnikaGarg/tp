# TP — Project Context

## Overview
This is the **team project (TP)** repository, cloned from [https://github.com/TrijalSrimal/tp](https://github.com/TrijalSrimal/tp). It is based on the **Duke** greenfield Java project template (named after the Java mascot).

## Tech Stack
- **Language:** Java (JDK 17)
- **Build Tool:** Gradle
- **Testing:** JUnit + I/O redirection (text-ui-test)
- **CI:** GitHub Actions
- **Code Quality:** Checkstyle

## Project Structure
```
TP/
├── src/
│   ├── main/java/seedu/duke/Duke.java    # Main entry point
│   └── test/java/seedu/duke/DukeTest.java # JUnit test skeleton
├── docs/                                  # Project documentation
├── config/                                # Checkstyle config
├── text-ui-test/                          # I/O redirection tests
├── build.gradle                           # Gradle build script
├── gradlew / gradlew.bat                  # Gradle wrapper
├── .github/                               # GitHub Actions CI
├── README.md
└── CONTRIBUTORS.md
```

## Change Log
| Date       | Change Description                     |
|------------|----------------------------------------|
| 2026-03-06 | Initial clone of the repository.       |
| 2026-03-06 | Ran code (`gradlew run`) — compiles & runs OK. Duke ASCII art prints, reads user input. |
| 2026-03-06 | I/O redirection tests (`text-ui-test/runtest.sh`) — **PASSED**. |
| 2026-03-06 | Checkstyle (`gradlew checkstyleMain checkstyleTest`) — **PASSED**, no violations. |
| 2026-03-06 | Created and switched to branch `Trijal-AboutUs`. |
