# Gym Class Manager

A Kotlin console application for managing **gym members, class sessions and bookings**. Built as a second-year Software Development Tools assignment using the layered architecture from the course Notes App as a reference.

## Features

- **Members:** add, list, update and delete.
- **Classes:** add, list, update and delete, with capacity validation.
- **Bookings (many-to-many):** create, list, move to a different class and cancel. A member may book several classes; a class may contain several members. Invalid and duplicate bookings, full classes and unsafe moves are rejected.
- **Search/filter:** find members and classes by case-insensitive name; find classes with spaces.
- **Reports:** booked/capacity/free counts per class and booking totals per member.
- **JSON storage:** loads on startup, saves on normal exit, and offers manual Save/Load. Saved files preserve next-ID counters; corrupt files are rejected without changing the current in-memory data. Startup will not overwrite an invalid existing save.

Data is stored locally at `data/gym-data.json`. It is not an online or multi-user application.

## Run

Requires **JDK 21**. The Gradle 9.3.0 wrapper is included.

```bash
./gradlew run
./gradlew check
./gradlew jacocoTestReport
```

On Windows, use `gradlew.bat run` / `gradlew.bat check`. You can also import the project as a Gradle project in IntelliJ IDEA.

### Menu

```text
1. Members          Add / List / Update / Delete
2. Classes          Add / List / Update / Delete
3. Bookings         Book / List / Cancel / Move
4. Save             Save JSON now
5. Load             Load JSON now
6. Search / Reports Search names, free spaces, attendance figures
0. Exit             Automatically saves JSON
```

## How the code is organised

| Location | Responsibility |
| --- | --- |
| `model/` | `Member`, `ClassSession` and `Booking` data classes |
| `service/GymService.kt` | Business rules, full CRUD, search/filter/reports |
| `service/GymSnapshot.kt` | Validated state that can be restored from a file |
| `service/GymReports.kt` | Derived class/member reports |
| `persistence/GymFileStore.kt` | JSON save/load with temporary-file replacement |
| `ui/GymConsole.kt` | Console input/output and menus |
| `utils/UserInput.kt` | Simple console input helpers |
| `src/test/kotlin/` | JUnit tests for service, menus, persistence and edge cases |

The data layer and rules are separate from the console menu. Booking links are represented by `Booking(memberId, classId)`, creating the required many-to-many relationship.

## Testing and automation

`./gradlew check` runs JUnit and the additional tasks **verifyProjectStructure**, **verifyDocumentation**, and **jacocoTestCoverageVerification**. The coverage gate is **65% minimum**; `jacocoTestReport` generates HTML/XML reports under `build/reports/jacoco/test/`. GitHub Actions runs the same wrapper and uploads test/coverage artifacts on every PR and push to `main`.

Tests cover CRUD, missing/invalid IDs, capacity limits, duplicate bookings, moving a booking, JSON reload and counter continuity, malformed JSON, safe console input and searches/reports.

## GitHub workflow and releases

Each iteration uses **issue → feature branch → meaningful commit(s) → pull request referencing issue → CI → merge → issue closes → delete branch**. Versions and the corresponding milestone commits are documented in [CHANGELOG.md](CHANGELOG.md); GitHub Releases provides tagged source snapshots.

## Submission and AI assistance

Read [Submission Checklist](docs/SUBMISSION_CHECKLIST.md), [Rubric Review](docs/RUBRIC_REVIEW.md) and [AI Development Record](docs/AI_DEVELOPMENT.md). The submission instructions require a **GitHub-downloaded ZIP** and a **video of no more than 10 minutes** walking through the code and AI development process (not a GitHub tour). Confirm lecturer `sdronan` has repository access.

## Known limits

This is intentionally a **console application** with local JSON, not a GUI or database. It does not include an attendance date/time scheduler, authentication or multi-user synchronization. The supplied assignment excerpt does not include every Gradle lab task, so those lab-specific requirements must be checked separately.

## Attribution

The lecturer-supplied `notes-app-4.0.zip` was used as an architectural example. The gym domain, models, booking/service rules and reports were developed for this project. AI assisted with project planning, source preparation, test design, code review and GitHub workflow execution; see the AI development record for details.
