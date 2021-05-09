package com.zaca.stillstanding.domain.dto;

public enum ResourceType {
	IMAGE,
	VOICE,
	NONE;
	
	public static ResourceType getByLocalFileExtension(String name) {
		if (name.endsWith(".jpg") || name.endsWith(".png")) {
			return ResourceType.IMAGE;
		}
		
		if (name.endsWith(".Ogg") || name.endsWith(".ogg")) {
			return ResourceType.VOICE;
		}
		
		return ResourceType.NONE;
	}
	
	

}
