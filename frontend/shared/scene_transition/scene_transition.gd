extends CanvasLayer

# Fade in and out animation between scenes
@onready var _animation_player = $AnimationPlayer

func change_scene(target: String, type: String) -> void:
	if type == "dissolve":
		scene_transition_dissolve(target)

func scene_transition_dissolve(target: String) -> void:
	await transition_dissolve()
	SceneManager.goto_scene(target)
	await transition_dissolve_backwards()

func transition_dissolve() -> void:
	_animation_player.play("dissolve")
	await _animation_player.animation_finished

func transition_dissolve_backwards() -> void:
	_animation_player.play_backwards("dissolve")
	await _animation_player.animation_finished
