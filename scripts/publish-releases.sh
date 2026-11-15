#!/usr/bin/env bash
# Run in Git Bash on Windows, Linux or macOS from a fresh/clean project clone.
# Uses YOUR authenticated Git and GitHub CLI permissions. Never overwrites existing tags.
set -euo pipefail

repo="20112153/20112153-Software-Development-Tools-App-Project"

die() { printf 'ERROR: %s\n' "$*" >&2; exit 1; }
for tool in git gh; do
  command -v "$tool" >/dev/null 2>&1 || die "Install $tool first."
done

git rev-parse --is-inside-work-tree >/dev/null 2>&1 || die "Run from the repository root."
[ "$(git rev-parse --show-prefix)" = "" ] || die "Run from the repository root, not a subfolder."
remote="$(git remote get-url origin)" || die "Missing origin remote."
case "$remote" in
  "https://github.com/$repo"|"https://github.com/$repo.git"|"git@github.com:$repo"|"git@github.com:$repo.git") ;;
  *) die "Unexpected origin: $remote. Expected $repo." ;;
esac

[ "$(git branch --show-current)" = "main" ] || die "Checkout main before publishing releases."
[ -z "$(git status --porcelain)" ] || die "Working tree has uncommitted changes. Commit or stash them first."
gh auth status -h github.com >/dev/null 2>&1 || die "Run 'gh auth login' with your GitHub account first."
git fetch origin main --tags
git pull --ff-only origin main
[ "$(git rev-parse HEAD)" = "$(git rev-parse origin/main)" ] || die "Local main differs from origin/main."

main_sha="$(git rev-parse HEAD)"
printf 'Publishing historical milestones for %s\n' "$repo"
printf 'Final v1.0.0 will point to %s\n' "$main_sha"
printf 'Make sure the latest main CI is green before running this script.\n'
read -r -p "Type PUBLISH to proceed (anything else cancels): " consent
[ "$consent" = "PUBLISH" ] || die "Cancelled; no changes made."

publish_one() {
  local tag="$1" sha="$2" title="$3" notes="$4"
  git cat-file -e "${sha}^{commit}" || die "Missing milestone commit $sha for $tag."
  git merge-base --is-ancestor "$sha" "$main_sha" || die "$sha is not an ancestor of main."

  if git show-ref --verify --quiet "refs/tags/$tag"; then
    existing="$(git rev-list -n 1 "$tag")"
    [ "$existing" = "$sha" ] || die "Existing $tag points to $existing instead of $sha; review manually."
    printf 'Verified existing tag %s.\n' "$tag"
  else
    git tag -a "$tag" "$sha" -m "$title"
    printf 'Created annotated local tag %s.\n' "$tag"
  fi

  git push origin "refs/tags/$tag"
  if gh release view "$tag" --repo "$repo" >/dev/null 2>&1; then
    printf 'Existing GitHub Release %s: left untouched.\n' "$tag"
  else
    gh release create "$tag" --repo "$repo" --verify-tag --title "$title" --notes "$notes"
    printf 'Published GitHub Release %s.\n' "$tag"
  fi
}

publish_one v0.1.0 2501ca0d95256041da4cf7e4e6b2c09b38034760 \
  'v0.1.0 - Initial framework' \
  'Initial member, class and booking models, service, JUnit and Gradle automation. Issue #1, PR #2.'
publish_one v0.2.0 77d3351b3efd7a57c601153ed7f41d15d9ff0991 \
  'v0.2.0 - Console and JSON persistence' \
  'Member/class/booking menus, safe booking updates and JSON save/load. Issues #3, #5, #7 and PRs #4, #6, #8.'
publish_one v0.3.0 9a2cdacc3869fabcec6911480ad2aaeb28110c77 \
  'v0.3.0 - Search and reports' \
  'Case-insensitive search, capacity filtering and booking reports. Issue #9, PR #10.'
publish_one v0.4.0 098c4b06639876b723b73a256aee4d9cda9afce5 \
  'v0.4.0 - Gradle and JUnit quality' \
  'Official Gradle wrapper, enhanced checks and additional error-path tests. Issue #11, PR #12.'
publish_one v1.0.0 "$main_sha" \
  'v1.0.0 - Final Gym Class Manager' \
  'Fully documented Kotlin Gym Class Manager with many-to-many CRUD, JSON persistence, searching, filtering, booking reports, 50+ JUnit tests, Gradle quality checks and issue/PR history.'

printf '\nDONE: verify the five Releases appear at https://github.com/%s/releases\n' "$repo"
