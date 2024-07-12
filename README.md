# Speed CoCat

## Descrição:
Bem-vindo ao Speedy CoCat, um jogo emocionante desenvolvido em Java com a tecnologia JavaFX. Neste jogo, você controla um gato ágil que deve pular de uma plataforma para outra enquanto evita obstáculos perigosos que podem derrubá-lo. Dentre os céus, nuvens e aviões, seu objetivo é simples: alcançar a maior pontuação possível subindo o mais alto que puder!

## Como jogar:
- Movimento: Use as teclas específicas para mover o gato para a esquerda, direita e para pular.
- Plataformas: Pule de uma plataforma para outra e continuar subindo.
- Obstáculos em Movimento: Fique atento aos obstáculos em movimento. Evite-os para não cair e perder o jogo.
- Pontuação: Continue subindo o mais alto que puder para alcançar a maior pontuação possível.

## Ferramnetas utilizadas:
- *Linguagem de Programação*: Java 21+
- *Biblioteca Gráfica*: JavaFX 21+
- *Controle de Versão*: Git
- *Hospedagem de Código*: GitHub
- *Metodologia Ágil*: Scrum
- *IDE*: IntelliJ IDEA

## Regras e Mecanismos: 
Permanecer o máximo possível pulando entre plataformas, subindo a pontuação enquanto a tela avança continuamente.

Se o player cair fora das plataformas ou ser atingido por algum obstáculo e não conseguir alcançar uma plataforma, o jogo termina.

Controles:
Mover (pular, direita, esquerda)

Interação com plataformas (saltá-las)

Interação com obstáculos (saltar)

## Pesquisa e Inspiração:
Esse jogo foi baseado no app mobile POU, o qual temos uma espécie de "pet" virtual onde é possível abrir um seletor de jogos internos com varias opções, dentre eles o SKY JUMP com um objetivo simples: Vá de uma plataforma para outra enquanto você tenta não cair no vazio. 

Acesse o link para referência: https://youtube.com/shorts/gebBM5YTWbE?si=araHty7H0jiX_ai-

## Informações de funcionamento (técnico):
### Player
Atributos

    velocityY e velocityX: Representam as velocidades vertical e horizontal do jogador.
    accelerationX: A aceleração horizontal aplicada ao jogador.
    gravity: Um valor constante que simula o efeito da gravidade no jogador.
    jumpStrength: A força aplicada quando o jogador pula.
    friction: O coeficiente de atrito para suavizar o movimento horizontal do jogador.
    onPlataform: Um booleano que indica se o jogador está em uma plataforma.
    plataformsJumped: Um contador para o número de plataformas puladas pelo jogador.
    collisionBox: Um retângulo usado para detecção de colisões.

Construtor
    
    Inicializa a imagem do jogador, tamanho, posição, velocidades e a caixa de colisão.

Métodos

    jump(): Define a velocidade vertical do jogador para a força do pulo e marca o jogador como não estando em uma plataforma.
    update(): Atualiza a posição do jogador com base na gravidade, aceleração e atrito, e atualiza a caixa de colisão.
    updateCollisionBox(): Sincroniza a posição da caixa de colisão com a posição do jogador.
    moveLeft(): Define a aceleração horizontal do jogador para mover à esquerda.
    moveRight(): Define a aceleração horizontal do jogador para mover à direita.


 

## Diagrama de Classes:

```mermaid
classDiagram
    direction LR
    
    class Game {
        +void start(Stage stage)
        +static void main(String[] args)
    }
     
    class ScreenController {
        +Label gameTittle
        +Button playButton
        +Button exitButton
        +Button settingsButton
        +AnchorPane background
        +AnchorPane screen2
        +AnchorPane gameOverScreen
        +ImageView cat
        +Scene scene
        +Stage stage
        +FXMLLoader fxmlLoader
        +Player player
        +List~Plataform~ plataforms
        +List~MovingObstacle~ movingObstacles
        +Pane gamePane
        +final int windowWidth
        +final int windowHeight
        +double lastPlataformY
        +int score
        +Text scoreText
        +ImageView backgroundImageView
        +boolean isPaused
        +boolean isGameOver
        +AnimationTimer timer
        +Scene gameScene
        +Scene pauseScene
        +SoundEffect backgroundMusic
        +SoundEffect jumpSound
        +SoundEffect gameOverSound
        +void setStage(Stage stage)
        +void switchToLoad(ActionEvent event)
        +void gameStart()
        +void createInitialPlataforms()
        +void updateMovingObstacles()
        +void createPlataform()
        +void checkCollisions()
        +void scrollScreen()
        +void createMovingObstacle()
        +void gameOver()
        +void switchToScene1()
        +void togglePause()
        +void switchToPause()
        +void switchToGameOver()
    }
     
    class Player {
        -double velocityY
        -double velocityX
        +double accelerationX
        -final double gravity
        -final double jumpStrength
        -final double friction
        -boolean onPlataform
        -int plataformsJumped
        -Rectangle collisionBox
        +Player(String imagePath)
        +void jump()
        +void update()
        +void updateCollisionBox()
        +void moveLeft()
        +void moveRight()
        +double getVelocityY()
        +void setVelocityY(double velocityY)
        +boolean isOnPlataform()
        +void setOnPlataform(boolean onPlataform)
        +int getPlataformsJumped()
        +void incrementPlataformsJumped()
        +Rectangle getCollisionBox()
    }
    
    class Plataform {
        +Plataform(double x, double y, String imagePath)
    }
     
    class MovingObstacle {
        -double direction
        -final double speed
        +MovingObstacle(double x, double y, String imagePath)
        +void update()
    }
     
    class SoundEffect {
        -final String audioPath
        -final boolean loop
        -MediaPlayer mediaPlayer
        +SoundEffect(String audioPath, boolean loop)
        +void play()
        +void stop()
        +boolean isLoop()
        +void setLoop(boolean loop)
    }
    ScreenController --> Player
    ScreenController --> Plataform
    ScreenController --> MovingObstacle
    ScreenController --> SoundEffect
    SoundEffect --> MediaPlayer
    Player <|-- ImageView
    Plataform <|-- ImageView
    MovingObstacle <|-- Plataform
```
## Diagrama de Sequência

```mermaid
sequenceDiagram
    participant User
    participant Game
    participant ScreenController
    participant Player
    participant Platform
    participant MovingObstacle
    participant SoundEffect
    participant GameOverScreen

    User ->> Game: Launch Game
    Game ->> ScreenController: setStage(Stage)
    User ->> ScreenController: click playButton
    ScreenController ->> ScreenController: switchToLoad(ActionEvent)
    activate ScreenController
    ScreenController ->> ScreenController: gameStart()
    deactivate ScreenController
    
    ScreenController ->>+ Player: Create Player
    ScreenController ->>+ Platform: Create Initial Platforms
    ScreenController ->>+ MovingObstacle: Create Initial Moving Obstacles
    ScreenController ->> SoundEffect: Play backgroundMusic
    
    Note over ScreenController, Player: Game Loop Starts
    User ->> ScreenController: Press Key (LEFT or RIGHT)
    ScreenController ->> Player: moveLeft() or moveRight()
    ScreenController ->> Player: update()
    
    ScreenController ->> ScreenController: updateMovingObstacles()
    ScreenController ->> ScreenController: checkCollisions()
    ScreenController ->> ScreenController: scrollScreen()

    ScreenController ->> Player: setOnPlatform(true or false)
    ScreenController ->> SoundEffect: Play jumpSound if Collision Detected
    ScreenController ->> Platform: Update Score if Collision Detected
    
    ScreenController ->> ScreenController: gameOver() if Player Falls Below Screen
    ScreenController ->> SoundEffect: Play gameOverSound if Player Falls Below Screen

    ScreenController ->> GameOverScreen: Show Game Over Screen
    GameOverScreen ->> ScreenController: Restart game
    GameOverScreen ->> Game: Go to main menu

```


<a href="https://www.figma.com/board/kEr6QwTs5c5MAFUdw71LDp/Speedy-Cocat?node-id=0-1&t=xxGhsMKnHWxR4R91-0" target="_blank"><img src="https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white" target="_blank"></a>
