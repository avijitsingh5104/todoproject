# IntelliJ TODO Tracker Plugin

This repository contains an IntelliJ IDEA plugin developed using Kotlin. The plugin enhances TODO management by scanning source files and aggregating TODO comments in a dedicated tool window for easy tracking and navigation.

# Features

- Scans Kotlin files for `TODO` comments.
- Displays TODO entries in a custom Tool Window panel on the right side.
- Automatically updates the list when changes occur in the editor.
- Persistent storage of TODO items across IDE sessions.
- Search bar for searching and filtering TODO statements
- Service-based architecture using IntelliJ’s project and application services.

# File Overview

- `EditorChangeListener.kt`, `TodoPsiChangeListener.kt`: Listen to changes in the editor and project structure.
- `TodoAnnotator.kt`: Annotates TODOs in the editor and display them in red color.
- `TodoScanner.kt`: Parses and detects TODO entries.
- `TodoEntry.kt`: Data class representing a TODO item.
- `TodoService.kt`, `TodoPanelService.kt`, `TodoStorageService.kt`, `TodoProjectService.kt`: Services managing TODO logic, UI, and persistence.
- `TodoStartupActivity.kt`: Initializes the plugin on project startup.
- `TodoToolWindowFactory.kt`, `TodoToolWindowPanel.kt`: Set up and manage the custom tool window.
- `plugin.xml`: IntelliJ Platform plugin descriptor.
