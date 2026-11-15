# Changelog — actual development milestones

Version tags point to merged commits, not arbitrary source copies. The assignment's required issue / PR history is retained in GitHub.

## v0.1.0 — Foundation
**Commit:** `2501ca0d95256041da4cf7e4e6b2c09b38034760`  
**Issue:** #1 · **PR:** #2

Starter Kotlin/Gradle project: original gym data models, service rules, booking links, console entry point and first JUnit/CI checks.

## v0.2.0 — Booking CRUD and JSON
**Commit:** `77d3351b3efd7a57c601153ed7f41d15d9ff0991`  
**Issues:** #3, #5, #7 · **PRs:** #4, #6, #8

Full member/class console menus, safe booking rescheduling and readable JSON storage with validation and ID continuity.

## v0.3.0 — Searching and reports
**Commit:** `9a2cdacc3869fabcec6911480ad2aaeb28110c77`  
**Issue:** #9 · **PR:** #10

Case-insensitive searching, available-space filtering and class/member booking summaries.

## v0.4.0 — Gradle/JUnit quality
**Commit:** `098c4b06639876b723b73a256aee4d9cda9afce5`  
**Issue:** #11 · **PR:** #12

Generated official Gradle wrapper; checked-in quality tasks and coverage gate; regression testing and persistence safeguards.

## v1.0.0 — Documentation and submission-ready code
**Issue:** #13 · **PR:** linked from issue

Full README, rubric review, AI-use record and submission/video checklist. Version `v1.0.0` should be tagged only after the final merged `main` code and tests are verified.

**Note:** Tag the historical merged milestone commits retrospectively using the instructions below; do not imply that these tags existed when the work was first merged.

## GitHub tag and Release publication — manual action required

**Not yet published through this connection.** Workflow attempts failed: GitHub Release API returned HTTP 403 (run #43717605822); annotated Git tag push was rejected because the installed GitHub App lacks `workflows` permission (run #43718022229). These are authorization restrictions, not app test failures.

The repository owner should create the four historical tags at the exact merge commits above, then `v1.0.0` at the latest verified `main` commit, using [docs/RELEASE_MANUAL.md](docs/RELEASE_MANUAL.md). Publish GitHub Releases from those tags with the milestone notes above.
