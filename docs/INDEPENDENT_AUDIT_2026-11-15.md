# Independent assignment audit — 15 November 2026

## Sources examined

1. Original **App – Some Guidance** and **App – Submitting your Work** screenshots, including the 20%-per-category rubric and the warning about failing without GitHub workflow.
2. The **GitHub Workflow** screenshot: Issue → Branch → Pull Request → Merge → Close issue → Delete Branch, followed by local IntelliJ Fetch/Checkout/cleanup instructions.
3. The actual lecturer-supplied **notes-app-4.0.zip** Gradle and Kotlin source tree.
4. The live private GitHub repository, current main source files, Actions runs and downloadable JUnit and JaCoCo artifacts.

## Live verification before this audit iteration

- Main commit: `883ab76132eeaf3dfc434bf7e04eb6776083b1ce`.
- GitHub Actions **Kotlin Build and Tests** run `43718617652`: success.
- JUnit XML artifact: **39 tests; 0 failures, 0 errors, 0 skipped** across 11 test classes.
- JaCoCo HTML artifact: **87% instruction coverage; 73% branch coverage** overall.
- Service: **96% instruction / 85% branch**; UI: **82% / 67%**; persistence: **81% / 53%**.
- The build checks `verifyProjectStructure`, `verifyDocumentation`, JaCoCo and JUnit. This iteration adds real CLI smoke verification and strengthens the gate.
- No GitHub tag refs and no GitHub Release objects were present.
- GitHub returned no confirmed repository collaborator permission for `sdronan`; pending-invite inspection was blocked.
- Source materials did **not** include Gradle lab task sheets, therefore full lab compliance remains **unverified**.

## Rubric-by-rubric assessment (not a mark prediction)

**Functionality (20%):** Full CRUD for `Member`, `ClassSession` and the `Booking(memberId, classId)` relationship, with a safe update/move operation; search, availability filter, occupancy and member-count reports; JSON persistence and validation. The console is intentionally simple. Test empty lists, missing IDs, full classes, duplicate links, save/load and malformed data in the demo.

**Architecture and developer experience (20%):** Data models, service, persistence and UI are separate. Most rules are centralized in `GymService`. A single JSON file avoids unnecessary DB complexity. The relatively long `GymConsole.kt` could be split in future, but splitting it purely for file count would add unnecessary complexity. Avoid claiming zero duplication or perfect architecture.

**GitHub (20%):** Issues, branches, meaningful commits, PRs referencing issues, green checks and remote branch cleanup are visible. Versioned releases are **NOT yet done**; they require the repository owner's authenticated Git account (see `docs/RELEASE_MANUAL.md`). Most iterations occurred in a short time window and should not be presented as weeks of historical development. The lecturer invitation remains unconfirmed.

**Gradle (20%):** Official Gradle wrapper, Kotlin/JUnit setup, GitHub Actions, custom verification tasks and JaCoCo checks. The audit adds `verifyConsoleSmoke` which runs the program end-to-end. *The screenshot requires all actual Gradle lab work*, but those lab instructions were not supplied; this part cannot be certified.

**JUnit (20%):** Existing verified 39 tests and coverage evidence. This audit expands cross-component tests and failure-path JSON cases, and requires 80% instruction/70% branch coverage. Report the *post-audit* figures only after seeing green CI results.

## Original reference versus implementation

The lecturer's Notes App reference has Kotlin `2.3.21`, Gradle wrapper `9.3.0`, JDK toolchain `25`, logging dependencies and Jackson JSON/XML persistence. Our application shares the source layering, Kotlin version and wrapper version, but uses JDK `21` and **Gson JSON only** to reduce dependencies. None of the rubric screenshots explicitly mandates Java 25, Jackson, XML, or those logging libraries. Verify with the actual Gradle lab handouts before deciding whether adaptations are acceptable.

## User actions required

1. Obtain complete Gradle lab specifications and align tasks if necessary.
2. Check `sdronan` in Settings → Collaborators and pending invitations.
3. Create and publish multiple Git tags and GitHub Release objects using the documented manual instructions; the connected GitHub App was denied those permissions.
4. On the student's local machine, Fetch → Checkout `main` → delete stale local feature branches.
5. Download the source ZIP **from the final GitHub repository** and complete the remaining Moodle submission steps independently.

## What this audit cannot claim

No grade is guaranteed; the work was not tested against missing Gradle lab task sheets, the collaborator invitation could not be verified, tags/releases had not been published, and the owner must still personally understand and present the code.

## Final confirmation after independent audit changes

PR #20 / Issue #19 implemented the revised Gradle smoke test, stronger coverage thresholds and extra regression tests. A first PR build failed due a Gradle Kotlin DSL `java.io` import collision; the error was corrected on the feature branch, rerun and **passed before merge**.

The post-merge build on `main` (commit `2ebc8e72088971918785cf66487fc19eca3afe18`, run `43721606288`) succeeded. I downloaded and parsed the final CI artifacts:

| Verified measurement | Value |
| --- | --- |
| JUnit test classes | 13 |
| JUnit test methods | **41** |
| Failures / Errors / Skips | **0 / 0 / 0** |
| Overall JaCoCo instruction coverage | **89%** |
| Overall JaCoCo branch coverage | **75%** |
| Business service instruction/branch | **96% / 85%** |
| Persistence instruction/branch | **93% / 82%** |
| UI instruction/branch | **82% / 67%** |
| Gradle quality gate | >=80% instruction; >=70% branches |
| App console smoke task | **Passed as part of Gradle check** |

GitHub additionally showed all **10 implementation/QA feature issues closed**, their **10 associated PRs merged** and only the `main` remote branch remaining at that checkpoint. Tags, Releases and lecturer access remain unconfirmed/missing as specified above. Individual Gradle lab sheets are still unavailable; a complete compliance guarantee would be unfounded.
