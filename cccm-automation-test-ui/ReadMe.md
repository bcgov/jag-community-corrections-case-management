Test Automation Project
The test automation is a subproject of the main jag-cccm project. It provides a series of end-to-end tests which are run against the active CCCM once deployed to OpenShift.

Usage
Clone the https://github.com/bcgov/jag-community-corrections-case-management repo and load the automation-test project individually into InteliJ IDEA of another IDE of your choosing. A single Maven command is used to execute the series of Automation tests from your IDE.

Before this may be executed, environmental variables must be set:

Environment variables for common tests           Value
'USERNAME_APP'	                                 User
'PASSWORD_APP'	                                 Password

