import jetbrains.buildServer.configs.kotlin.*

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2024.12"

project {

    buildType(Build2)
    buildType(Build1)
}

object Build1 : BuildType({
    name = "build1"

    enablePersonalBuilds = false
    type = BuildTypeSettings.Type.COMPOSITE
    maxRunningBuilds = 1

    params {
        param("par1", "2")
    }

    vcs {
        showDependenciesChanges = true
    }

    dependencies {
        snapshot(Build2) {
        }
    }
})

object Build2 : BuildType({
    name = "build2"
    steps {
        script {
            id = "simpleRunner"
            scriptContent = """
                echo "##teamcity[testStarted name='MyTest.test2']"
                sleep 2
                echo "##teamcity[testFailed type='comparisonFailure' name='MyTest.test2' message='failure message' details='message and stack trace' expected='expected value' actual='actual value']"
                echo "##teamcity[testFinished name='MyTest.test2']"
                sleep 2
            """.trimIndent()
        }
    }
})
