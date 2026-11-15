# 9-minute code and AI-process video walkthrough

The lecturer asks for a video **no longer than 10 minutes**, explaining the *codebase and AI development process*. **Do not do a GitHub tour.** Record the actual working program, source files, Gradle configuration and representative tests. Speak in your own words and only say you personally checked something if you actually did.

## Before recording

1. Open the latest `main` clone in IntelliJ IDEA with JDK 21.
2. Make sure `gradlew.bat check` succeeds (or `./gradlew check` in Git Bash).
3. In a new clone or after securely backing up `data/gym-data.json`, start with no existing saved data so IDs begin at 1.
4. Have these files ready in the IntelliJ tabs: `Main.kt`, the three `model` files, `GymService.kt`, `GymConsole.kt`, `GymFileStore.kt`, `build.gradle.kts`, `BookingRescheduleTest.kt`, and `ConsoleStorageRecoveryTest.kt`.
5. Use a screen recorder that captures readable text and your spoken explanation. Rehearse once and aim for **8:30–9:30** to stay safely under the 10-minute maximum.

## Time-coded speaking and demonstration guide

| Time | Show | What to explain in plain language |
| --- | --- | --- |
| **0:00–0:40** | `Main.kt` and the terminal main menu | "This is a Gym Class Manager in Kotlin. I used the Notes App as an architectural reference, but the data and booking functionality are designed for this application." |
| **0:40–1:50** | `Member.kt`, `ClassSession.kt`, `Booking.kt` | "A member can attend/book several classes and one class can have several members. The `Booking` model connects the two IDs, which is the many-to-many relationship." |
| **1:50–3:10** | `GymService.kt` | Briefly show add, update, delete, create booking, cancel and reschedule. Point to duplicate and capacity checks. Explain why failed moves preserve the original booking. |
| **3:10–4:50** | Actual console demo | Create two members and two classes, trigger the full-class error, move a booking, then open the available-spaces filter and class booking totals. Explain that these are *bookings*, not verified attendance. |
| **4:50–5:50** | `GymFileStore.kt`, JSON and restart | Save/exit, show the simple JSON file, restart and demonstrate that bookings remain. Explain that imported data is checked before replacing live records and unique IDs are maintained. |
| **5:50–7:05** | JUnit tests | Show one normal booking test, one duplicate/capacity test, a bad JSON test and the 500-operation invariant test. Mention what tests caught or guarded against, not imaginary bugs. |
| **7:05–8:10** | `build.gradle.kts` and terminal `gradlew.bat check` result | Show JUnit, JaCoCo and the custom `verifyConsoleSmoke`, `verifyDocumentation` and `verifyProjectStructure` tasks. The thresholds are 90% instruction and 80% branch coverage. |
| **8:10–9:10** | `docs/AI_DEVELOPMENT.md` and one source file | "AI helped with idea evaluation, initial structures, test examples and GitHub actions. I reviewed outputs, used the JUnit/Gradle checks, fixed failures and kept issue/PR history. I can explain the code rather than blindly copying generated output." |
| **9:10–9:30** | Main menu or one report | Summarize the three main outcomes: m:n CRUD, searching/filtering/reporting, and automated validation. Note the simple console UI and local JSON as reasonable scope limits. |

## Small deterministic console demo

Use an empty save file in a *fresh clone* (or back up any existing save first).

1. Members → Add **Alex**, then **Sam** (member IDs 1 and 2).
2. Classes → Add **Yoga** with capacity **1**; add **Spin** with capacity **2** (class IDs 1 and 2).
3. Bookings → Book Alex (1) into Yoga (1); attempt Sam (2) into Yoga (1) to show **Class is full**.
4. Book Sam (2) into Spin (2); move Alex (1) from Yoga (1) to Spin (2).
5. Search / Reports → **Available classes** displays Yoga with one free place; **Class booking totals** shows Yoga 0/1 and Spin 2/2.
6. Save and Exit; run again; list bookings to show both members still in Spin. Don't enter a real person's details.

## Presenter notes

- Avoid saying you created multiple versions across weeks: the GitHub history shows development iterations on 15 November 2026.
- Do not claim the lecturer has access, tagged Releases are published or all Gradle labs are implemented unless you independently verify those items.
- You do **not** need to tour the GitHub website. Explain the issue → branch → PR → merge approach verbally while reviewing code, as required.
- Do not submit this guide as the recorded video; the lecturer explicitly requests **your own** code and AI-process walkthrough.
