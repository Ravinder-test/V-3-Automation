#!/bin/bash

# Path to the folder where ExtentReport.html is generated
REPORT_PATH="test-output"

# GitHub repo info
GIT_REPO="https://github.com/Ravinder-test/Automation-Reports.git"

echo "🔁 Preparing to upload latest report to GitHub..."

cd "$REPORT_PATH" || exit

# Initialize Git and set remote if not done already
if [ ! -d ".git" ]; then
  git init
  git remote add origin "$GIT_REPO"
else
  echo "✅ Git already initialized"
fi


# Commit and push
git add .
git commit -m "Auto-update report $(date)"
git branch -M main
git push -f origin main

echo "✅ Report uploaded successfully!"
