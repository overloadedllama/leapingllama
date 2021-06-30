package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.physics.box2d.*;
import com.overloadedllama.leapingllama.game.Bullet;
import com.overloadedllama.leapingllama.game.Enemy;
import com.overloadedllama.leapingllama.game.Llama;

public class MyContactListener implements ContactListener {

    public MyContactListener() {

    }


    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa == null || fb == null) return;
        if (fa.getUserData() == null || fb.getUserData() == null) return;

        if (isBulletEnemyContact(fa, fb)) {
            System.out.println("COLLISION BULLET-ENEMY DETECTED!");
        } else if (isLlamaEnemyContact(fa, fb)) {
            System.out.println("COLLISION LLAMA-ENEMY DETECTED!");
        }



    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }


    /**
     * if the contact is between a bullet and an enemy, remove both from respective arrays
     */
    private boolean isBulletEnemyContact(Fixture a, Fixture b) {

            if ((a.getUserData() instanceof Bullet && b.getUserData() instanceof Enemy)
                    || (a.getUserData() instanceof Enemy && b.getUserData() instanceof Bullet)) {

                if (a.getUserData() instanceof Bullet) {
                    GameScreen.removeBullet((Bullet) a.getUserData());
                } else if (b.getUserData() instanceof Bullet) {
                    GameScreen.removeBullet((Bullet) b.getUserData());
                }

                if (a.getUserData() instanceof Enemy) {
                    GameScreen.removeEnemy((Enemy) a.getUserData());
                } else if (b.getUserData() instanceof Enemy) {
                    GameScreen.removeEnemy((Enemy) b.getUserData());
                }
                return true;
            }
        return false;
    }

    private boolean isLlamaEnemyContact(Fixture a, Fixture b) {
        return ((a.getUserData() instanceof Llama && b.getUserData() instanceof Enemy)
                || (a.getUserData() instanceof Enemy && b.getUserData() instanceof Llama));
    }

}
