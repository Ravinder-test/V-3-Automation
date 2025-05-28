#!/bin/bash

# Paths
REPORT_SRC="/Users/apple/eclipse-workspace/V-3-Automation/PMIQ-Automation/test-output/ExtentReport.html"
REPORT_REPO="/Users/apple/eclipse-workspace/V-3-Automation/Automation-Reports"
DEST_FILE="$REPORT_REPO/ExtentReport.html"

# GitHub Repo
GIT_REPO="https://github.com/Ravinder-test/Automation-Reports.git"

echo "📁 Copying latest report from test project..."
cp "$REPORT_SRC" "$DEST_FILE" || { echo "❌ Failed to copy report"; exit 1; }

cd "$REPORT_REPO" || { echo "❌ Report repo folder not found at $REPORT_REPO"; exit 1; }

# Git init only if needed
if [ ! -d ".git" ]; then
  echo "🔧 Initializing Git repo..."
  git init
  git remote add origin "$GIT_REPO"
fi

# Commit and push
echo "🚀 Committing and pushing to GitHub..."
git add ExtentReport.html
git commit -m "🔄 Auto-update Extent Report - $(date)" || echo "⚠️ Nothing to commit"
git branch -M main
git push -f origin main

echo "✅ Report uploaded successfully!"