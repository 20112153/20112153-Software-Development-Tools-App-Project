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

Full README, rubric review, AI-use record, submission/video checklist and published GitHub Releases. Tagged from the successful final `main` CI run.

**Note:** The older milestone commits are tagged retrospectively to preserve the genuine chronological source versions, rather than pretending the release tags existed when those commits were first merged.

## GitHub Release publication

The CI-completion workflow automatically pushes **annotated Git tags** for these milestones after successful builds. The connected GitHub Actions token returned HTTP 403 when trying to create GitHub Release objects; create the corresponding releases manually in GitHub from the existing tags, using the milestone notes above. Tags are still grounded in the actual merged commits.
