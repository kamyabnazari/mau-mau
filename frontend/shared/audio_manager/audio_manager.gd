extends Node

func _input(event):
	if event is InputEventMouseButton:
		AudioManager.find_child("SoundClick").play()
