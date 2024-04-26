#!/bin/bash

urlencode() {
  local LCALL=C
  local string="$*"
  local length="${#string}"
  local char

  for (( i = 0; i < length; i++ )); do
    char="${string:i:1}"
    if [[ "$char" == [a-zA-Z0-9.~-] ]]; then
      printf "$char" 
    else
      printf '%%%02X' "'$char" 
    fi
  done
  printf '\n'
}

# Define your GitHub username and repository name
github_username="neogaialab"
repo_name="lusci"

# Get the version from gradle.properties
version=$(grep -oP 'version=\K.*' gradle.properties)

title="v$version"
tag_name="v$version"
prerelease="false"
release_notes="Please refer to [CHANGELOG.md](https://github.com/$github_username/$repo_name/blob/main/CHANGELOG.md) for details."

title_encoded=$(urlencode "$title")
tag_name_encoded=$(urlencode "$tag_name")
release_notes_encoded=$(urlencode "$release_notes")

# Generate the GitHub release URL
github_url="https://github.com/$github_username/$repo_name/releases/new/$release_version?title=$title_encoded&body=$release_notes_encoded&tag=$tag_name&prerelease=$prerelease"

# Print the clickable URL
echo "GitHub Release URL: $github_url"
