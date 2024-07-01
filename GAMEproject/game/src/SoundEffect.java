
import java.io.InputStream;
import javax.sound.sampled.*;

public class SoundEffect implements Runnable {

    // Atributos
    private String audioPath;
    private boolean loop;

    //Contrutores
    public SoundEffect(String audioPath, boolean loop) {
        this.audioPath = audioPath;
        this.loop = loop;
    }

    // M�todos espec�ficos
    public void play () {
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            // Inicia a reprodu��o em loop infinito
            do{
                // Cria um novo fluxo de entrada de �udio a partir do arquivo original
                InputStream inputStream = SoundEffect.class.getResourceAsStream(audioPath);
                AudioInputStream copiedStream = AudioSystem.getAudioInputStream(inputStream);

                // Obt�m o formato de �udio do arquivo .wav
                AudioFormat audioFormat = copiedStream.getFormat();

                // Cria um DataLine.Info para a linha de reprodu��o
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

                // Obt�m a linha de reprodu��o do sistema
                SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

                // Abre novamente a linha de reprodu��o
                line.open(audioFormat);

                // Inicia a reprodu��o
                line.start();

                // Cria um buffer para armazenar os dados do �udio
                byte[] buffer = new byte[4096];
                int bytesRead = 0;

                // L� dados do �udio do InputStream e escreve na linha de reprodu��o
                while ((bytesRead = copiedStream.read(buffer)) != -1) {
                    line.write(buffer, 0, bytesRead);
                }

                // Encerra a reprodu��o
                line.drain();
                line.stop();
                line.close();
                copiedStream.close();
            }while (loop);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // M�todos de acesso
    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

}
