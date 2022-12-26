#include <iostream>
#include <SDL2/SDL.h>
#include <SDL2/SDL_audio.h>

int main(int argc, char const *argv[])
{
    SDL_Init(SDL_INIT_AUDIO);
    std::cout << "Audio initialized.\n";

    SDL_AudioStream *stream = SDL_NewAudioStream(AUDIO_S16, 1, 22050, AUDIO_F32, 2, 48000);
    if (stream == NULL) {
        std::cout << "Stream not ready.\n";
    }

    return 0;
}
