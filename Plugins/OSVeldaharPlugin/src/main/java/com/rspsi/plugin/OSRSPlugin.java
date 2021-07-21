package com.rspsi.plugin;

import com.displee.cache.index.archive.Archive;

import com.rspsi.jagex.Client;
import com.rspsi.jagex.cache.loader.anim.AnimationDefinitionLoader;
import com.rspsi.jagex.cache.loader.anim.FrameBaseLoader;
import com.rspsi.jagex.cache.loader.anim.FrameLoader;
import com.rspsi.jagex.cache.loader.anim.GraphicLoader;
import com.rspsi.jagex.cache.loader.config.VariableBitLoader;
import com.rspsi.jagex.cache.loader.floor.FloorDefinitionLoader;
import com.rspsi.jagex.cache.loader.map.MapIndexLoader;
import com.rspsi.jagex.cache.loader.object.ObjectDefinitionLoader;
import com.rspsi.jagex.cache.loader.textures.TextureLoader;
import com.rspsi.jagex.net.ResourceResponse;
import com.rspsi.plugin.loader.AnimationDefinitionLoaderOSRS;
import com.rspsi.plugin.loader.FloorDefinitionLoaderOSRS;
import com.rspsi.plugin.loader.FrameBaseLoaderOSRS;
import com.rspsi.plugin.loader.FrameLoaderOSRS;
import com.rspsi.plugin.loader.GraphicLoaderOSRS;
import com.rspsi.plugin.loader.MapIndexLoaderOSRS;
import com.rspsi.plugin.loader.ObjectDefinitionLoaderOSRS;
import com.rspsi.plugin.loader.TextureLoaderOSRS;
import com.rspsi.plugin.loader.VarbitLoaderOSRS;
import com.rspsi.plugins.ClientPlugin;

public class OSRSPlugin implements ClientPlugin {

	private FrameLoaderOSRS frameLoader;
	
	@Override
	public void initializePlugin() {
		ObjectDefinitionLoader.instance = new ObjectDefinitionLoaderOSRS();
		FloorDefinitionLoader.instance = new FloorDefinitionLoaderOSRS();
		AnimationDefinitionLoader.instance = new AnimationDefinitionLoaderOSRS();
		MapIndexLoader.instance = new MapIndexLoaderOSRS();
		TextureLoader.instance = new TextureLoaderOSRS();
		frameLoader = new FrameLoaderOSRS();
		FrameLoader.instance = frameLoader;
		FrameBaseLoader.instance = new FrameBaseLoaderOSRS();
		GraphicLoader.instance = new GraphicLoaderOSRS();
		VariableBitLoader.instance = new VarbitLoaderOSRS();
	}

	@Override
	public void onGameLoaded(Client client) {
			frameLoader.init(2500);
			Archive config = client.getCache().createArchive(2, "config");
			ObjectDefinitionLoader.instance.init(config);
			FloorDefinitionLoader.instance.init(config);
			AnimationDefinitionLoader.instance.init(config);
			GraphicLoader.instance.init(config);
			VariableBitLoader.instance.init(config);
			
			Archive version = client.getCache().createArchive(5, "update list");
			MapIndexLoader.instance.init(version);
			

			Archive textures = client.getCache().createArchive(6, "textures");
			TextureLoader.instance.init(textures);
		
	}

	@Override
	public void onResourceDelivered(ResourceResponse arg0) {
		// TODO Auto-generated method stub
		
	}

}
