# Mau Mau

![Sample Image](/readme-screenshot.png)

MauMau game with a dedicated backend.

## Naming Convention

For branch naming, please use the format: `<name>-<problem>`

Example: `kn-fixing-view`

## Introduction

To run the application:

1. Start the Java Spring app.
2. Download GODOT Engine and open the project.

Here is the link to download the GODOT Engine: [https://godotengine.org/download/windows](https://godotengine.org/download/windows)

Choose version: 4.1.1

Then, open `project.godot`.

## Game Rules

You can find the rules of the game at [https://bananario.de/onlinegames/maumau/maumau.php](https://bananario.de/onlinegames/maumau/maumau.php)

### Please adhere to the following rules:

Cards can only be placed on cards of the same color or value.
For example, if a Heart8 is played, another Heart or any other 8 can be played on top of it.
If you can't play a card, click on the draw pile to get another card in hand.

### Special Cards:

- Ace: If an Ace is played, the opponent must skip their turn.
- 7: After this card is played, the opponent must draw 2 cards, unless they also play a 7. With each additional 7, the number of penalty cards increases by 2.
- Jack: This card can always be played, regardless of the color in play. However, a Jack can't be played on top of another Jack. Once a Jack is played, the color to be played next can be chosen.

### Ending the Game

The game ends when a player lays down all their cards and declares 'mau mau'.
If the game is ended with a 7, all penalty cards must be drawn before counting. It can happen that you play a 7 as your last card, but haven't won yet because the opponent also plays a 7. The penalty cards must be drawn and the game continues.
Important: Don't forget to press the Mau button at the bottom left before playing your penultimate card, otherwise you must draw 2 penalty cards. If a game is ended with a Jack, the points are counted double."
