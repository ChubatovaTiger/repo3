import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.parallelTests
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.matrix
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot



version = "2023.11"

project {

    vcsRoot(HttpsGithubComChubatovaTigerChubatovaGradleTestsBackup)

    buildType(Matr)
    buildType(Build1)
    buildType(Build2)

    subProject(Chain)
}

object Build1 : BuildType({
    name = "build1"

    vcs {
        root(HttpsGithubComChubatovaTigerChubatovaGradleTestsBackup, ".=>folder")
        root(DslContext.settingsRoot, "+:. => .a")
    }

    steps {
        gradle {
            id = "gradle_runner"
            tasks = "clean build"
            buildFile = "build.gradle"
            workingDir = "folder"
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
        root(HttpsGithubComChubatovaTigerChubatovaGradleTestsBackup)
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
            numberOfBatches = 2
        }
    }
})

object Matr : BuildType({
    name = "matr"

    vcs {
        root(HttpsGithubComChubatovaTigerChubatovaGradleTestsBackup, ".=>f3")
    }

    steps {
        script {
            id = "simpleRunner"
            scriptContent = "ls f3"
        }
    }

    features {
        matrix {
            param("a1", listOf(
                value("2"),
                value("3")
            ))
        }
    }
})

object HttpsGithubComChubatovaTigerChubatovaGradleTestsBackup : GitVcsRoot({
    name = "https://github.com/ChubatovaTiger/ChubatovaGradleTestsBackup"
    url = "https://github.com/ChubatovaTiger/ChubatovaGradleTestsBackup"
    branch = "refs/heads/master"
    branchSpec = "refs/heads/(*)"
})


object Chain : Project({
    name = "chain"

    buildType(Chain_Build1)
    buildType(Chain_Build3)
})

object Chain_Build1 : BuildType({
    name = "build1"

    vcs {
        root(HttpsGithubComChubatovaTigerChubatovaGradleTestsBackup)
    }
    steps {
        script {
            id = "simpleRunner"
            scriptContent = "ls"
        }
    }
    dependencies {
        snapshot(Chain_Build3) {
            reuseBuilds = ReuseBuilds.NO
        }
    }
})

object Chain_Build3 : BuildType({
    name = "build3"

    vcs {
        root(HttpsGithubComChubatovaTigerChubatovaGradleTestsBackup, ".=>f2")
    }
    steps {
        script {
            id = "simpleRunner"
            scriptContent = "ls f2"
        }
    }
})
