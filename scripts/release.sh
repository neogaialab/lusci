#!/bin/bash

# Define functions

function is_semver() {
  local semver="$1"
  local regex="^([0-9]+)\.([0-9]+)\.([0-9]+)(-[a-zA-Z0-9-]+)*$"
  echo $semver
  if [[ $semver =~ $regex ]]; then
    true
  else
    false
  fi
}

execute_command() {
  echo "Executing command: $*"
  eval "$*"
}

bump_changelog() {
  execute_command bash ../scripts/update-changelog.sh
}

release_github() {
  execute_command bash ../scripts/release-github.sh
}

# Checks

if [ -z "$1" ]; then
    echo "Error: Version is required."
    exit 1
fi

if ! is_semver $1; then
  echo "Invalid version"
  exit 1
fi

# Define variables

version="$1"

tagTemplate="v$version"
tagAnnotation="Release v$version"
commitMessage="chore: release v$version"

buildTasks=("gradle shadowJar")

pushToRemote="origin"
requireBranch="main"

# Define tasks

build() {
  echo "Executing build tasks: ${buildTasks[@]}"
  for task in "${buildTasks[@]}"; do
    execute_command "$task"
  done
}

bump_version() {
  sed -i "s/^version=.*/version=$version/" gradle.properties
}

git_tasks() {
  git add -A
  git commit -m "$commitMessage"
  git tag -a "$tagTemplate" -m "$tagAnnotation"
  git push "$pushToRemote" "$requireBranch"
}

# Main script logic

bump_version
bump_changelog
build
git_tasks
release_github

echo "Release process completed."
