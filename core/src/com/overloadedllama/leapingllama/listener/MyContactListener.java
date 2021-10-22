package com.overloadedllama.leapingllama.listener;

import com.badlogic.gdx.physics.box2d.*;
import com.overloadedllama.leapingllama.llamautils.LlamaUtil;
import com.overloadedllama.leapingllama.LlamaConstants;
import com.overloadedllama.leapingllama.game.*;
import com.overloadedllama.leapingllama.screens.GameScreen;

public class MyContactListener implements ContactListener, LlamaConstants {
    final GameScreen parent;
    LlamaUtil llamaUtil;

    public MyContactListener(final GameScreen parent, LlamaUtil llamaUtil) {
        this.parent = parent;
        this.llamaUtil = llamaUtil;
    }


    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa == null || fb == null) return;
        if (fa.getUserData() == null || fb.getUserData() == null) return;

        isBulletEnemyContact(fa, fb);
        isLlamaEnemyContact(fa, fb);
        isAmmoCollected(fa, fb);
        isCoinCollected(fa, fb);
        isNotObstacleDodged(fa, fb);
    }

    @Override
    public void endContact(Contact contact) {    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {    }


    private void isBulletEnemyContact(Fixture a, Fixture b) {
        if ((a.getUserData() instanceof Bullet) && (b.getUserData() instanceof Enemy)) {
            ((Bullet) a.getUserData()).setDestroyable(true);
            ((Enemy) b.getUserData()).decreaseNumLife();
            parent.updateEnemiesKilled();
            return;
        }

        if ((b.getUserData() instanceof Bullet) && (a.getUserData() instanceof Enemy)) {
            ((Bullet) b.getUserData()).setDestroyable(true);
            ((Enemy) a.getUserData()).decreaseNumLife();
            parent.updateEnemiesKilled();
        }

    }

    /**
     * if there is any llama-enemy contact, check llama is punching, and in that
     * case the enemy is destroyed, else game over
     */
    private void isLlamaEnemyContact(Fixture a, Fixture b) {
        if ((a.getUserData() instanceof Llama && b.getUserData() instanceof Enemy)
                || (a.getUserData() instanceof Enemy && b.getUserData() instanceof Llama)) {

            if (parent.getLlama().isPunching() &&
                    parent.getLlama().getBody().getLinearVelocity().y > -0.001 &&
                    parent.getLlama().getBody().getLinearVelocity().y < 0.001) {

                if (a.getUserData() instanceof Enemy) {
                    ((Enemy) a.getUserData()).setDestroyable(true);
                } else {
                    ((Enemy) b.getUserData()).setDestroyable(true);
                }
                llamaUtil.getSoundManager().playSound(PUNCH);
            } else {
                System.out.println("COLLISION LLAMA-ENEMY DETECTED");
                parent.gameOver();
            }

        }
    }

    private void isCoinCollected(Fixture a, Fixture b) {
        if ((a.getUserData() instanceof Llama && b.getUserData() instanceof Coin)
                || (a.getUserData() instanceof Coin && b.getUserData() instanceof Llama)) {


            if (a.getUserData() instanceof Coin) {
                ((Coin) a.getUserData()).setDestroyable(true);
                parent.setMoney(parent.getMoney()+((Coin) a.getUserData()).getQuantity());

            } else {
                ((Coin) b.getUserData()).setDestroyable(true);
                parent.setMoney(parent.getMoney()+((Coin) b.getUserData()).getQuantity());


            }
            parent.updateCoinsTaken();
        }
    }

    private void isAmmoCollected(Fixture a, Fixture b) {
        if ((a.getUserData() instanceof Llama && b.getUserData() instanceof Ammo)
                || (a.getUserData() instanceof Ammo && b.getUserData() instanceof Llama)) {

            if (a.getUserData() instanceof Ammo) {
                ((Ammo) a.getUserData()).setDestroyable(true);
                parent.setAmmunition(parent.getAmmunition()+((Ammo) a.getUserData()).getQuantity());

            } else {
                ((Ammo) b.getUserData()).setDestroyable(true);
                parent.setAmmunition(parent.getAmmunition()+((Ammo) b.getUserData()).getQuantity());

            }

            parent.updateAmmosTaken();
        }
    }

    private void isNotObstacleDodged(Fixture a, Fixture b) {
        if ((a.getUserData() instanceof Llama && b.getUserData() instanceof Obstacle)
                || (a.getUserData() instanceof Obstacle && b.getUserData() instanceof Llama)) {

            System.out.println("COLLISION LLAMA-OBSTACLE DETECTED");
            parent.gameOver();
        }
    }
}
