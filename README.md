# QuackR

Our project for "Webtechnologien 2".

## Deployment

### Setting up Wildfly

* Install **Wildfly 16.0.0.Final** (Java EE Full & Web Distribution) from <https://wildfly.org/downloads/>
* Run `wildfly-installation-folder/bin/standalone.sh`

### Building and deploying the application

* Build the project using `mvn install` on the command line.
* Locate the `.war` archive at `quackapp-frontend/target/`.
* Copy the archive to `wildfly-installation-folder/standalone/deployments/`

### Viewing the application

* **Angular frontend:** Open <http://localhost:8080/quackapp> in your browser.
* **REST interface:** still missing?
