# Submission Checklist and 10-minute walkthrough plan

The lecturer's submission slide asks for **(a)** one ZIP containing source code *downloaded from the GitHub repository*, and **(b)** a link to a video (maximum 10 minutes) walking through the **codebase and AI development process** — explicitly *no GitHub tours*.

## Before submitting

- [ ] Open the final `main` branch and confirm the latest **Kotlin Build and Tests** GitHub Actions run passed.
- [ ] Confirm GitHub releases `v0.1.0` through `v1.0.0` exist and match the intended commits.
- [ ] In GitHub **Settings → Collaborators / Access**, invite or verify lecturer **sdronan**; the brief specifies Week 8. A pending invitation may require acceptance.
- [ ] Run `./gradlew clean check jacocoTestReport` (Windows: `gradlew.bat`) with JDK 21.
- [ ] Demonstrate CRUD, invalid booking, safe booking move, JSON save/load and at least one report.
- [ ] Download the GitHub **Code → Download ZIP** from the final main branch; do **not** submit a stale earlier archive.
- [ ] Record your own video under 10 minutes and upload/link it as Moodle requires.
- [ ] Submit both source ZIP and video link before the deadline.

## Suggested video structure

| Time (approx.) | What to demonstrate |
| --- | --- |
| 0:00–0:50 | Purpose of the app, Kotlin and console design |
| 0:50–2:15 | `Member`, `ClassSession`, `Booking` models and many-to-many links |
| 2:15–4:10 | `GymService`: CRUD, safe rescheduling, full-class and duplicate rules |
| 4:10–5:30 | Menu interactions and searches/reports |
| 5:30–6:30 | `GymFileStore`, valid JSON reload and malformed-data handling |
| 6:30–8:15 | Show JUnit tests, Gradle extra tasks and JaCoCo coverage in code |
| 8:15–9:40 | Explain AI assistance, how tests checked the output, lessons learned |
| 9:40–10:00 | Summary of strengths and acknowledged limitations |

**Do not spend the video navigating GitHub pages.** Discuss the development workflow and AI process verbally while walking through code and tests, as the brief asks.

## Known manual actions

The connected GitHub tools can verify repository commits, issues and PRs, but do not expose an authorized collaborator-invitation action. The student must check/invite `sdronan` in GitHub settings. The student must also record/upload the video, and check any lab specifications not provided in the assignment excerpt.
