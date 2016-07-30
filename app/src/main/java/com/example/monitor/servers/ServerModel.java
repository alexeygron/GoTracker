package com.example.monitor.servers;

public class ServerModel {
	
	protected String ipAddr;
	protected String srvName;
	protected String numPlayers;
	protected String maxPlayers;
	protected String srvMap;
	protected String srvGame;
	protected String srvTags;
	protected String srvDbId;

	public ServerModel(){
	}
	
	public ServerModel(String ip){
		ipAddr = ip;
	}
	
	public String getIpAddr () {
		return ipAddr;
	}

	public void setIpAddr (String ip) {
		ipAddr = ip;
	}
	
	public void setSrvName (String name) {
		srvName = name;
	}
	
	public String getSrvName () {
		return srvName;
	}
	
	public String getGame () {
		return srvGame;
	}
	
	public String getTags () {

		return srvTags;
	}
	
	public void setTags (String tags) {
		srvTags = tags;
	}
	
	public void setGame (String game) {
		srvGame = game;
	}
	
	
	public void setNumPlayers (String num) {
		numPlayers = num;
	}
	
	public void setMaxPlayers (String max) {
		maxPlayers = max;
	}
	
	public String getPlayers () {
		return numPlayers + "/" + maxPlayers;
	}
	
	public String getNumPlayers () {
		return numPlayers;
	}
	
	public void setMap (String mapN) {
		srvMap = mapN;
	}
	
	public String getMap () {
		return srvMap;
	}

	public void setDbId (String id) {
		srvDbId = id;
	}

	public String getDbId () {
		return srvDbId;
	}

	@Override
	public String toString(){
		return "IpAddr " + ipAddr;
	}
}
