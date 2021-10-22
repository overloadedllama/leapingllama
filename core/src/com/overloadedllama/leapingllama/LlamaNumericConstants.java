package com.overloadedllama.leapingllama;

public interface LlamaNumericConstants {


    int CHUNK_LENGTH = 50;
    float STEP_TIME = 1.0f / 60.0f;
    int VELOCITY_ITERATIONS = 6;
    int POSITION_ITERATIONS = 2;

    float  UNITS_PER_METER = 128;

    float METER_WIDTH = GameApp.WIDTH / UNITS_PER_METER;
    float METER_HEIGHT = GameApp.HEIGHT / UNITS_PER_METER;

    float VELOCITY = 2;



}
