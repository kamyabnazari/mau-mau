extends Node

# Card Actions
signal played_card(data)
signal played_card_successfully(id)
signal add_played_card_to_discard_pile(data)

# HTTP Actions
signal http_start_game_response(body)
signal http_get_playable_cards_response(body)
signal http_play_turn_reponse(body)
signal http_get_all_games_response(body)
signal http_get_game_response(body)
