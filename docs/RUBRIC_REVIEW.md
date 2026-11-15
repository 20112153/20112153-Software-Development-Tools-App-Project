# Rubric alignment — project self-check

The lecturer's App brief assigns **20% each** to Functionality, Architecture and DX, GitHub, Gradle, and JUnit. The assessment below maps actual code and workflow evidence to that rubric; it is **not a promised grade**.

| Criterion | Evidence | Remaining checks |
| --- | --- | --- |
| Functionality | Full member/class CRUD and explicit booking Create/Read/Update(move)/Delete; name search, availability filtering, attendance/count reports; validation and JSON persistence | User demo of unusual inputs, deleted links, and restart |
| Architecture / DX | Separate `model`, `service`, `ui`, `persistence`, and `utils`; no separate database, web app, or unnecessary frameworks | Demonstrate menu behavior and separation to lecturer |
| GitHub | Separate issues #1, #3, #5, #7, #9, #11, #13, #15, #17; linked feature PRs and passing CI; merged remote branches deleted; README/changelog and manual tag instructions | **Confirm lecturer `sdronan` invited** and **manually publish milestone tags/releases** (GitHub App lacks permission) |
| Gradle | Gradle 9.3.0 wrapper, automated `check` in Actions; custom `verifyProjectStructure`, `verifyDocumentation`, and `verifyConsoleSmoke` tasks plus JaCoCo reporting/coverage verification | **Review all original Gradle lab tasks**; the supplied rubric excerpt does not enumerate them |
| JUnit | Service, console, booking-rescheduling, snapshot, JSON, reporting and edge-case tests; automated CI and minimum 80% instruction / 70% branch coverage gate | Check latest green CI and discuss representative boundary tests |

## Why it stays simple

A console app demonstrates reliable input handling without a JavaFX/web UI. The booking link makes the many-to-many structure explicit. In-memory service state and one JSON file provide practical storage without database setup.

## Assignment-specific reminders

The brief says failing to use GitHub workflow **throughout** development results in a failing App grade. It requires a ZIP downloaded from GitHub and a <=10-minute recorded code/AI-development walk-through (no GitHub tours). Verify `sdronan` access and any Week 8 timing requirement with the lecturer.

This document cannot certify unknown lab-specific requirements or predict the final grade.

## Important distinction: source rubric vs. verified work

The actual assignment screenshot explicitly states: **Baseline** full CRUD on many-to-many relationships, **Good** usable domain reports and sensible UX, **Excellent** reliable search/filter/reporting including errors/edge cases. The architecture Excellent column demands clear separation of concerns, DRY, readability and minimal unnecessary complexity. The GitHub Excellent column asks for **multiple tagged releases** and well-defined iterations plus an excellent README. Gradle requires **all lab tasks working in Actions** and additional tasks for higher bands. JUnit requires meaningful coverage including error/edge cases.

In the latest *pre-audit* successful CI run (#43718617652), there were **39 passing JUnit tests**, with **87% instruction** and **73% branch** coverage (downloaded artifacts). The newer test figures should be confirmed after this audit PR merges.

**Not verified:** no tagged releases existed at audit time; `sdronan` permission was not confirmed; the individual Gradle lab handouts were unavailable, so complete Gradle lab compliance is *unknown*. The original Notes App's Gradle file uses Kotlin plugin `2.3.21` and JVM 25; our project uses the same Kotlin plugin but JDK 21. The screenshot does not specify a required JDK, so don't claim either configuration proves lab alignment. The original GitHub workflow screenshot also includes local IntelliJ Fetch → Checkout main → Delete local feature branch cleanup, which is an owner/IDE action and cannot be verified through remote API alone.
