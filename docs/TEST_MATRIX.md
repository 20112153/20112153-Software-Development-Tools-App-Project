# Pre-submission process and regression matrix

Prepared from the **actual App rubric and GitHub Workflow slides**, the original Notes App ZIP, and the current Kotlin application. This is a traceable test inventory, not a guarantee of marks or evidence of any missing Gradle lab handouts.

## Functionality, architecture and edge-case evidence

| Process | Main source | Automated verification | Rubric relevance |
| --- | --- | --- | --- |
| Add / list / update / delete members | `GymService`, `GymConsole` | `GymServiceTest`, `GymConsoleTest`, `ConsoleEdgeCasesTest` | Functionality baseline, reliable UX |
| Add / list / update / delete classes | `GymService`, `GymConsole` | `GymServiceTest`, `GymConsoleTest` | Functionality baseline |
| Reject blank names, invalid IDs and invalid capacity | `GymService`, `GymConsole` | `GymServiceTest`, `ConsoleEdgeCasesTest`, `QualityRegressionTest` | Excellent edge/error cases |
| Prevent deletion without confirmation | `GymConsole` | `GymConsoleTest`, `ConsoleEdgeCasesTest` | UX/error handling |
| Many-to-many member ↔ class links | `Booking`, `GymService` | `GymServiceTest`, `RelationshipInvariantTest` | Full m:n CRUD baseline |
| Book, list, move and cancel bookings | `GymService`, `GymConsole` | `BookingRescheduleTest`, `BookingRescheduleConsoleTest`, `FullWorkflowTest` | Full m:n CRUD baseline |
| Prevent duplicate booking, overbooking, missing member/class | `GymService` | `GymServiceTest`, `BookingRescheduleTest`, `ConsoleEdgeCasesTest` | Excellent error cases |
| Preserve source booking after failed move | `GymService`, `GymConsole` | `BookingRescheduleTest`, `ConsoleEdgeCasesTest` | Excellent reliability |
| Update class capacity below current bookings | `GymService`, `GymConsole` | `QualityRegressionTest`, `ConsoleEdgeCasesTest` | Excellent edge cases |
| Remove dangling links after member/class deletion | `GymService` | `GymServiceTest`, `RelationshipInvariantTest` | m:n integrity |
| Search members/classes case insensitively | `GymService`, `GymConsole` | `SearchReportsTest`, `SearchReportsConsoleTest` | Excellent searching |
| Filter classes with available spaces | `GymService`, `GymConsole` | `SearchReportsTest`, `SearchReportsConsoleTest` | Excellent filtering |
| Per-class booking occupancy and member booking reports | `GymService`, `GymReports` | `SearchReportsTest`, `FullWorkflowTest` | Good/Excellent reporting |
| Save and restore valid JSON | `GymFileStore`, `GymSnapshot` | `GymFileStoreTest`, `PersistenceConsoleTest`, `FullWorkflowTest` | Reliability and maintainable architecture |
| Preserve unique counters after deleted IDs and restart | `GymSnapshot`, `GymService` | `GymSnapshotTest`, `GymFileStoreTest` | Excellent integrity |
| Refuse missing/malformed/invalid JSON without partial data loss | `GymFileStore`, `GymService` | `InvalidJsonRegressionTest`, `GymSnapshotTest`, `GymFileStoreTest` | Excellent error handling |
| Protect corrupted startup/manual-load files | `GymConsole` | `ManualLoadSafetyTest`, `PersistenceConsoleTest`, `ConsoleStorageRecoveryTest` | Excellent error handling |
| Manual load rollback and explicit save recovery | `GymConsole`, `GymFileStore` | `ConsoleStorageRecoveryTest` | Persistence UX |
| Mixed repeated CRUD, links, capacity and restore invariants | `GymService` | `RelationshipInvariantTest` (500 deterministic operations) | Regression assurance |
| Execute a real console and inspect its JSON | `MainKt`, Gradle | Custom `verifyConsoleSmoke`, included in `check` and GitHub Actions | Gradle Excellent extra task + DX |
| Compile, JUnit, coverage verification, docs and structure | `build.gradle.kts`, GitHub Actions | `gradlew clean check jacocoTestReport` | Gradle/JUnit |

Automated tests prove the listed conditions on the tested code and environment; they do not establish correctness for every input or guarantee an Excellent grade.

## Mandatory GitHub workflow and submission checks

- **Completed and verifiable remotely:** meaningful issues, branches and commits, PRs referencing issues, passing CI before merge, merged PRs, closed issues and deleted remote feature branches. All feature work should continue using this sequence, not direct `main` commits.
- **Student must verify locally:** IntelliJ **Git → Fetch → Checkout main → delete previous local feature branch** and confirm latest changes remain in the IDE, as shown in the workflow slide.
- **NOT complete:** the Excellent GitHub column explicitly requires **multiple tagged releases**. The connected GitHub App was denied permission to create tags/Releases. Follow `docs/RELEASE_MANUAL.md` from an authenticated owner account.
- **Not confirmed:** the lecturer's required Week 8 GitHub invitation to `sdronan`; collaborator permission returned null and pending invites were not accessible.
- **Gradle unknown:** all lab-work tasks must be present and working in Actions, but **individual Gradle lab handouts were not supplied**. The Notes App alone is not proof that each task was done.
- **Submission not complete:** the source ZIP must be downloaded from GitHub, and remaining Moodle submission steps must be completed independently.

## Honest test result reporting

Use the GitHub Actions test-results and JaCoCo artifacts from the **latest successfully merged main branch build**. Do not copy a previous test count into the final submission without checking the new build. The pre-iteration baseline is 41 passing JUnit tests, 89% instruction coverage and 75% branch coverage (main run #43722066239). The successful **PR #24 run #43723245181**, verified from downloaded JUnit XML and JaCoCo HTML, reports **50 tests, 0 failures/errors/skips, 91% instruction coverage and 80% branch coverage**. The Gradle coverage gate is now raised to 90% instructions / 80% branches. Verify the post-merge `main` run independently before submission.

## Grade interpretation

Five categories each carry 20%; no numeric point-to-band conversion is supplied in the brief. Functionality and JUnit have strong Excellent evidence, architecture is Good-to-Excellent, GitHub is **not currently Excellent without tagged releases**, and Gradle cannot be fully graded until lab requirements are verified. Any numerical mark is therefore only a conditional, broad estimate—not a mark awarded by the lecturer.
