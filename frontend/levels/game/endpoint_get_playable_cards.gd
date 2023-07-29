extends HTTPRequest

# Called when the node enters the scene tree for the first time.
func _ready():
	self.set_use_threads(true)
	self.connect("request_completed", http_get_playable_cards_response)

func http_get_playable_cards(game_id: int, player_index: int):
	var custom_header: PackedStringArray = ["Content-Type: application/json"]
	var url = "http://localhost:8080/game-rest/get-playable-cards?gameId=" + str(game_id) + "&playerIndex=" + str(player_index)
	self.request(url, custom_header, HTTPClient.METHOD_GET)

func http_get_playable_cards_response(_result, response_code, _headers: PackedStringArray, body: PackedByteArray):
	if(response_code == 200):
		var json_body = JSON.parse_string(body.get_string_from_utf8())
		GameActions.emit_signal("http_get_playable_cards_response", json_body)
	else:
		print('response_code: ', response_code)
		print('problem on the server')
