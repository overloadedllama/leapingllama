package com.overloadedllama.leapingllama.listener;

import com.badlogic.gdx.physics.box2d.*;
import com.overloadedllama.leapingllama.Settings;
import com.overloadedllama.leapingllama.LlamaConstants;
import com.overloadedllama.leapingllama.game.*;
import com.overloadedllama.leapingllama.screens.GameScreen;

public class MyContactListener implements ContactListener, LlamaConstants {

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

        if (isBulletEnemyObstacleContact(fa, fb)) {
            //System.out.println("COLLISION BULLET-ENEMY DETECTED!");
        } else if (isLlamaEnemyContact(fa, fb)) {
            //System.out.println("COLLISION LLAMA-ENEMY DETECTED!");
        } else if (isLlamaGroundPlatformContact(fa, fb, contact)) {
            System.out.println("COLLISION LLAMA-GROUND DETECTED!");
        }

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


    private boolean isBulletEnemyObstacleContact(Fixture a, Fixture b) {
            if (a.getUserData() instanceof Bullet) {
                if ((b.getUserData() instanceof Enemy || b.getUserData() instanceof Obstacle)) {
                    ((Bullet) a.getUserData()).setDestroyable(true);
                    ((GameObject) b.getUserData()).setDestroyable(true);
                }
                parent.updateEnemiesKilled();
                return true;
            }

            if (b.getUserData() instanceof Bullet) {
                if ((a.getUserData() instanceof Enemy) || (a.getUserData() instanceof Obstacle)) {
                    ((Bullet) b.getUserData()).setDestroyable(true);
                    ((GameObject) a.getUserData()).setDestroyable(true);
                }
                parent.updateEnemiesKilled();
                return true;
            }

        return false;
    }

    /**
     * if there is any llama-enemy contact, check llama is punching, and in that
     * case the enemy is destroyed, else game over
     */
    private boolean isLlamaEnemyContact(Fixture a, Fixture b) {
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
                Settings.playSound("punch");
            } else {
                parent.gameOver();
            }

            return true;
        }
        return false;
    }

    // at the moment this is is useless
    private boolean isLlamaGroundPlatformContact(Fixture a, Fixture b, Contact contact) {

        short bitsA = a.getFilterData().categoryBits;
        short bitsB = b.getFilterData().categoryBits;

        boolean llamaGroundCollision = (bitsA == CATEGORY_LLAMA && bitsB == CATEGORY_GROUND) || (bitsB == CATEGORY_LLAMA && bitsA == CATEGORY_GROUND);

        if (llamaGroundCollision) {
            System.out.println("COLLISION LLAMA-GROUND DETECTED!");
        }

        return false;
    }

    private boolean isCoinCollected(Fixture a, Fixture b) {
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
            return true;
        }
        return false;
    }

    private boolean isAmmoCollected(Fixture a, Fixture b) {
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
            return true;
        }
        return false;
    }

    private boolean isNotObstacleDodged(Fixture a, Fixture b) {
        if ((a.getUserData() instanceof Llama && b.getUserData() instanceof Obstacle)
                || (a.getUserData() instanceof Obstacle && b.getUserData() instanceof Llama)) {


            if (a.getUserData() instanceof Llama) {
                parent.gameOver();

            } else {
                parent.gameOver();

            }


            return true;
        }
        return false;
    }
}
