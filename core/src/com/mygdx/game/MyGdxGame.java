package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import com.mygdx.game.Tree;

import javax.swing.Timer;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture racer;
    Texture left;
    Texture right;
    Texture background;
    Texture treeImg;
    Texture fallenTree;
    Array<Tree> trees;
    int screenWidth, screenHeight;
    int skierWidth = 150, skierHeight = 200;
    int treeWidth = 160, treeHeight = 245;
    Rectangle skier;
    long lastTimeCreated;
    int time = 3000;
    Sound sound;
    Music music;
    double speed = 140;
    int x;
    int count = 0;
    int direction = 2; //1 влево, 2 прямо, 3 вправо


    @Override
    public void create() {
        batch = new SpriteBatch();
        racer = new Texture("ski.png");
        background = new Texture("Snow.jpg");
        left = new Texture("left.png");
        right = new Texture("right.png");
        treeImg = new Texture("tree.png");
        fallenTree = new Texture("fallen.png");
        screenHeight = Gdx.graphics.getHeight();
        screenWidth = Gdx.graphics.getWidth();
        skier = new Rectangle();
        skier.height = skierHeight;
        skier.width = skierWidth;
        skier.x = screenWidth / 2;
        skier.y = screenHeight / 2;
        trees = new Array<>();
        sound = Gdx.audio.newSound(Gdx.files.internal("scream.mp3"));
        createTree();
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        music.setLooping(true);
        music.play();
        x = Gdx.input.getX();
    }


    @Override
    public void render() {
        batch.begin();
        batch.draw(background, 0, 0, screenWidth, screenHeight);
        if (direction == 1) {
            batch.draw(left, skier.x, skier.y, skier.width, skier.height);
        } else if (direction == 2) {
            batch.draw(racer, skier.x, skier.y, skier.width, skier.height);
        } else if (direction == 3) {
            batch.draw(right, skier.x, skier.y, skier.width, skier.height);
        }
        for (Tree t : trees) {
            if (!t.isCrushed()) {
                batch.draw(treeImg, t.x, t.y, t.width, t.height);
            } else if (t.isCrushed()) {
                batch.draw(fallenTree, t.x, t.y, t.width, t.height);
            }
        }
        batch.end();

        if (Gdx.input.isTouched() && Math.abs(Gdx.input.getX() - x) > 20) {
            if (Gdx.input.getX() > x) {
                direction = 3;
                skier.x += 20;
                x = (int) skier.getX();
            } else {
                direction = 1;
                skier.x -= 20;
                x = (int) skier.getX();
            }
        } else {
            direction=2;
        }
        if (skier.x < 0) {
            skier.x = 0;
        }
        if (skier.x > screenWidth - skier.width) {
            skier.x = screenWidth - skier.width;
        }

        for (Tree r : trees) {
            if (r.isCrushed()) {
                r.width = r.getWidth() / 1.005f;
                r.height = r.getHeight() / 1.005f;
            } else {
                r.width = r.getWidth() / 1.004f;
                r.height = r.getHeight() / 1.004f;
            }

            r.y += speed * Gdx.graphics.getDeltaTime();
            if (r.y > screenHeight) {
                trees.removeValue(r, true);
            }
            if (r.overlaps(skier)) {
                if (!r.isCrushed()) {
                    sound.play();
                    speed = 60;
                    time+=50;
                }
                r.setCrushed(true);
            }
        }

        if (TimeUtils.millis() - lastTimeCreated > time) {
            createTree();
            if (time > 500) {
                time -= 30;
            }
        }

    }

    private void createTree() {
        Tree tree = new Tree();
        tree.width = treeWidth;
        tree.height = treeHeight;
        tree.y = 0;
        Random rand = new Random();
        tree.x = rand.nextInt(screenWidth - treeWidth);
        lastTimeCreated = TimeUtils.millis();
        if (speed < 280) {
            speed += 45;
        }
        trees.add(tree);
    }

    @Override
    public void dispose() {
        batch.dispose();
        racer.dispose();
    }
}