# Puerto Rico

A Java-based digital implementation of the Puerto Rico board game, developed as an individual academic project. The application recreates the core game mechanics, allowing multiple players to participate in a shared game session.

## Features

- **Player Management:** Tracks each player's doubloons, victory points (VP), colonists, goods, buildings, and plantations.
- **Role-Based Gameplay:** Implements six player roles: Settler, Builder, Mayor, Craftsman, Trader, and Captain, each with its own gameplay mechanics.
- **Resource Management:** Supports resource production, trading, shipping goods, and managing shared game resources.
- **Turn Management:** Handles player turns, game rounds, role selection, and game-ending conditions.
- **Game State Management:** Maintains and updates the overall game state, including player resources, board positions, available actions, and role-related bonuses.
- **Save & Load:** Allows players to save the current game state and resume a previously saved game.
- **Multiplayer Support:** Enables multiple players to participate from separate computers using TCP socket communication.
- **Game State Synchronization:** Serializes and transfers game state between players to keep their game views synchronized after player actions.
- **In-Game Chat:** Provides a multiplayer chat system using Java RMI, allowing players to send messages and retrieve chat history.

## Project Status

This project was developed as an academic project. Due to time constraints and the complexity of the original board game not all game cards and mechanics have been implemented. The application focuses on the core gameplay mechanics and multiplayer functionality.

## Tech Stack

- **Language:** Java
- **UI:** JavaFX
- **Networking:** TCP Sockets
