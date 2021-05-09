package com.zaca.stillstanding.domain.dto;


import com.fasterxml.jackson.databind.node.ObjectNode;


import lombok.Data;

@Data
public class MatchEvent{
	private EventType type;
	private ObjectNode payload;
	
	public MatchEvent(EventType type, ObjectNode data) {
		this.type = type;
		this.payload = data;
	}
	
	public MatchEvent() {
        // TODO Auto-generated constructor stub
    }


    

}