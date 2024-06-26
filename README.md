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

Se o personagem cair fora das plataformas ou ser atingido por algum obstáculo e não conseguir alcançar uma plataforma, o jogo termina.

Controles:
Mover (pular, direita, esquerda)

Interação com plataformas (saltá-las)

Interação com obstáculos (ser atingido)

Ativar boost (impuslo de jump)

## Diagrama de Classes:

```mermaid
classDiagram
    Screen <|-- Image
    Image <|-- Player
    Image <|-- MovingObstacle 
    Image <|-- StoppedObstacle
    Sounds --|> Screen

    class Sounds{
        +noises()
    }

    class Screen{
        background
    }

    class Image{
        +drawn()
    }

    class StoppedObstacle{
    }

    class MovingObstacle{
        +velocity
        +move(set x)
    }

    class Player{
        +string name
        +int points
        +int timeSurvived
        +jump(set y)
        +velocity()
        +move(set x)
    }
```
<a href="https://www.figma.com/board/kEr6QwTs5c5MAFUdw71LDp/Speedy-Cocat?node-id=0-1&t=xxGhsMKnHWxR4R91-0" target="_blank"><img src="https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white" target="_blank"></a>