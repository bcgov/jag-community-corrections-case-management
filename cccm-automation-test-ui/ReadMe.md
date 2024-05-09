Test Automation Project
The test automation is a subproject of the main jag-cccm project. It provides a series of end-to-end tests which are run against the active CCCM once deployed to OpenShift.

Usage
Clone the https://github.com/bcgov/jag-community-corrections-case-management repo and load the automation-test project individually into InteliJ IDEA of another IDE of your choosing. A single Maven command is used to execute the series of Automation tests from your IDE.

Before this may be executed, environmental variables must be set:

Environment variables for common tests           Value
'USERNAME_APP'	                                 User
'PASSWORD_APP'	                                 Password

Run the tests with command:
java -jar pathToJar "args", in args you should specify what kind of tests should be run: Login,CreateNewCustodyCMRP,regression.
Add "true" value for tests that you want to run, "false is optional".

Command which will run one Login test:
java -jar target/cccm-automation-test-ui-0.0.1-SNAPSHOT-test-jar-with-dependencies.jar Login=true

Command which will run Login  and CreateNewCustodyCMRP tests:
java -jar target/cccm-automation-test-ui-0.0.1-SNAPSHOT-test-jar-with-dependencies.jar Login=true,CreateNewCustodyCMRP=true

Command which will run full regression scope:
java -jar target/cccm-automation-test-ui-0.0.1-SNAPSHOT-test-jar-with-dependencies.jar  regression=true

DOCKER:

to build docker:
docker build --tag 'cccm_at' .    

to run a test in container use command:
docker run -e USERNAME_APP=USERNAME_APP_VALUE -e PASSWORD_APP=PASSWORD_APP_VALUE 'cccm_at' CreateNewCustodyCMRP=true  

to run a full regression:
docker run -e USERNAME_APP=USERNAME_APP_VALUE -e PASSWORD_APP=PASSWORD_APP_VALUE 'cccm_at' regression=true  