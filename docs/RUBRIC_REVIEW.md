# Rubric alignment — project self-check

The lecturer's App brief assigns **20% each** to Functionality, Architecture and DX, GitHub, Gradle, and JUnit. The assessment below maps actual code and workflow evidence to that rubric; it is **not a promised grade**.

| Criterion | Evidence | Remaining checks |
| --- | --- | --- |
| Functionality | Full member/class CRUD and explicit booking Create/Read/Update(move)/Delete; name search, availability filtering, booking-occupancy/count reports; validation and JSON persistence | User demo of unusual inputs, deleted links, and restart |
| Architecture / DX | Separate `model`, `service`, `ui`, `persistence`, and `utils`; no separate database, web app, or unnecessary frameworks | Demonstrate menu behavior and separation to lecturer |
| GitHub | Separate issues #1, #3, #5, #7, #9, #11, #13, #15, #17; linked feature PRs and passing CI; merged remote branches deleted; README/changelog and manual tag instructions | **Confirm lecturer `sdronan` invited** and **manually publish milestone tags/releases** (GitHub App lacks permission) |
| Gradle | Gradle 9.3.0 wrapper, automated `check` in Actions; custom `verifyProjectStructure`, `verifyDocumentation`, and `verifyConsoleSmoke` tasks plus JaCoCo reporting/coverage verification | **Review all original Gradle lab tasks**; the supplied rubric excerpt does not enumerate them |
| JUnit | Service, console, booking-rescheduling, snapshot, JSON, reporting and error-path tests; 41 passing JUnit tests and 89% instruction / 75% branch coverage on final verified main run #43721606288; enforced 80% / 70% gates | Check latest green CI and discuss representative boundary tests |

## Why it stays simple

A console app demonstrates reliable input handling without a JavaFX/web UI. The booking link makes the many-to-many structure explicit. In-memory service state and one JSON file provide practical storage without database setup.

## Assignment-specific reminders

The brief says failing to use GitHub workflow **throughout** development results in a failing App grade. It requires a ZIP downloaded from GitHub and a <=10-minute recorded code/AI-development walk-through (no GitHub tours). Verify `sdronan` access and any Week 8 timing requirement with the lecturer.

This document cannot certify unknown lab-specific requirements or predict the final grade.

## Important distinction: source rubric vs. verified work

The actual assignment screenshot explicitly states: **Baseline** full CRUD on many-to-many relationships, **Good** usable domain reports and sensible UX, **Excellent** reliable search/filter/reporting including errors/edge cases. The architecture Excellent column demands clear separation of concerns, DRY, readability and minimal unnecessary complexity. The GitHub Excellent column asks for **multiple tagged releases** and well-defined iterations plus an excellent README. Gradle requires **all lab tasks working in Actions** and additional tasks for higher bands. JUnit requires meaningful coverage including error/edge cases.

In the latest *pre-audit* successful CI run (#43718617652), there were **39 passing JUnit tests**, with **87% instruction** and **73% branch** coverage (downloaded artifacts). The newer test figures should be confirmed after this audit PR merges.

**Not verified:** no tagged releases existed at audit time; `sdronan` permission was not confirmed; the individual Gradle lab handouts were unavailable, so complete Gradle lab compliance is *unknown*. The original Notes App's Gradle file uses Kotlin plugin `2.3.21` and JVM 25; our project uses the same Kotlin plugin but JDK 21. The screenshot does not specify a required JDK, so don't claim either configuration proves lab alignment. The original GitHub workflow screenshot also includes local IntelliJ Fetch → Checkout main → Delete local feature branch cleanup, which is an owner/IDE action and cannot be verified through remote API alone.

## Final verified evidence (after audit PR #20)

- Build on `main` at merge commit `2ebc8e72088971918785cf66487fc19eca3afe18`, [GitHub Actions run #43721606288](https://github.com/20112153/20112153-Software-Development-Tools-App-Project/actions/runs/43721606288): **successful**.
- Downloaded JUnit XML: **41 tests across 13 classes; 0 failures, 0 errors, 0 skips**.
- Downloaded JaCoCo HTML: **89% instruction and 75% branch coverage overall**; service **96% / 85%**, persistence **93% / 82%**, UI **82% / 67%**. Console entry point and raw input helpers have low direct-JUnit coverage but the custom Gradle `verifyConsoleSmoke` exercises the actual entry point in CI.
- GitHub history before this documentation update: **10 closed implementation/QA issues, 10 merged PRs, only `main` remote branch**. Each PR references its issue; all feature/fix remote branches were cleaned up.
- Zero Git tags/Releases; no verified `sdronan` collaborator access; no full Gradle lab handout to compare. Do **not** claim these are complete.

This subsection describes a verified historical commit and CI run; a later documentation-only commit does not alter the measured application tests or functional logic, but its own CI must also pass.

## Final regression evidence (PR #24)

An additional issue/feature-branch/PR iteration implemented **9 more JUnit tests** (5 console validation/error paths, 3 manual JSON recovery flows, 1 seeded 500-operation relationship invariant test) plus the GitHub test-to-rubric matrix. The PR's downloaded artifacts show **50/50 passed, 91% JaCoCo instruction and 80% branch coverage**. The Gradle gate has been strengthened to 90%/80%; check the newest post-merge CI run. User-facing reports now explicitly represent *bookings/occupancy* rather than actual physical attendance.

**Remaining hard requirements are unchanged:** create multiple annotated Git tags/GitHub Releases, verify lecturer `sdronan` access, compare against the missing Gradle lab instructions, and prepare the GitHub-source ZIP and student-recorded 10-minute video. Avoid presenting the repository as fully submission-ready until those are addressed.
