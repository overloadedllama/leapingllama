package com.overloadedllama.leapingllama.listener;

import com.badlogic.gdx.input.GestureDetector;

public class MyGestureListener extends GestureDetector {
    public interface DirectionListener {
        void onLeft();

        void onRight();

        void flingUp();

        void onDown();

        void startPanDown();

        void stopPan();
    }

    public MyGestureListener(DirectionListener directionListener) {
        super(new DirectionGestureListener(directionListener));
    }

    private static class DirectionGestureListener extends GestureAdapter{
        DirectionListener directionListener;

        public DirectionGestureListener(DirectionListener directionListener){
            this.directionListener = directionListener;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            if(Math.abs(velocityX)>Math.abs(velocityY)){
                if(velocityX>0){
                    directionListener.onRight();
                }else{
                    directionListener.onLeft();
                }
            }else{
                if(velocityY>0){
                    directionListener.onDown();
                }else{
                    directionListener.flingUp();
                }
            }
            return super.fling(velocityX, velocityY, button);
        }


        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            if(deltaY>0)
                directionListener.startPanDown();

            return super.pan(x, y, deltaX, deltaY);
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {

            directionListener.stopPan();
            return super.panStop(x, y, pointer, button);
        }
    }

}