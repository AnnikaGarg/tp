# SpendSwift

SpendSwift is a **Command Line Interface (CLI) expense tracker** built for university students who prefer fast, keyboard-driven workflows over traditional GUI budgeting apps.

Log expenses, set budgets, search and filter your spending history, track loans, and view category-level statistics — all without leaving the terminal.

## Setting up in IntelliJ

Prerequisites: JDK 17 (use the exact version), update IntelliJ to the most recent version.

1. **Ensure IntelliJ JDK 17 is defined as an SDK**, as described [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk) -- this step is not needed if you have used JDK 17 in a previous IntelliJ project.
1. **Import the project _as a Gradle project_**, as described [here](https://se-education.org/guides/tutorials/intellijImportGradleProject.html).
1. **Verify the setup**: After importing, locate `src/main/java/seedu/duke/SpendSwift.java`, right-click it, and choose `Run SpendSwift.main()`. You should see the SpendSwift welcome message.

## Build automation using Gradle

* This project uses Gradle for build automation and dependency management. It includes a basic build script as well (i.e. the `build.gradle` file).
* If you are new to Gradle, refer to the [Gradle Tutorial at se-education.org/guides](https://se-education.org/guides/tutorials/gradle.html).

## Testing

### JUnit tests

* Run all tests with `./gradlew test`.
* If you are new to JUnit, refer to the [JUnit Tutorial at se-education.org/guides](https://se-education.org/guides/tutorials/junit.html).

### I/O redirection tests

* To run _I/O redirection_ tests (aka _Text UI tests_), navigate to the `text-ui-test` and run the `runtest(.bat/.sh)` script.

## Checkstyle

* A sample CheckStyle rule configuration is provided in this project.
* If you are new to Checkstyle, refer to the [Checkstyle Tutorial at se-education.org/guides](https://se-education.org/guides/tutorials/checkstyle.html).

## CI using GitHub Actions

The project uses [GitHub Actions](https://github.com/features/actions) for CI. When you push a commit to this repo or PR against it, GitHub Actions will run automatically to build and verify the code as updated by the commit/PR.

## Documentation

* [User Guide](docs/UserGuide.md)
* [Developer Guide](docs/DeveloperGuide.md)
