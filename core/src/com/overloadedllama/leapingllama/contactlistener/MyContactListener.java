package com.overloadedllama.leapingllama.contactlistener;

import com.badlogic.gdx.physics.box2d.*;
import com.overloadedllama.leapingllama.game.*;
import com.overloadedllama.leapingllama.screens.GameScreen;

public class MyContactListener implements ContactListener {

    final GameScreen parent;

    // set the GameScreen parent in order to not use static method --> some noisy problems...
    public MyContactListener(final GameScreen parent) {
        this.parent = parent;
    }


    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa == null || fb == null) return;
        if (fa.getUserData() == null || fb.getUserData() == null) return;

        if (isBulletEnemyContact(fa, fb)) {
            //System.out.println("COLLISION BULLET-ENEMY DETECTED!");
        } else if (isLlamaEnemyContact(fa, fb)) {
            //System.out.println("COLLISION LLAMA-ENEMY DETECTED!");
        } else if (isLlamaGroundPlatformContact(fa, fb)) {
            //System.out.println("COLLISION LLAMA-GROUND DETECTED!");
        }

    }

    @Override
    public void endContact(Contact contact) {    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {    }


    private boolean isBulletEnemyContact(Fixture a, Fixture b) {

            if ((a.getUserData() instanceof Bullet && b.getUserData() instanceof Enemy)
                    || (a.getUserData() instanceof Enemy && b.getUserData() instanceof Bullet)) {

                if (a.getUserData() instanceof Bullet) {
                    ((Bullet) a.getUserData()).setDestroyable(true);
                } else if (b.getUserData() instanceof Bullet) {
                    ((Bullet) b.getUserData()).setDestroyable(true);
                }

                if (a.getUserData() instanceof Enemy) {
                    ((Enemy) a.getUserData()).setDestroyable(true);
                } else if (b.getUserData() instanceof Enemy) {
                    ((Enemy) b.getUserData()).setDestroyable(true);
                }
                return true;
            }
        return false;
    }

    private boolean isLlamaEnemyContact(Fixture a, Fixture b) {
        if ((a.getUserData() instanceof Llama && b.getUserData() instanceof Enemy)
                || (a.getUserData() instanceof Enemy && b.getUserData() instanceof Llama)) {
            parent.gameOver();
            return true;
        }
        return false;
    }

    // at the moment this is is useless
    private boolean isLlamaGroundPlatformContact(Fixture a, Fixture b) {
        if ((a.getUserData() instanceof Llama && b.getUserData() instanceof Ground)
                || (a.getUserData() instanceof Ground && b.getUserData() instanceof Llama)) {
            return true;
        }

        if ((a.getUserData() instanceof Llama && b.getUserData() instanceof Platform)
                || (a.getUserData() instanceof Platform && b.getUserData() instanceof Llama)) {
            return true;
        }

        return false;
    }

}
