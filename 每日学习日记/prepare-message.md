#!/bin/sh

echo '=====================Prepare Commit Message Hook======================='

COMMIT_MSG_FILE=$1
COMMIT_SOURCE=$2
SHA1=$3

message="$(cat $COMMIT_MSG_FILE)"
echo "Commit Message"
echo "=================================="
echo $message
echo "=================================="
regex='IssueI[dD]:[[:space:]]?(PMS_[0-9]+|BTMS_[0-9]+|CHANGE_ADHOC|BRANCH|ASSIGNMENT|POC|INFRA|GITOPS|QA)'
echo "Validate commit message against regex: $regex"
if [[ $message =~ ^[mM]erge.* || $message =~ ^[rR]evert.* ]]
then
	echo 'Skip merge or revert commit'
	exit 0
fi
if [[ $message =~ $regex ]] 
then 
	echo 'IssueID found in the commit message, proceed commit without any action'
	exit 0
fi

ISSUE_ID=$(git config issue.id)
message=" IssueID: $ISSUE_ID" >> "$COMMIT_MSG_FILE"
if [[ ! -z "$ISSUE_ID" && $message =~ $regex ]]
then 
	echo " IssueID: $ISSUE_ID" >> "$COMMIT_MSG_FILE"
	echo 'IssueID found in the commit message, proceed commit without any action'
	echo "Git Config Issue ID: $ISSUE_ID"
	exit 0
fi

echo "IssueID not found."
echo "Please proceed to amend with the IssueID."
echo "OR"
echo "Reconfigure/Configure the IssueID in your local git repository by using git config --local issue.id yourIssueId"
exit 1
