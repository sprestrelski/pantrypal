# CSE 110 Project Team 39 Culinary Companion Crew
PantryPals - the application that provides you with recipes that YOU have currently in your possession! 

GitHub Repo: https://github.com/ucsd-cse110-fa23/cse-110-project-team-39  
MS1 Project Board: https://github.com/orgs/ucsd-cse110-fa23/projects/10  
MS2 Project Board: https://github.com/orgs/ucsd-cse110-fa23/projects/74  

## How to run the app
1. Clone the repository
2. Change or create a `.vscode/launch.json`, and change the `vmArgs` to point to your JavaFX lib.
3. Run from `App.java`.

### Sample `launch.json`
```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Current File",
            "request": "launch",
            "mainClass": "${file}"
        },
        {
            "type": "java",
            "name": "App",
            "request": "launch",
            "mainClass": "code.App",
            "projectName": "app", 
            "vmArgs": "--module-path 'user/javafx-sdk-21/lib' --add-modules javafx.controls,javafx.base,javafx.fxml,javafx.graphics,javafx.media,javafx.web --add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED --add-exports javafx.base/com.sun.javafx.event=ALL-UNNAMED"
        }
    ]
}
```