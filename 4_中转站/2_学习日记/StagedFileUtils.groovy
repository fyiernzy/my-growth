package com.ifastpay

import org.gradle.api.Project

class StagedFileUtils {

    private StagedFileUtils() {
    }

    static String detectStagedJavaFiles(Project project) {
        def logger = project.logger

        // Priority 1: User manually provides files via -Pfiles
        def filesOverride = project.providers.gradleProperty('files').orElse('').get().trim()
        if (filesOverride) {
            logger.lifecycle("[checkstyleStaged] [${project.path}] Using -Pfiles override")
            return filesOverride.replaceAll(/\s+/, '\n')
        }

        // Priority 2: GitLab CI/CD Merge Request context
        def env = System.getenv()
        def baseSha = env['CI_MERGE_REQUEST_TARGET_BRANCH_SHA']
        def headSha = env['CI_COMMIT_SHA']
        def defaultBranch = env['CI_DEFAULT_BRANCH'] ?: 'develop'

        if (baseSha && headSha) {
            logger.lifecycle("[checkstyleStaged] [${project.path}] GitLab MR context detected: ${baseSha}..${headSha}")
            def output = GitUtils.gitDiffJava([baseSha, headSha], project)
            if (output) {
                logger.lifecycle("[checkstyleStaged] [${project.path}] Found ${output.readLines().size()} Java file(s) in GitLab MR diff")
                return output
            }
        }

        // Priority 3: CI/CD Pipeline
        if (env['CI'] == 'true') {
            logger.lifecycle("[checkstyleStaged] [${project.path}] CI pipeline context – comparing against origin/${defaultBranch}")
            GitUtils.gitFetchBranch('origin', defaultBranch, project)
            def output = GitUtils.gitDiffJava(["origin/${defaultBranch}", "HEAD"], project)
            if (output) {
                logger.lifecycle("[checkstyleStaged] [${project.path}] Found ${output.readLines().size()} Java file(s) in CI diff against ${defaultBranch}")
                return output
            }
        }

        // Priority 4: Local development (interactive staging)
        def stagedFiles = GitUtils.gitDiffJava(['--cached'], project)
        if (stagedFiles) {
            logger.lifecycle("[checkstyleStaged] [${project.path}] Found ${stagedFiles.readLines().size()} staged Java file(s)")
            return stagedFiles
        }

        // Priority 5: Local development - if no staged files, check unstaged changes
        def unstagedFiles = GitUtils.gitDiffJava([], project)
        if (unstagedFiles) {
            logger.lifecycle("[checkstyleStaged] [${project.path}] Found ${unstagedFiles.readLines().size()} unstaged Java file(s)")
            return unstagedFiles
        }

        logger.lifecycle("[checkstyleStaged] [${project.path}] No Java files found for checkstyle analysis")
        return ""
    }
}