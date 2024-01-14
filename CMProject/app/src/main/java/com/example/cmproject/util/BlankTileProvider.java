package com.example.cmproject.util;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

import java.io.ByteArrayOutputStream;

public class BlankTileProvider implements TileProvider {
    private static final int TILE_WIDTH = 256;
    private static final int TILE_HEIGHT = 256;

    @Override
    public Tile getTile(int x, int y, int zoom) {
        // Create a blank Bitmap for each tile
        Bitmap bitmap = Bitmap.createBitmap(TILE_WIDTH, TILE_HEIGHT, Bitmap.Config.ARGB_8888);

        // Convert the Bitmap to a byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        // Return the tile
        return new Tile(TILE_WIDTH, TILE_HEIGHT, byteArray);
    }
}