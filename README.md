# GwentStone

This project implements a card game where two players compete against each other using decks of cards. The game involves strategic placement of cards on a table and attacking the opponent's cards and hero.

## Features
The Card Game implementation showcases the following key features, all designed and implemented using object-oriented programming principles:

- Modular Card Classes: Each card in the game is represented by a class that encapsulates its attributes, abilities, and behavior. This modular approach allows for easy addition of new card types and enhances code maintainability.
- Inheritance and Polymorphism: The game leverages inheritance to create specialized card classes that inherit common properties from abstract card classes. This enables polymorphism, allowing cards to be treated uniformly while exhibiting unique behaviors.
- Encapsulation and Information Hiding: The card classes encapsulate their data and provide controlled access to their attributes. This information hiding promotes data integrity and supports future modifications without affecting the overall system.
- Class Relationships and Composition: The game models relationships between different classes, such as players, decks, and heroes, using composition. This composition-based design enhances code organization and facilitates interaction between game entities.
- Abstraction and Interfaces: Interfaces are used to define common behaviors and contract specifications for classes. This abstraction allows for loose coupling between components and promotes flexibility and code reuse.
- Design Patterns: The project incorporates various design patterns, such as the Factory Method pattern for creating cards, the Command pattern for executing player actions, and the Strategy pattern for defining different card behaviors. These patterns enhance code readability, maintainability, and extensibility.
- Code Reusability and Extensibility: The modular design and adherence to OOP principles make it easy to add new card types, implement additional game features, or modify existing behavior without impacting the core game logic.

## Usage
To play GwentStone, follow these instructions:

Compile the Java source files:
`javac main/Main.java`

Prepare your game by creating an input JSON file that defines the game setup and actions. Take inspiration from the provided input sample for the JSON structure.
Launch the game by executing the following command and providing the path to your input file as a command-line argument:
`java main.Main /path/to/input.json`

The results will be saved in a JSON output file named output.json.
