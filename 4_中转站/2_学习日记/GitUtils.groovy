package com.ifastpay

import org.gradle.api.Project

class GitUtils {

    private GitUtils() {
    }

    static String executeCommand(List<String> cmd, Project requester) {
        def workingDir = requester.projectDir
        def logger = requester.logger

        try {
            logger.info("[exec] ${cmd.join(' ')} (cwd=${workingDir})")
            def proc = new ProcessBuilder(cmd)
                    .directory(workingDir)
                    .redirectErrorStream(true)
                    .start()
            proc.waitFor()
            return proc.exitValue() == 0 ? proc.inputStream.text.trim() : ""
        } catch (Exception e) {
            logger.warn("[exec] failure: ${e.message}")
            return ""
        }
    }

    static String gitDiffJava(List<String> extraArgs = [], Project requester) {
        def cmd = ['git', 'diff', '--name-only', '--diff-filter=ACMRTUXB'] +
                extraArgs +
                ['--', ':(glob)**/*.java']
        return executeCommand(cmd, requester)
    }

    static void gitFetchBranch(String remote = 'origin', String branch, Project requester) {
        executeCommand(['git', 'fetch', '--no-tags', '--prune', remote, branch], requester)
    }
}