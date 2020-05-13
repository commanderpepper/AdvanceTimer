package commanderpepper.advancetimer.ui.dismisstimer

sealed class MediaPlayerState{
    object Playing: MediaPlayerState()
    object Stopped: MediaPlayerState()
}