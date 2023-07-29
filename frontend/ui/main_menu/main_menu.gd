extends Control

func _ready():
	AudioManager.find_child("MusicStack").play()

func _on_new_game_button_pressed():
	AudioManager.find_child("SoundButton").play()
	SceneTransition.change_scene("res://levels/game/game.tscn", "dissolve")

func _on_quit_button_pressed():
	AudioManager.find_child("SoundButton").play()
	get_tree().quit()

func _on_credits_button_pressed():
	AudioManager.find_child("SoundButton").play()
	$ExternalPanelOverlay.visible = true
	var tween = create_tween()
	tween.tween_property($CreditsPanel, "position", Vector2(234, 118), 1)

func _on_settings_button_pressed():
	AudioManager.find_child("SoundButton").play()
	$ExternalPanelOverlay.visible = true
	var tween = create_tween()
	tween.tween_property($SettingsPanel, "position", Vector2(234, 118), 1)

func _on_close_settings_button_pressed():
	AudioManager.find_child("SoundButton").play()
	$ExternalPanelOverlay.visible = false
	var tween = create_tween()
	tween.tween_property($SettingsPanel, "position", Vector2(234, 400), 1)

func _on_close_credits_button_pressed():
	AudioManager.find_child("SoundButton").play()
	$ExternalPanelOverlay.visible = false
	var tween = create_tween()
	tween.tween_property($CreditsPanel, "position", Vector2(234, 400), 1)

func _on_fullscreen_toggle_pressed():
	AudioManager.find_child("SoundButton").play()
	if 	DisplayServer.window_get_mode() == 3:
		DisplayServer.window_set_mode(DisplayServer.WINDOW_MODE_WINDOWED)
	else:
		DisplayServer.window_set_mode(DisplayServer.WINDOW_MODE_FULLSCREEN)

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
