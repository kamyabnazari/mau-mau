extends Node

# Loading all of required nodes

@onready var node_deck = $DeckPile
@onready var node_discard_pile = $DiscardPilePanel/DiscardPile
@onready var node_players_hand = $PlayersHand
@onready var node_opponents_hand = $OpponentsHand
@onready var node_avatar_opponent = $AvatarOpponent
@onready var node_avatar_player = $AvatarPlayer
@onready var node_external_panel_overlay = $ExternalPanelOverlay
@onready var node_game_won = $GameWonPanel
@onready var node_game_lost = $GameLostPanel
@onready var node_pause_panel = $PausePanel
@onready var node_suit_choosing_panel = $SuitChoosingPanel
@onready var node_name_choosing_panel = $NameChoosingPanel
@onready var node_player_name_field = $NameChoosingPanel/PlayerNameField
@onready var node_game_choosing_panel = $GameChoosingPanel
@onready var node_current_suit_label = $CurrentGameSuit/Suit
@onready var node_endpoint_start_game = $EndpointStartGame
@onready var node_endpoint_get_playble_cards = $EndpointGetPlayableCards
@onready var node_endpoint_play_turn = $EndpointPlayTurn
@onready var node_endpoint_get_all_games = $EndpointGetAllGames
@onready var node_endpoint_get_game = $EndpointGetGame

var card_resource : PackedScene = load("res://levels/game/card.tscn")

# All Games
var current_all_games : Array

var rank_to_texture_file_map = {
	"ACE": "ace",
	"TWO": "2",
	"THREE": "3",
	"FOUR": "4",
	"FIVE": "5",
	"SIX": "6",
	"SEVEN": "7",
	"EIGHT": "8",
	"NINE": "9",
	"TEN": "10",
	"JACK": "jack",
	"QUEEN": "queen",
	"KING": "king"
}

var suit_to_texture_file_map = {
	"HEART": "hearts",
	"DIAMOND": "diamonds",
	"CLUB": "clubs",
	"SPADE": "spades"
}

# Define number of starting cards
var number_of_cards_to_draw : int = 1
var number_of_cards_first : int = 4

enum Players {PLAYER, OPPONENT}
var current_game_turn : = Players.PLAYER

# HTTP Recieved Obejcts
var current_game : Dictionary
var current_game_id : int
var current_game_direction : String
var current_game_suit : String = ""
var current_game_state_running : bool = false

# Deck of cards variables
var current_deck_id : int
var current_deck_list_of_cards : Array

# Discard Pile of card variables
var current_discard_pile_id : int
var current_discard_pile_cards : Array

# Player variables 
var current_player_id : int
var current_player_name : String
var current_player_score : int
var current_player_mau_mau_call : bool

var current_card_being_played

# Player hand variables
var current_player_hand_id : int
var current_player_hand_cards : Array
var current_player_playable_hand_cards : Array

# Opponent variables
var current_opponent_id : int
var current_opponent_name : String
var current_opponent_score : int
var current_opponent_mau_mau_call : bool

# Opponent hand variables
var current_opponent_hand_id : int
var current_opponent_hand_cards : Array
var current_opponent_playable_hand_cards : Array

# First method to be called
func _ready():
	# Basic randomizer
	randomize()

	# Preparing signals
	GameActions.connect("played_card", _action_card_being_played)
	
	# HTTP Response Handlers
	GameActions.connect("http_start_game_response", _handler_http_start_game_response)
	GameActions.connect("http_get_playable_cards_response", _handler_get_playable_cards_response)
	GameActions.connect("http_play_turn_reponse", _handler_play_turn_response)
	GameActions.connect("http_get_all_games_response", _handler_http_get_all_games_response)
	GameActions.connect("http_get_game_response", _handler_http_get_game_response)
	
	# Starting Game Sounds
	AudioManager.find_child("MusicStack").play()
	
	# Default Avatars states
	node_avatar_opponent.texture =  load("res://shared/themes/round_buttons/button_round_blue.png")
	node_avatar_opponent.modulate.a = 0.5
	
	node_avatar_player.texture = load("res://shared/themes/round_buttons/button_round_brown.png")
	node_avatar_player.modulate.a = 1.0
	
	await node_endpoint_get_all_games.http_get_all_games()

# Game loop method
func _process(_delta):
	if current_game_state_running:
		if node_players_hand.get_child_count() == 0:
			_action_game_won()
		elif node_opponents_hand.get_child_count() == 0:
			_action_game_lost()

func _action_game_won():
	_on_game_won()

func _action_game_lost():
	_on_game_lost()

# Starting method on first round start
func _action_start_game():
	_helper_clear_round()
	await node_endpoint_start_game.http_start_game(current_player_name)

func _handler_http_get_game_response(game):
	_helper_clear_round()
	_helper_map_game_object_to_variables(game)
	_starting_action_create_deck_of_cards()
	_starting_action_create_discard_pile()
	_starting_action_create_hands()
	
	_on_close_game_choosing_panel()

func _handler_http_get_all_games_response(games):
	current_all_games = games
	
	var games_list : ItemList = $GameChoosingPanel/GamesList
	for game in current_all_games:
		var game_name : String = "Game: " + String.num(game["id"])
		games_list.add_item(game_name)
	
	# Open Game Choosing Panel
	_on_open_game_choosing_panel()

func _handler_http_start_game_response(game):
	_helper_map_game_object_to_variables(game)
	_starting_action_create_deck_of_cards()
	_starting_action_create_discard_pile()
	_starting_action_create_hands()
	
	AudioManager.find_child("SoundFan").play()
	current_game_state_running = current_game["running"]

func _handler_get_playable_cards_response(list_of_valid_cards):
	current_player_playable_hand_cards = list_of_valid_cards
	
	for card in current_player_playable_hand_cards:
		if card["id"] == current_card_being_played["id"]:
			if current_card_being_played["rank"] == "JACK":
				_on_open_suit_choosing_panel()
			else:
				await node_endpoint_play_turn.http_play_turn(current_game_id, current_player_id, "PLAY" ,current_card_being_played["id"], current_game_suit, current_player_mau_mau_call)

func _handler_play_turn_response(game):
	_helper_clear_round()
	_helper_map_game_object_to_variables(game)
	_starting_action_create_deck_of_cards()
	_starting_action_create_discard_pile()
	_starting_action_create_hands()

func _action_draw_card():
	AudioManager.find_child("SoundSlide").play()
	await node_endpoint_play_turn.http_play_turn(current_game_id, current_player_id, "DRAW" ,current_card_being_played["id"], current_game_suit, current_player_mau_mau_call)

# Switch the current turn to next player
func _action_change_turn():
	if current_game_turn == Players.PLAYER:
		current_game_turn = Players.OPPONENT
		
		node_avatar_opponent.texture = load("res://shared/themes/round_buttons/button_round_brown.png")
		node_avatar_opponent.modulate.a = 1.0
		
		node_avatar_player.texture =  load("res://shared/themes/round_buttons/button_round_blue.png")
		node_avatar_player.modulate.a = 0.5
	else:
		current_game_turn = Players.PLAYER
		
		node_avatar_opponent.texture =  load("res://shared/themes/round_buttons/button_round_blue.png")
		node_avatar_opponent.modulate.a = 0.5
		
		node_avatar_player.texture = load("res://shared/themes/round_buttons/button_round_brown.png")
		node_avatar_player.modulate.a = 1.0

# Checking for card played
func _action_card_being_played(data):
	current_card_being_played = data
	
	# Get all valid cards for player
	await node_endpoint_get_playble_cards.http_get_playable_cards(current_game_id, 0)

# Suit change triggering
func _action_choose_suit():
	_on_open_suit_choosing_panel()

# Creating starting deck of cards
func _starting_action_create_deck_of_cards():
	# Iterate through the 'current_deck_list_of_cards' array
	for card_data in current_deck_list_of_cards:
		var card = card_resource.instantiate()
		card.suit = card_data['suit']
		card.rank = card_data['rank']
		card.order = card_data["order"]
		card.id = card_data['id']
		var suit_texture_file = suit_to_texture_file_map[card_data['suit']]
		var rank_texture_file = rank_to_texture_file_map[card_data['rank']]
		card.texture = load("res://levels/game/assets/cards_atlas/atlas_texture_card_" + suit_texture_file + "_" + rank_texture_file + ".tres")
		node_deck.add_child(card)

# Creating Discard Pile and adding first card
func _starting_action_create_discard_pile():
	# Iterate through the 'current_discard_pile_cards' array
	for card_data in current_discard_pile_cards:
		var card = card_resource.instantiate()
		card.suit = card_data['suit']
		card.rank = card_data['rank']
		card.order = card_data["order"]
		card.id = card_data['id']
		card.mouse_filter = Control.MOUSE_FILTER_IGNORE
		var suit_texture_file = suit_to_texture_file_map[card_data['suit']]
		var rank_texture_file = rank_to_texture_file_map[card_data['rank']]
		card.texture = load("res://levels/game/assets/cards_atlas/atlas_texture_card_" + suit_texture_file + "_" + rank_texture_file + ".tres")
		node_discard_pile.add_child(card)

# Create starting hands
func _starting_action_create_hands():
	# Iterate through the 'current_player_hand_cards' array
	for card_data in current_player_hand_cards:
		var card = card_resource.instantiate()
		card.suit = card_data['suit']
		card.rank = card_data['rank']
		card.order = card_data["order"]
		card.id = card_data['id']
		var suit_texture_file = suit_to_texture_file_map[card_data['suit']]
		var rank_texture_file = rank_to_texture_file_map[card_data['rank']]
		card.texture = load("res://levels/game/assets/cards_atlas/atlas_texture_card_" + suit_texture_file + "_" + rank_texture_file + ".tres")
		node_players_hand.add_child(card)

	# Iterate through the 'current_opponent_hand_cards' array
	for card_data in current_opponent_hand_cards:
		var card = card_resource.instantiate()
		card.suit = card_data['suit']
		card.rank = card_data['rank']
		card.order = card_data["order"]
		card.id = card_data['id']
		card.change_card_to_opponent()
		card.mouse_filter = Control.MOUSE_FILTER_IGNORE
		node_opponents_hand.add_child(card)

# Helper methods

# Clears all cards from the players' hands and the discard pile
func _helper_clear_round():
	for card in node_players_hand.get_children():
		card.queue_free()
		
	for card in node_opponents_hand.get_children():
		card.queue_free()

	for card in node_discard_pile.get_children():
		card.queue_free()
	
	for card in node_deck.get_children():
		card.queue_free()	

func _helper_map_game_object_to_variables(body):
	# Game properties
	current_game = body
	current_game_id = current_game["id"]
	current_game_direction = current_game["direction"]
	current_game_suit = current_game["suit"]
	
	# Extracting the list of cards data
	current_deck_id = current_game["deck"]["id"]
	current_deck_list_of_cards = current_game["deck"]["listOfCard"]
	
	# Extracting the discard pile data
	current_discard_pile_id = current_game["discardPile"]["id"]
	current_discard_pile_cards = current_game["discardPile"]["listOfCard"]
	
	# Extracting the current player data
	current_player_id = current_game["currentPlayer"]["id"]
	current_player_name = current_game["currentPlayer"]["name"]
	current_player_score = current_game["currentPlayer"]["score"]
	current_player_mau_mau_call = current_game["currentPlayer"]["mauMauCall"]
	
	# Extracting the current hand data
	current_player_hand_id = current_game["currentPlayer"]["hand"]["id"]
	current_player_hand_cards = current_game["currentPlayer"]["hand"]["listOfCard"]

	# Extracting the opponent player data
	current_opponent_id = current_game["nextPlayer"]["id"]
	current_opponent_name = current_game["nextPlayer"]["name"]
	current_opponent_score = current_game["nextPlayer"]["score"]
	current_opponent_mau_mau_call = current_game["nextPlayer"]["mauMauCall"]
	
	# Extracting the opponent hand data
	current_opponent_hand_id = current_game["nextPlayer"]["hand"]["id"]
	current_opponent_hand_cards = current_game["nextPlayer"]["hand"]["listOfCard"]
	
	node_current_suit_label.text = current_game_suit

# Helper method to move card to discard pile
func _helper_action_move_card_to_discard_pile(card):
	# Moving card from deck to discard pile
	card.get_parent().remove_child(card)
	node_discard_pile.add_child(card)

# MauMau Action

func _on_mau_mau_button_pressed():
	current_player_mau_mau_call = true

# PausePanel Toggles

func _on_close_settings_button_pressed():
	AudioManager.find_child("SoundButton").play()
	node_external_panel_overlay.visible = false
	var tween = create_tween()
	tween.tween_property(node_pause_panel, "position", Vector2(234, 400), 1)

func _on_pause_button_pressed():
	AudioManager.find_child("SoundButton").play()
	node_external_panel_overlay.visible = true
	var tween = create_tween()
	tween.tween_property(node_pause_panel, "position", Vector2(234, 69), 1)

# PausePanel Buttons

func _on_main_menu_button_pressed():
	AudioManager.find_child("SoundButton").play()
	SceneTransition.change_scene("res://ui/main_menu/main_menu.tscn", "dissolve")

func _on_music_toggle_pressed():
	AudioManager.find_child("SoundButton").play()
	if AudioServer.is_bus_mute(1):
		AudioServer.set_bus_mute(1, false)
	else:
		AudioServer.set_bus_mute(1, true)

func _on_sound_toggle_pressed():
	AudioManager.find_child("SoundButton").play()
	if AudioServer.is_bus_mute(2):
		AudioServer.set_bus_mute(2, false)
	else:
		AudioServer.set_bus_mute(2, true)

# Choosing suit panel

func _on_open_suit_choosing_panel():
	node_external_panel_overlay.visible = true
	var tween = create_tween()
	tween.tween_property(node_suit_choosing_panel, "position", Vector2(234, 69), 1)

func _on_suit_hearts_button_pressed():
	node_external_panel_overlay.visible = false
	var tween = create_tween()
	tween.tween_property(node_suit_choosing_panel, "position", Vector2(234, 400), 1)
	current_game_suit = "HEART"
	node_current_suit_label.text = current_game_suit
	await node_endpoint_play_turn.http_play_turn(current_game_id, current_player_id, "PLAY" ,current_card_being_played["id"], current_game_suit, current_player_mau_mau_call)

func _on_suit_clubs_button_pressed():
	node_external_panel_overlay.visible = false
	var tween = create_tween()
	tween.tween_property(node_suit_choosing_panel, "position", Vector2(234, 400), 1)
	current_game_suit = "CLUB"
	node_current_suit_label.text = current_game_suit
	await node_endpoint_play_turn.http_play_turn(current_game_id, current_player_id, "PLAY" ,current_card_being_played["id"], current_game_suit, current_player_mau_mau_call)

func _on_suit_spades_button_pressed():
	node_external_panel_overlay.visible = false
	var tween = create_tween()
	tween.tween_property(node_suit_choosing_panel, "position", Vector2(234, 400), 1)
	current_game_suit = "SPADE"
	node_current_suit_label.text = current_game_suit
	await node_endpoint_play_turn.http_play_turn(current_game_id, current_player_id, "PLAY" ,current_card_being_played["id"], current_game_suit, current_player_mau_mau_call)

func _on_suit_diamonds_button_pressed():
	node_external_panel_overlay.visible = false
	var tween = create_tween()
	tween.tween_property(node_suit_choosing_panel, "position", Vector2(234, 400), 1)
	current_game_suit = "DIAMONDS"
	node_current_suit_label.text = current_game_suit
	await node_endpoint_play_turn.http_play_turn(current_game_id, current_player_id, "PLAY" ,current_card_being_played["id"], current_game_suit, current_player_mau_mau_call)

# Drawing card button

func _on_draw_button_pressed():
	_action_draw_card()

# Game State Messages

func _on_game_won():
	node_external_panel_overlay.visible = true
	var tween = create_tween()
	tween.tween_property(node_game_won, "position", Vector2(206, 122), 1)

func _on_game_lost():
	node_external_panel_overlay.visible = true
	var tween = create_tween()
	tween.tween_property(node_game_lost, "position", Vector2(206, 122), 1)

# Name choosing panel

func _on_open_game_choosing_panel():
	node_external_panel_overlay.visible = true
	var tween = create_tween()
	tween.tween_property(node_game_choosing_panel, "position", Vector2(234, 69), 1)

func _on_new_game_button_pressed():
	node_external_panel_overlay.visible = false
	var tween = create_tween()
	tween.tween_property(node_game_choosing_panel, "position", Vector2(234, 400), 1)
	
	_on_open_name_choosing_panel()

# Start older games from the list

func _on_games_list_item_clicked(index, at_position, mouse_button_index):
	await node_endpoint_get_game.http_get_game(index)

func _on_close_game_choosing_panel():
	node_external_panel_overlay.visible = false
	var tween = create_tween()
	tween.tween_property(node_game_choosing_panel, "position", Vector2(234, 400), 1)

func _on_open_name_choosing_panel():
	node_external_panel_overlay.visible = true
	var tween = create_tween()
	tween.tween_property(node_name_choosing_panel, "position", Vector2(234, 69), 1)

func _on_start_button_pressed():
	if node_player_name_field.text != "":
		node_external_panel_overlay.visible = false
		var tween = create_tween()
		tween.tween_property(node_name_choosing_panel, "position", Vector2(234, 400), 1)
		
		# Change player name on the frontend
		node_avatar_player.find_child("Name").text = current_player_name
		
		# Start Game
		_action_start_game()

func _on_player_name_field_text_changed(new_text):
	current_player_name = new_text

# Restart Game Scene

func _on_restart_game_button_pressed():
	AudioManager.find_child("SoundButton").play()
	SceneTransition.change_scene("res://levels/game/game.tscn", "dissolve")
