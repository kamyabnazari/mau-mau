extends HTTPRequest

# Called when the node enters the scene tree for the first time.
func _ready():
	self.set_use_threads(true)
	self.connect("request_completed", http_start_game_response)

func http_start_game(player_name: String):
	var custom_header: PackedStringArray = ["Content-Type: application/json"]
	self.request("http://localhost:8080/game-rest/startGame", custom_header, HTTPClient.METHOD_POST, player_name)

func http_start_game_response(_result, response_code, _headers: PackedStringArray, body: PackedByteArray):
	if(response_code == 200):
		var json_body = JSON.parse_string(body.get_string_from_utf8())
		GameActions.emit_signal("http_start_game_response", json_body)
	else:
		print('response_code: ', response_code)
		print('problem on the server')
