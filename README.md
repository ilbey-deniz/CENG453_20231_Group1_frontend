# CENG453 Group1 Catan Frontend

## Project Structure
```
└───com.group1.frontend
    ├───components (Logic edges, corners, board, game, etc.)
    ├───constants (Constants for the game)
    ├───controllers (controllers for the FXML files)
    ├───dto (Data transfer objects)
    │    ├─── httpDto (DTOs for HTTP requests)
    │    └─── websocketDto (DTOs for websocket messages)
    ├───enums (Enums for the game)
    ├───events (JavaFX events)
    ├───utils (Utility classes)
    └───view 
        └───elements (JavaFX elements written without FXML: BoardView, EdgeView, CornerView, etc.)
└───resources
    └───assets (.fxml files and images)
```

## General Idea of Game Updates and Communication
- After a lobby is created, clients can send and receive websocket messages from /game/{roomCode} endpoint.
- Our structure heavily depends on a HostPlayer and a ClientPlayer. HostPlayer is the player who created the lobby, and ClientPlayer is the player who joined the lobby.
- CPU players will play on HostPlayer's instance and updates will be sent to ClientPlayers.
- Backend acts as a WebSocketBroker, and there is no game logic on backend. Backend only sends messages to clients, and receives messages from clients.
- Game related updates are sent to clients via websocket messages.


## Features
- Lobby creation
  - /game/create POST request creates a new GameRoom on both frontend and backend, and returns a 5 digit game code.
  - when a player enters a game code, /game/join POST request is sent to backend, and if the game code is valid, the player is added to the game room.
  - when a player clicks the start button, /game/start POST request is sent to backend, and if the game room has 2 or more players, the game starts.
  - each player has a ready button, and when a player clicks it, /game/ready POST request is sent to backend, and if all players are ready, the game starts.
- CPU player
- Custom assets
- Websocket communication
  - After a lobby is created, clients can send and receive websocket messages from /game/{roomCode} endpoint.
  - Game related updates are sent to clients via websocket messages.
- Trade
    - Non-CPU players can trade with each other.
    - When a player clicks the trade button, a trade window pops up, and the player can select the resources to trade.
    - When a trade offer is sent,backend will be notified over /game/tradeInit and backend will set an AtomicBoolean to true.
    - Now the trade offer can be sent to the other player via websocket.
    - If the other player has enough resources, the player can accept the trade, and send confirmation to backend via /game/tradeAccept.
    - Backend will set the AtomicBoolean to false, and the trade will be completed. The AtomicBoolean will prevent race conditions.
    - After the trade is completed, all players will be notified over websocket.

## Testing
- GameTest
    - It tests the main functionalities of Game class.
        - Initialize game with 4 predefined player, board and initial buildings.
        - Test whether initialization is correct.
        - Test whether resource adding and initial resource distribution correct.
        - Test whether dice is bound between 2 and 12.
        - Test whether resource correctly distributed with dice roll.
- ServiceTest
    - It tests the main functionalities of Service which is about game creation and lobby.
        - Initialize test by creating two service by login.
        - Test whether login correct.
        - Test whether lobby creation correct and give 5 digit room code.
        - Test whether lobby creation and joining lobby works correct.
        - Test whether ready and start game functionalities correct.

## Notes
- Our contribution to the project is 50%. We have worked on the project together, and we have done pair programming most of the time. Therefore, commits are not a good indicator of our contribution.
