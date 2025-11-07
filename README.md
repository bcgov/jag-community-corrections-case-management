# jag-community-corrections-case-management
BCGOV Github repository for CCCM

![Lifecycle:Maturing](https://img.shields.io/badge/Lifecycle-Maturing-007EC6)

## Project Structure

    .
    ├── .github                                 # Contains GitHub Related sources
    ├── cccm-backend                            # backend application directrory                                               
    ├── cccm-frontend                			# frontend application directrory 
    ├── COMPLIANCE.yaml                         #
	├── docker-compose.yml                      # Docker compose 	
    ├── LICENSE                                 # Apache License
    └── README.md                               # This file.

## CI/CD
Deploying to Dev Test and Prod

DEV - automated process that gets triggered by code change 

There are different workflows for the different environments that we occupy. When code is pushed into the main branch in github, it triggers a build and push action which builds an image of the code and pushes that image to image stream in the tools space in openshift. It is then tagged with a dev tag and the deployments in our dev environment get triggered to restart the pod with the new image. The image SHA's are automated and tracked through the kustomization file in the argocd repo, meaning that every new image has a unique SHA and the pod knows the exact one that it is looking for (the new image that was pushed into the image stream).  

TEST/PROD - manual process that needs the user to intentionally run the github action

Once the code is running as expected in Dev and its approved to go to Test, there are 2 github action workflows that need to be run. "Promote backend image to test or production" and "Promote frontend image to Test or Production" ( backend-promote-image-to-env.yml and frontend-promote-image-to-env.yml for the code ). They are split up in 2 so that you have the option to promote just the frontend or the backend but if you want both you have to run both. When you go to one of these workflows there will be a button with a dropdown that says "Run workflow". All you need to do is select the environment you want it run to and it takes care of deploying it to that environment and the pod will restart when it sees there is a new image. 

ROLE-SYNC - this is a seperate workflow that probably wont need to run that much but it follows the same pattern as the main application, the workflow is automated to push to Dev and you have to manually run the action to push a new image to Test or Production. 
