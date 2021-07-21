package com.rspsi.plugin.loader;

import com.displee.cache.index.archive.Archive;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.stream.IntStream;

import com.rspsi.jagex.cache.loader.map.MapIndexLoader;
import com.rspsi.jagex.cache.loader.map.MapType;
import com.rspsi.jagex.io.Buffer;

public class MapIndexLoader317 extends MapIndexLoader {
	

	private int[] mapHashes, objects, landscapes;
	private boolean[] members;

	@Override
	public void init(Archive archive) {
		byte[] indices = archive.file("map_index").getData();
		Buffer buffer = new Buffer(indices);
		int count = indices.length / 7;
		mapHashes = new int[count];
		landscapes = new int[count];
		objects = new int[count];
		members = new boolean[count];
		int pos = 0;
		for (int region = 0; region < count; region++) {
			mapHashes[region] = buffer.readUShort();
			landscapes[region] = buffer.readUShort();
			objects[region] = buffer.readUShort();
			members[region] = buffer.readUByte() == 1;
			
			pos++;
		}
		System.out.println("expected regions " + count + " - actual " + pos);
	}

	@Override
	public void init(Buffer buffer) {
		int count = buffer.getPayload().length / 7;
		mapHashes = new int[count];
		landscapes = new int[count];
		objects = new int[count];
		int pos = 0;
		for (int region = 0; region < count; region++) {
			mapHashes[region] = buffer.readUShort();
			landscapes[region] = buffer.readUShort();
			objects[region] = buffer.readUShort();
			buffer.readUByte();
		}
		System.out.println("expected regions " + count + " - actual " + pos);
	}

	@Override
	public int getFileId(int hash, MapType type) {
		int index = IntStream.range(0, mapHashes.length)
				.filter(i -> hash == mapHashes[i])
				.findFirst()
				.orElse(-1);
		if(index >= 0) {
			return type == MapType.LANDSCAPE ? landscapes[index] : objects[index];
		}
		
		return -1;
	}

	@Override
	public boolean landscapePresent(int id) {
		return IntStream.range(0, mapHashes.length)
				.filter(i -> id == landscapes[i])
				.findFirst()
				.orElse(-1) >= 0;
	}

	@Override
	public boolean objectPresent(int id) {
		return IntStream.range(0, mapHashes.length)
				.filter(i -> id == objects[i])
				.findFirst()
				.orElse(-1) >= 0;
	}

	
	@Override
	public byte[] encode() {
		ByteBuffer buffer = ByteBuffer.allocate((mapHashes.length * 7));
		for(int index = 0;index<mapHashes.length;index++) {
			buffer.putShort((short) mapHashes[index]);
			buffer.putShort((short) landscapes[index]);
			buffer.putShort((short) objects[index]);
			buffer.put((byte)0);
		}
		return buffer.array();
	}

	@Override
	public void set(int regionX, int regionY, int landscapeId, int objectsId) {
		
		int hash = (regionX << 8) + regionY;
		int index = IntStream.range(0, mapHashes.length)
				.filter(i -> hash == mapHashes[i])
				.findFirst()
				.orElse(-1);
		if(index >= 0) {
			System.out.println("Setting index " + index);
			landscapes[index] = landscapeId;
			objects[index] = objectsId;
		} else {
			System.out.println("Adding new index");
			int[] mapHashes = Arrays.copyOf(this.mapHashes, this.landscapes.length + 1);
			int[] landscapes = Arrays.copyOf(this.landscapes, this.landscapes.length + 1);
			int[] objects = Arrays.copyOf(this.objects, this.landscapes.length + 1);
			boolean[] members = Arrays.copyOf(this.members, this.members.length + 1);
			index = mapHashes.length - 1;
			mapHashes[index] = hash;
			landscapes[index] = landscapeId;
			objects[index] = objectsId;
		
			this.mapHashes = mapHashes;
			this.landscapes = landscapes;
			this.objects = objects;
			this.members = members;
		}
		
	}


}
