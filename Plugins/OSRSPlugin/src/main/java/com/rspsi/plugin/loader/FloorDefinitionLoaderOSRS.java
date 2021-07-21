package com.rspsi.plugin.loader;

import com.displee.cache.index.archive.Archive;
import com.displee.cache.index.archive.file.File;

import java.nio.ByteBuffer;

import com.rspsi.jagex.cache.ArchiveUtils;
import com.rspsi.jagex.cache.def.Floor;
import com.rspsi.jagex.cache.loader.floor.FloorDefinitionLoader;
import com.rspsi.jagex.cache.loader.floor.FloorType;

public class FloorDefinitionLoaderOSRS extends FloorDefinitionLoader {

	private Floor[] overlays;
	private Floor[] underlays;

	@Override
	public void init(Archive archive) {
		
	}
	

	@Override
	public void init(byte[] data) {
	
	}
	
	public void initOverlays(Archive archive) {
		this.overlays = new Floor[ArchiveUtils.getHighestFile(archive).getId() + 1];
		for(File file : archive.files()) {
			if(file != null && file.getData() != null) {
				Floor floor = decodeOverlay(ByteBuffer.wrap(file.getData()));
				floor.generateHsl();
				overlays[file.getId()] = floor;
			}
		}
		
	}
	
	public void initUnderlays(Archive archive) {
		this.underlays = new Floor[ArchiveUtils.getHighestFile(archive).getId() + 1];
		for(File file : archive.files()) {
			if(file != null && file.getData() != null) {
				Floor floor = decodeUnderlay(ByteBuffer.wrap(file.getData()));
				floor.generateHsl();
				underlays[file.getId()] = floor;
			}
		}
		
		
	}


	public Floor decodeUnderlay(ByteBuffer buffer) {
		Floor floor = new Floor();
		while (true){
			int opcode = buffer.get();
			if (opcode == 0) {
				break;
			} else if (opcode == 1) {
				int rgb = ((buffer.get() & 0xff) << 16) + ((buffer.get() & 0xff) << 8) + (buffer.get() & 0xff);
				floor.setRgb(rgb);
			} else {
				System.out.println("Error unrecognised underlay code: " + opcode);
			}
		}
		return floor;
	}

	public Floor decodeOverlay(ByteBuffer buffer) {
		Floor floor = new Floor();
		while (true) {
			int opcode = buffer.get();
			if (opcode == 0) {
				break;
			} else if (opcode == 1) {
				int rgb = ((buffer.get() & 0xff) << 16) + ((buffer.get() & 0xff) << 8) + (buffer.get() & 0xff);
				floor.setRgb(rgb);
			} else if (opcode == 2) {
				int texture = buffer.get() & 0xff;
				floor.setTexture(texture);
			} else if (opcode == 5) {
				floor.setShadowed(false);
			} else if (opcode == 7) {
				int anotherRgb = ((buffer.get() & 0xff) << 16) + ((buffer.get() & 0xff) << 8) + (buffer.get() & 0xff);
				floor.setAnotherRgb(anotherRgb);
			} else {
				System.out.println("Error unrecognised overlay code: " + opcode);
			}
		}
		return floor;
	}
	
	@Override
	public Floor getFloor(int id, FloorType type) {
		if(type == FloorType.OVERLAY)
			return overlays[id];
		else
			return underlays[id];
	}


	@Override
	public int getSize(FloorType type) {
		if(type == FloorType.OVERLAY)
			return overlays.length;
		else
			return underlays.length;
	}

	@Override
	public int count() {
		return 0;
	}

	@Override
	public Floor forId(int arg0) {
		return null;
	}


}
