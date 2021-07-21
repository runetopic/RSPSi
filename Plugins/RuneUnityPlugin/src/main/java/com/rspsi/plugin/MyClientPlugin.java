package com.rspsi.plugin;

import com.rspsi.jagex.cache.loader.config.VariableBitLoader;
import com.rspsi.plugin.loader.*;
import com.displee.cache.index.archive.Archive;

import java.io.IOException;

import com.rspsi.jagex.Client;
import com.rspsi.jagex.cache.def.TextureDef;
import com.rspsi.jagex.cache.loader.anim.AnimationDefinitionLoader;
import com.rspsi.jagex.cache.loader.anim.FrameBaseLoader;
import com.rspsi.jagex.cache.loader.anim.FrameLoader;
import com.rspsi.jagex.cache.loader.anim.GraphicLoader;
import com.rspsi.jagex.cache.loader.floor.FloorDefinitionLoader;
import com.rspsi.jagex.cache.loader.map.MapIndexLoader;
import com.rspsi.jagex.cache.loader.object.ObjectDefinitionLoader;
import com.rspsi.jagex.cache.loader.textures.TextureLoader;
import com.rspsi.jagex.net.ResourceResponse;
import com.rspsi.editor.cache.CacheFileType;
import com.rspsi.options.Options;
import com.rspsi.plugins.ClientPlugin;
import com.rspsi.util.ChangeListenerUtil;

public class MyClientPlugin implements ClientPlugin {
	
	
	private MyTextureLoader textureLoader;
	private MyObjectDefinitionLoader objLoader;
	private MyFrameLoader frameLoader;
	
	@Override
	public void initializePlugin() {
		objLoader = new MyObjectDefinitionLoader();
		ObjectDefinitionLoader.instance = objLoader;
		FloorDefinitionLoader.instance = new MyFloorDefinitionLoader();
		AnimationDefinitionLoader.instance = new MyAnimationDefinitionLoader();
		MapIndexLoader.instance = new MyMapIndexLoader();
		GraphicLoader.instance = new MyGraphicLoader();
		frameLoader = new MyFrameLoader();
		FrameLoader.instance = frameLoader; 
		FrameBaseLoader.instance = new MyFrameBaseLoader();
		VariableBitLoader.instance = new MyVarbitLoader();
		
	}

	@Override
	public void onGameLoaded(Client client) throws IOException {
			frameLoader.init(5000);
			textureLoader = new MyTextureLoader(51, 1419, client.getProvider());
			TextureLoader.instance = textureLoader;
			

			Archive config = client.getCache().createArchive(2, "config");
			Archive sound = client.getCache().createArchive(8, "sound");
			objLoader.init(config, sound);
			
			FloorDefinitionLoader.instance.init(config);


			VariableBitLoader.instance.init(config);
			ChangeListenerUtil.addListener(() -> {
				FloorDefinitionLoader.instance.init(config);
				textureLoader.clearCache();
				client.mapRegion.updateTiles();
			}, Options.hdTextures);
			
			AnimationDefinitionLoader.instance.init(config);
			
			TextureDef.unpackConfig(config);

			Archive versionList = client.getCache().createArchive(5, "versionlist");
			MapIndexLoader.instance.init(versionList);
			

			Archive textures = client.getCache().createArchive(6, "textures");
			TextureLoader.instance.init(textures);
			
	}

	@Override
	public void onResourceDelivered(ResourceResponse resource) {
		if(resource.getRequest().getType() == CacheFileType.TEXTURE) {
			textureLoader.load(resource.getRequest().getFile(), resource.getData());
		}
	}

}
