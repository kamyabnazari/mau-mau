extends HTTPRequest

# Called when the node enters the scene tree for the first time.
func _ready():
	self.set_use_threads(true)
	self.connect("request_completed", http_play_turn_reponse)

# Function to initiate HTTP GET request
func http_play_turn(game_id: int, user_id: int, action: String, card_to_be_played_id: int, suit: String, mau_mau_called: bool):
	var custom_header: PackedStringArray = ["Content-Type: application/json"]
	var url = "http://localhost:8080/game-rest/playTurn?gameId=" + str(game_id) + "&userId=" + str(user_id) + "&action=" + action + "&cardToBePlayedId=" + str(card_to_be_played_id) + "&suit=" + suit + "&mauMauCalled=" + str(mau_mau_called)
	self.request(url, custom_header, HTTPClient.METHOD_GET)

# Function to handle response from HTTP request
func http_play_turn_reponse(_result, response_code, _headers: PackedStringArray, body: PackedByteArray):
	if response_code == 200:
		var json_body = JSON.parse_string(body.get_string_from_utf8())
		GameActions.emit_signal("http_play_turn_reponse", json_body)
	else:
		print('response_code: ', response_code)
		print('problem on the server')
