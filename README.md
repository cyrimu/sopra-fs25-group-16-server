# Codenames-Server

## Introduction
Codenames is a game where opposing teams (red/blue) compete to describe their team’s words. There is a 5x5 grid of tiles with a word on each.  Each team has one spymaster who gives a clue to help their teammates guess the right words. They can only use one word as a descriptor, and the number of tiles it refers to. For example, “Water, 3” might describe “pool”, “fish”, “bucket”. The aim of the game is to be the first team to guess all of your tiles.

The aim of this  project is to digitalize the analog game and make it available to be played on the web. The possibilities of a online version are infinite, but it creates as well some inconveniences. As players would not be playing on the same room, it is required an input text to provide the clue and the times that the Spymaster thinks that a word is repeated. Moreover, it is possible that the players in the same room do not speak the same language. Therefore, we have designed a translation system.

Additionally, one complain that regular players complain about is that at a certain point the words start to become familiar. To enhance the players experience, we have added as well another version which instead of words, images are generated each time and displayed.

## Used Technologies

 - Java 17
 - Springboot
 - Gradle (Buildsystem)
 - MongoDB (Database)
 - Google Cloud (Deployment)
 - SonarCloud (Source Control)
 - OpenAI (Image Generation)
 - DeepL (Translation)
 
## High-Level Components

 [Game Class](https://github.com/cyrimu/sopra-fs25-group-16-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/classes/Game.java):
	 The game class contains all the data needed to represent Codenames in a digitial format and contains helper functions to retrieve often requested partial data and querys in a structured manner.
	 
[Game Service](https://github.com/cyrimu/sopra-fs25-group-16-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/service/GameService.java):
 The Game Service implements the actual logic to play Codenames. All actions from players are forwarded via endpoints to this service and the service modifies the game state of the correct game object corresponding to the action input by players. Additionally it functions as a Factory for Game objects.
 
 [Lobby Class](https://github.com/cyrimu/sopra-fs25-group-16-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/classes/Lobby.java):
 The Lobby class functions as a waiting area for players and stores the wished game configuration by players (Image mode vs. Text mode, assigned roles, etc.). This class only stores the data and offers simple modification. This data stored in the Lobby is passed via the GameConfiguration Class to the Game object constructor when creating new games.
 
 [Lobby Service](https://github.com/cyrimu/sopra-fs25-group-16-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/service/LobbyService.java):
 The Lobby service processes all the updates that are received from players regarding the Lobby stat and modifies the corresponding Lobby object. These modifications are then passed back to all players via Websocket. Additionally it functions as a Factory for Lobby objects.

## Building

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

 1. Download and install JDK-17 : 
	[Link to instructions](https://www.oracle.com/java/technologies/downloads/)
   
2. For version Control and contribution Git needs to be installed:
	[Link to instructions](https://git-scm.com/downloads)
   
 3. You also need to setup a GitHub account:
	 [Link to instructions](https://docs.github.com/en/get-started/start-your-journey/creating-an-account-on-github)

### External Dependencies

 - The DeepL API is used for translation and needs to be working in order for translation to succeed.
 - The OpenAI Image Generation API needs to be running in order for Image mode to function correctly.
 - MongoDB is used as a database and needs to be running in order for this application to work.
 (No action by developer is necessary since connection is already implemented)

### Building with Gradle

You can use the local Gradle Wrapper to build the application.

-   macOS: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

#### Build

    ./gradlew build

#### Run

    ./gradlew bootRun


You can verify that the server is running by visiting `localhost:8080` in your browser.

#### Test

    ./gradlew test

#### Development Mode

You can start the backend in development mode, this will automatically trigger a new build and reload the application once the content of a file has been changed.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`

#### API Endpoint Testing with Postman

We recommend using [Postman](https://www.getpostman.com) to test your API Endpoints.

#### Debugging

If something is not working and/or you don't know what is going on. We recommend using a debugger and step-through the process step-by-step.

To configure a debugger for SpringBoot's Tomcat servlet (i.e. the process you start with `./gradlew bootRun` command), do the following:

1.  Open Tab: **Run**/Edit Configurations
2.  Add a new Remote Configuration and name it properly
3.  Start the Server in Debug mode: `./gradlew bootRun --debug-jvm`
4.  Press `Shift + F9` or the use **Run**/Debug "Name of your task"
5.  Set breakpoints in the application where you need it
6.  Step through the process one step at a time


## Deployment
### External Dependencies
 - This server will deploy to Google Cloud to run, therefore the Google Services need to be running.
 - The GitHub actions need the connected Secrets to be present in order for deployment to succeed.
 - For Source code control Sonarcloud needs to be running and working
### Steps to deploy
This project uses CI/CD using github actions and will automatically deploy to the Google Cloud and all other external dependencies on each commit. Therefore no additional actions need to be performed.
### Modify Deployment
 - To add additional processes modify the [github actions file](https://github.com/cyrimu/sopra-fs25-group-16-server/tree/main/.github/workflows)
 - To modify Google Cloud settings modify [app.yaml](https://github.com/cyrimu/sopra-fs25-group-16-server/blob/main/app.yaml) 

## Roadmap
Additional Features that could be implemented:

 - Add a live chat for players to interact with each other (potentially split Spymasters and Operatives so that Spymasters cannot abuse chat to give hints).
 - Implement real time highlighting of selected cards for all players, so that other players can perceive the thought process of the current player.

## Authors

-  **Rashmi Dingdur** - _Frontend_ - [Profile](https://github.com/rashmidindgur)
-  **Calvin Klein** - Frontend - [Profile](https://github.com/calvinkoch00)
-    **Sergi Garcia Montmany** - _Fullstack_ - [Profile](https://github.com/sgm17)
-   **Cyril Müller** - _Backend_ - [Profile](https://github.com/cyrimu)
-   **Piotr Wojtaszewski** - _Backend_ - [Profile](https://github.com/winnerpio)


## License

This project is licensed under the Apache-2.0 Licence - see the [LICENSE.md](https://github.com/cyrimu/sopra-fs25-group-16-server/blob/main/LICENSE) file for details

## Acknowledgments
The following template provided by the instructors of the course : "Software Praktikum (SoPra) - FS25"
was used as starting point for this project.
[Link to template](https://github.com/HASEL-UZH/sopra-fs25-template-server) 
