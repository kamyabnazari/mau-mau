extends TextureRect

@export var is_opponent: bool
@export var suit: String
@export var rank: String

var id
var order

var card_back_texture = load("res://levels/game/assets/cards_atlas/atlas_texture_card_back.tres")

func _ready():
	GameActions.connect("played_card_successfully", was_i_played_successfully)
	if is_opponent:
		texture = card_back_texture

func _get_drag_data(_at_position):
	if is_opponent:
		return false
	else:
		var data = {}
		data["texture"] = texture
		data["id"] = id
		data["suit"] = suit
		data["rank"] = rank
		data["order"] = order

		var drag_preview = duplicate()
		drag_preview.modulate.a = 0.5
		
		var preview_control = Control.new()
		preview_control.add_child(drag_preview)
		
		drag_preview.position = -0.5 * drag_preview.size
		
		set_drag_preview(preview_control)
		
		return data

func change_card_to_opponent():
	texture = card_back_texture

func was_i_played_successfully(card_id):
	if card_id == id:
		queue_free()
