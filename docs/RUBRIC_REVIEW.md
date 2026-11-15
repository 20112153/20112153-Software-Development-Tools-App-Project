# Rubric alignment — project self-check

The lecturer's App brief assigns **20% each** to Functionality, Architecture and DX, GitHub, Gradle, and JUnit. The assessment below maps actual code and workflow evidence to that rubric; it is **not a promised grade**.

| Criterion | Evidence | Remaining checks |
| --- | --- | --- |
| Functionality | Full member/class CRUD and explicit booking Create/Read/Update(move)/Delete; name search, availability filtering, attendance/count reports; validation and JSON persistence | User demo of unusual inputs, deleted links, and restart |
| Architecture / DX | Separate `model`, `service`, `ui`, `persistence`, and `utils`; no separate database, web app, or unnecessary frameworks | Demonstrate menu behavior and separation to lecturer |
| GitHub | Separate issues #1, #3, #5, #7, #9, #11, #13, #15, #17; linked feature PRs and passing CI; merged branches deleted; README/changelog; manual version/tag instructions | **Confirm lecturer `sdronan` invited** and **manually publish milestone tags/releases** (GitHub App lacks permission) |
| Gradle | Gradle 9.3.0 wrapper, automated `check` in Actions; custom `verifyProjectStructure` and `verifyDocumentation` tasks plus JaCoCo reporting/coverage verification | **Review all original Gradle lab tasks**; the supplied rubric excerpt does not enumerate them |
| JUnit | Service, console, booking-rescheduling, snapshot, JSON, reporting and edge-case tests; automated CI and 65% coverage gate | Check latest green CI and discuss representative boundary tests |

## Why it stays simple

A console app demonstrates reliable input handling without a JavaFX/web UI. The booking link makes the many-to-many structure explicit. In-memory service state and one JSON file provide practical storage without database setup.

## Assignment-specific reminders

The brief says failing to use GitHub workflow **throughout** development results in a failing App grade. It requires a ZIP downloaded from GitHub and a <=10-minute recorded code/AI-development walk-through (no GitHub tours). Verify `sdronan` access and any Week 8 timing requirement with the lecturer.

This document cannot certify unknown lab-specific requirements or predict the final grade.
