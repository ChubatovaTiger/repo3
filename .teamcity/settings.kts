import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.amazonEC2CloudImage
import jetbrains.buildServer.configs.kotlin.amazonEC2CloudProfile
import jetbrains.buildServer.configs.kotlin.buildSteps.script

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

version = "2023.05"

project {

    buildType(Build1)

    features {
        amazonEC2CloudImage {
            id = "PROJECT_EXT_2"
            profileId = "amazon-1"
            vpcSubnetId = "subnet-043178c302cabfe37"
            instanceType = "t3.medium"
            securityGroups = listOf("sg-072d8bfa0626ea2a6")
            source = Source("ami-0bac84ec5a4017f06")
        }
        amazonEC2CloudProfile {
            id = "amazon-1"
            name = "bb"
            terminateIdleMinutes = 0
            region = AmazonEC2CloudProfile.Regions.EU_WEST_DUBLIN
            authType = instanceIAMRole()
            param("secure:access-id", "credentialsJSON:99126818-d5fd-49c1-969e-3d21b141cc85")
            param("secure:secret-key", "credentialsJSON:99126818-d5fd-49c1-969e-3d21b141cc85")
        }
    }
}

object Build1 : BuildType({
    name = "build1"

    steps {
        script {
            name = "bb"
            id = "simpleRunner%"
            scriptContent = "echo a"
        }
    }
})
