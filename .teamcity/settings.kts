import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.parallelTests
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

version = "2023.11"

project {

    vcsRoot(HttpsGithubComChubatovaTigerChubatovaGradleTestsBackup2)

    buildType(Build1)
    buildType(Build2)
}

object Build1 : BuildType({
    name = "build1"

    vcs {
        root(HttpsGithubComChubatovaTigerChubatovaGradleTestsBackup2, ".=>folder2")
        root(DslContext.settingsRoot, "+:. => .a")
    }

    steps {
        gradle {
            id = "gradle_runner"
            tasks = "clean build"
            buildFile = "build.gradle"
            workingDir = "folder2"
            jdkHome = "%env.JDK_11_0%"
        }
    }

    features {
        parallelTests {
            numberOfBatches = 2
        }
    }
})

object Build2 : BuildType({
    name = "build2"

    vcs {
        root(HttpsGithubComChubatovaTigerChubatovaGradleTestsBackup2)
        root(DslContext.settingsRoot, "+:. => .a")
    }

    steps {
        gradle {
            id = "gradle_runner"
            tasks = "clean build"
            buildFile = "build.gradle"
            jdkHome = "%env.JDK_11_0%"
        }
    }

    features {
        parallelTests {
            numberOfBatches = 3
        }
    }
})

object HttpsGithubComChubatovaTigerChubatovaGradleTestsBackup2 : GitVcsRoot({
    name = "https://github.com/ChubatovaTiger/ChubatovaGradleTestsBackup2"
    url = "https://github.com/ChubatovaTiger/ChubatovaGradleTestsBackup"
    branch = "refs/heads/master"
    branchSpec = "refs/heads/(*)"
})
