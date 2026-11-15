# Gym Class Manager (Kotlin)

A small Kotlin command-line application for managing gym members, class sessions and bookings. It follows the layered structure of the course Notes App, with its own domain and a many-to-many relationship.

## Current features

- Member menu: add, list, update and delete members.
- Class menu: add, list, update and delete classes, including booked/available capacity.
- Booking menu: create, list and cancel class bookings.
- Booking constraints in `GymService`: no duplicate bookings, no overbooking, valid references only, and automatic cleanup of bookings when members or classes are deleted.
- Basic input validation, friendly messages, and confirmation before deleting members or classes.
- JUnit 5 tests for business logic and automated console interaction.
- Gradle checks, JaCoCo coverage reporting, GitHub Actions and merged-feature-branch cleanup.

**Current limitation:** All data is held **in memory** and is lost when the program exits. This is an intentionally rudimentary console interface, not the final assignment.

## Menu

```text
Gym Class Manager
1. Members     -> Add / List / Update / Delete
2. Classes     -> Add / List / Update / Delete
3. Bookings    -> Book class / List bookings / Cancel booking
0. Exit
```

## Project layout

```text
src/main/kotlin/
  Main.kt                   # starts the console application
  ui/GymConsole.kt          # menu navigation and input/output
  model/                    # Member, ClassSession, Booking
  service/GymService.kt     # data operations and domain rules
  utils/UserInput.kt        # console input helper
src/test/kotlin/
  service/GymServiceTest.kt # service and edge-case tests
  ui/GymConsoleTest.kt      # menu interaction tests
.github/workflows/          # build/test and branch-cleanup workflows
build.gradle.kts
```

## Running and testing

Requires JDK 21 and Gradle 9.3.0. Open in IntelliJ as a Gradle project, or run:

```bash
gradle run
gradle check
gradle jacocoTestReport
```

Coverage is generated in `build/reports/jacoco/test/html/`. GitHub Actions installs Gradle automatically. The project does not yet contain a Gradle wrapper, which can be added in a separate issue.

## Assignment GitHub workflow

For each change: **create an issue → feature branch → meaningful commit(s) → PR referencing the issue → pass CI → merge → confirm issue closed → delete the merged branch**. Tag multiple releases during development and keep the README up to date.

## Planned next iterations

1. Add JSON save/load and tests for correct ID generation after reloading.
2. Add search/filter options and useful capacity/attendance reports.
3. Further improve validation, automated test coverage, Gradle tasks and documentation.
4. Prepare tagged releases and the short submission video explaining the code and AI-assisted workflow.

## Attribution and AI use

Project structure is inspired by the lecturer-provided Notes App example. The gym domain, many-to-many booking logic, menus and tests are adapted specifically for this project. AI assisted in preparing the code and testing plan; the student should review, test and be able to explain the implementation.

## Iteration: booking updates

The booking submenu also supports **Move booking** (option 4). A move is validated before changing the original booking, so full or missing destination classes and duplicate bookings do not lose data. The corresponding service and console tests are included.
