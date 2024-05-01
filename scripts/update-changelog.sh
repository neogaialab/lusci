#!/bin/bash

# Define the filename of your changelog file
changelog_file="../CHANGELOG.md"

# Get the version from gradle.properties
version=$(grep -oP 'version=\K.*' gradle.properties)

# Get today's date in the format YYYY-MM-DD
today_date=$(date +%F)

# Replace the [Unreleased] section with today's date
sed -i "s/\[Unreleased\]/[$version] - $today_date/" "$changelog_file"

echo "Changelog updated with today's date: $today_date"
