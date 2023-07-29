extends CenterContainer

@export var card: PackedScene

func _ready():
	GameActions.connect("add_played_card_to_discard_pile", _adding_card_to_discard_pile)

func _can_drop_data(_at_position, _data):
	return true

func _drop_data(_at_position, data):
	GameActions.emit_signal("played_card", data)

func _adding_card_to_discard_pile(data):
	var dropped_card = card.instantiate()
	
	AudioManager.find_child("SoundPlace").play()
	
	add_child(dropped_card)
	dropped_card.texture = data["texture"]
	dropped_card.suit = data["suit"]
	dropped_card.rank = data["rank"]
	dropped_card.rank = data["order"]
	dropped_card.mouse_filter = MOUSE_FILTER_IGNORE
	
	GameActions.emit_signal("played_card_successfully", data["id"])
