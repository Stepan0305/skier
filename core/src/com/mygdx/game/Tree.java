package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

public class Tree extends Rectangle {
    private boolean isCrushed;
    public Tree(){
        isCrushed=false;
    }

    public boolean isCrushed() {
        return isCrushed;
    }

    public void setCrushed(boolean crushed) {
        isCrushed = crushed;
    }
}