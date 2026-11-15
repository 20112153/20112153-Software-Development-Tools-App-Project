# Submission Checklist and 10-minute walkthrough plan

The lecturer's submission slide asks for **(a)** one ZIP containing source code *downloaded from the GitHub repository*, and **(b)** a link to a video (maximum 10 minutes) walking through the **codebase and AI development process** — explicitly *no GitHub tours*.

## Before submitting

- [ ] Open the final `main` branch and confirm the latest **Kotlin Build and Tests** GitHub Actions run passed. After the packaging iteration, also verify the `github-source-zip` artifact exists in that run. The independent audit's verified baseline was [run #43721606288](https://github.com/20112153/20112153-Software-Development-Tools-App-Project/actions/runs/43721606288): 41 tests, 89% instruction / 75% branch coverage; check the current run as well.
- [ ] **Create annotated Git tags** `v0.1.0` through `v1.0.0` with your own authorized Git account; use the guarded `scripts/publish-releases.sh` helper (instructions: [Release Guide](RELEASE_MANUAL.md)), or follow the documented commands manually. Verify each tag's commit.
- [ ] **Publish multiple GitHub Releases**: the helper creates them using your own authenticated `gh` CLI; alternatively open **Releases → Draft a new release** and publish each tagged version using notes from `CHANGELOG.md`. Confirm they appear on GitHub; the connected App could not do this itself.
- [ ] In GitHub **Settings → Collaborators / Access**, invite or verify lecturer **sdronan**; the brief specifies Week 8. A pending invitation may require acceptance.
- [ ] Obtain the **original Gradle lab handouts** and check each required lab task is present and executed successfully by GitHub Actions. The rubric screenshot alone is not enough to certify this.
- [ ] Run `./gradlew clean check jacocoTestReport` (Windows: `gradlew.bat`) with JDK 21; confirm both instruction and branch coverage gates pass.
- [ ] Demonstrate CRUD, invalid booking, safe booking move, JSON save/load and at least one report.
- [ ] In IntelliJ, follow the lecturer's final local branch cleanup: **Git → Fetch → Checkout main → delete the old local feature branch**, and check the merged commits remain visible. Remote branch deletion was already automated.
- [ ] Download exactly **one final source ZIP** from GitHub's **Code → Download ZIP** while on `main` (the literal method in the lecturer's submission brief). Alternatively, from the latest successful main Actions run, download `github-source-zip`, extract **only** the contained `gym-class-manager-source.zip`, verify `SOURCE_COMMIT.txt`, and confirm with the lecturer if the Actions-produced GitHub archive is acceptable. Do **not** submit the outer artifact archive, build outputs or an old feature-branch ZIP.
- [ ] Record your own **under-10-minute** code-and-AI-process video using [Video Walkthrough Guide](VIDEO_WALKTHROUGH_GUIDE.md), without a GitHub tour; upload/link it as Moodle requires.
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
