package org.example.speedy;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

// Classe para gerenciar e reproduzir efeitos sonoros
public class SoundEffect {

    // Atributos
    private final String audioPath; // Caminho do arquivo de áudio
    private final boolean loop; // Indica se o áudio deve ser reproduzido em loop
    private MediaPlayer mediaPlayer; // Reprodutor de mídia

    // Construtor
    public SoundEffect(String audioPath, boolean loop) {
        this.audioPath = audioPath; // Define o caminho do arquivo de áudio
        this.loop = loop; // Define se o áudio deve ser reproduzido em loop

        // Cria um caminho de mídia para o arquivo de áudio
        String mediaPath = getClass().getResource("/" + audioPath).toString();
        this.mediaPlayer = new MediaPlayer(new Media(mediaPath));

        // Configura o ciclo de repetição do áudio se loop for verdadeiro
        if (loop) {
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Repetição indefinida
        }
    }

    // Método para reproduzir o som
    public void play() {
        mediaPlayer.play(); // Inicia a reprodução do áudio
    }

    // Método para parar o som
    public void stop() {
        mediaPlayer.stop(); // Para a reprodução do áudio
    }

    // Métodos de acesso
    public boolean isLoop() {
        return loop; // Retorna se o áudio está configurado para loop
    }

    public void setLoop(boolean loop) {
        if (loop) {
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Define repetição indefinida se loop for verdadeiro
        } else {
            mediaPlayer.setCycleCount(0); // Para a reprodução após o ciclo atual se loop for falso
        }
    }
}
