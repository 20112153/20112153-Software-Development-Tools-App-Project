# Gym Class Manager (Kotlin)

A small Kotlin command-line application for managing gym members, class sessions and bookings. This is the **starting framework**, not a completed assignment. It is based on the layering approach of the course Notes App, but uses a different domain and an explicit many-to-many relationship.

## Current features (v0.1.0 starter)

- In-memory CRUD for members and class sessions in `GymService`.
- Many-to-many bookings: one member can book many classes and one class can have many members.
- Create/cancel bookings, reject duplicate bookings, enforce class capacity and clean up bookings when linked records are deleted.
- Basic console entry point to add and view records and create bookings.
- JUnit 5 tests covering normal paths and errors.
- GitHub Actions for Gradle build, tests, structural verification, JaCoCo coverage and deletion of merged feature branches.

**Not implemented yet:** full console CRUD menus, filtering, reports, JSON saving/loading, and final UX improvements. Track these as separate GitHub issues and pull requests.

## Project layout

```text
src/main/kotlin/
  Main.kt                   # simple console UI
  model/                    # Member, ClassSession, Booking
  service/GymService.kt     # domain rules and CRUD
  utils/UserInput.kt        # console input handling
src/test/kotlin/service/    # JUnit tests
.github/workflows/gradle.yml
build.gradle.kts
```

## Running and testing

Requires **JDK 21** and **Gradle 9.3.0**. Import the project in IntelliJ IDEA as a Gradle project or use an installed Gradle CLI:

```bash
gradle run
gradle check
gradle jacocoTestReport
```

The coverage report is generated under `build/reports/jacoco/test/html/`. GitHub Actions installs Gradle automatically. The Gradle wrapper from the reference ZIP is not included in this initial GitHub upload: add a generated wrapper with IntelliJ/`gradle wrapper` in a dedicated follow-up issue, or use Gradle 9.3.0 directly.

## Workflow required for the assignment

For every feature: **Issue → feature branch → meaningful commits → pull request with `Closes #issue` → merge → confirm issue closed → delete remote branch**. Use multiple labelled/tagged releases as the application develops, maintain this README, and demonstrate code and AI-assisted development in the submission video.

## Planned iterations

1. Complete interactive menus for member/class/booking CRUD and input validation.
2. Add JSON persistence and test ID continuity after reloading.
3. Add searching, filtering, attendance/capacity reports and edge-case tests.
4. Polish documentation, coverage thresholds and release notes.

## Attribution and AI use

Initial structure draws on the lecturer-provided Notes App example (Kotlin source layout, Gradle and console utilities). The Gym Class Manager domain, models, booking logic and tests are independently adapted. AI assisted with planning and preparing the starter files; generated code should be reviewed, run and explained by the student.
