package com.rspsi.plugin.loader;

import com.displee.cache.index.archive.Archive;

import com.rspsi.jagex.cache.anim.Graphic;
import com.rspsi.jagex.cache.loader.anim.AnimationDefinitionLoader;
import com.rspsi.jagex.cache.loader.anim.GraphicLoader;
import com.rspsi.jagex.io.Buffer;


public class GraphicLoader474 extends GraphicLoader {


	private Graphic[] graphics;
	private int count;
	
	@Override
	public int count() {
		return count;
	}

	@Override
	public Graphic forId(int id) {
		if(id < 0 || id > count)
			return null;
		return graphics[id];
	}

	@Override
	public void init(Archive archive) {
		Buffer buffer = new Buffer(archive.file("spotanim.dat").getData());
		count = buffer.readUShort();
		if (graphics == null) {
			graphics = new Graphic[count];
		}

		for (int id = 0; id < count; id++) {
			try {
				graphics[id] = decode(buffer);
				graphics[id].setId(id);
			} catch (Exception ex) {

			}
		}
	}

	@Override
	public void init(byte[] data) {
		Buffer buffer = new Buffer(data);
		count = buffer.readUShort();
		if (graphics == null) {
			graphics = new Graphic[count];
		}

		for (int id = 0; id < count; id++) {
			try {
				graphics[id] = decode(buffer);
				graphics[id].setId(id);
			} catch (Exception ex) {

			}
		}
	}
	
	public Graphic decode(Buffer buffer) {
		Graphic graphic = new Graphic();
		do {
			int opcode = buffer.readUByte();
			if (opcode == 0)
				return graphic;

			if (opcode == 1) {
				graphic.setModel(buffer.readUShort());
			} else if (opcode == 2) {
				int animationId = buffer.readUShort();
				if (animationId >= 0) {
					graphic.setAnimation(AnimationDefinitionLoader.getAnimation(animationId));
				}
				graphic.setAnimationId(animationId);
			} else if (opcode == 4) {
				graphic.setBreadthScale(buffer.readUShort());
			} else if (opcode == 5) {
				graphic.setDepthScale(buffer.readUShort());
			} else if (opcode == 6) {
				graphic.setOrientation(buffer.readUShort());
			} else if (opcode == 7) {
				graphic.setAmbience(buffer.readUByte());
			} else if (opcode == 8) {
				graphic.setModelShadow(buffer.readUByte());
			} else if (opcode == 40) {
				int len = buffer.readUByte();
				int[] originalColours = new int[len];
				int[] replacementColours = new int[len];
				for (int i = 0; i < len; i++) {
					originalColours[i] = buffer.readUShort();
					replacementColours[i] = buffer.readUShort();
				}
				graphic.setOriginalColours(originalColours);
				graphic.setReplacementColours(replacementColours);
			} else if(opcode == 41) {
				int len = buffer.readUByte();
				
				for (int i = 0; i < len; i++) {
					buffer.readUShort();
					buffer.readUShort();
				}
			} else {
				System.out.println("Error unrecognised spotanim config code: " + opcode);
			}
		} while (true);
	}

}
