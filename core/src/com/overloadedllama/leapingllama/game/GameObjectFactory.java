package com.overloadedllama.leapingllama.game;

import com.overloadedllama.leapingllama.llamautils.LlamaUtil;


public class GameObjectFactory extends AbstractFactory {

    public GameObjectFactory(LlamaUtil llamaUtil) {
        super(llamaUtil);
    }

    public GameObject createGameObject(String type, float x, float y, float h, float length) {
        switch (type) {
            case LLAMA:
                return new Llama(x, y, h, llamaUtil);
            case ALIEN:
                return new Enemy(x, y, h, llamaUtil);
            case BULLET:
                return new Bullet(x, y, h, llamaUtil);
            case GROUNDS:
                return new Ground(x, y, h, length, VELOCITY, llamaUtil);
            case PLATFORM:
                return new Platform(x, y, h, length, VELOCITY, llamaUtil);
            case OBSTACLE:
                return new Obstacle(x, y, h, VELOCITY * 2, llamaUtil);
            case ALIEN_CYAN_DEAD:
                return new EnemyDied(ALIEN_CYAN_DEAD, x, y, h, llamaUtil);
            case ALIEN_YELLOW_DEAD:
                return new EnemyDied(ALIEN_YELLOW_DEAD, x, y, h, llamaUtil);
            default:
                throw new RuntimeException("Game Object Type " + type + " does not exist.");
        }
    }


}
