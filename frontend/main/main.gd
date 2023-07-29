extends Node

func _ready():
	SceneTransition.transition_dissolve_backwards()

func _on_timer_timeout():
	SceneTransition.change_scene("res://ui/main_menu/main_menu.tscn", "dissolve")
