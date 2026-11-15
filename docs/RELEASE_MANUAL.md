# How to create the required tagged GitHub Releases

**Manual action required:** The connected GitHub App was able to commit, merge and run CI but could not create GitHub Releases (HTTP 403) or push version tags containing workflow history (missing `workflows` permission). No tags or Release objects were verified as published by the automation.

## 1. Fastest safe method (Windows Git Bash)

From a fresh clone with **Git** and the **GitHub CLI** installed, authenticate `gh auth login`, then run:

```bash
bash scripts/publish-releases.sh
```

The helper verifies it is in this exact repository on clean, up-to-date `main`, checks existing tags instead of overwriting them, creates annotated historical tags at verified milestone commits, pushes them and publishes the corresponding Releases with your authenticated owner account. It requires a GitHub user who actually has permission to write tags and Releases. If permission is denied, stop and use your GitHub account's token/permissions or the manual UI method below.

## 2. Manual alternative: create annotated tags using your own Git account

Use an authenticated terminal with Git installed, or open a terminal in your local clone:

```bash
git clone https://github.com/20112153/20112153-Software-Development-Tools-App-Project.git
cd 20112153-Software-Development-Tools-App-Project
git fetch origin --tags

git tag -a v0.1.0 2501ca0d95256041da4cf7e4e6b2c09b38034760 -m "Initial gym framework (issue #1, PR #2)"
git tag -a v0.2.0 77d3351b3efd7a57c601153ed7f41d15d9ff0991 -m "Booking CRUD and JSON persistence (issues #3 #5 #7)"
git tag -a v0.3.0 9a2cdacc3869fabcec6911480ad2aaeb28110c77 -m "Searching filtering and reports (issue #9)"
git tag -a v0.4.0 098c4b06639876b723b73a256aee4d9cda9afce5 -m "Gradle wrapper and testing quality (issue #11)"
git tag -a v1.0.0 origin/main -m "Final verified Gym Class Manager"

git push origin v0.1.0 v0.2.0 v0.3.0 v0.4.0 v1.0.0
```

If any tag already exists, **inspect it first** with `git show --no-patch v0.1.0` and do not overwrite tags. Only run the above after the final `main` build passes. The historical SHA values are actual merged milestones; `v1.0.0` should refer to the current final `main` head.

## 3. Publish Release objects on GitHub

1. Open the repository's **Releases** page.
2. Select **Draft a new release**.
3. Under **Choose a tag**, select an existing version tag (for example `v0.1.0`).
4. Enter a release title and copy the matching milestone description from `CHANGELOG.md`.
5. Click **Publish release**, then repeat for the other tags.

Check that five Releases are visible. No other app-code changes are needed to publish Releases.

## 4. Validate and submit

Confirm release versions and commit targets in GitHub, invite/verify the lecturer's `sdronan` account, then download the **final GitHub source ZIP**. The requirements are summarised in [SUBMISSION_CHECKLIST.md](SUBMISSION_CHECKLIST.md).

**Why not run the automation again?** The GitHub logs show a permissions denial; retrying without changed app permissions cannot solve it.
