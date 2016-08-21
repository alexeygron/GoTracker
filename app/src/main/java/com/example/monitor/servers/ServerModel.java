package com.example.monitor.servers;

public class ServerModel {
	
	protected String ipAddr;
	protected String mName;
	protected String numPlayers;
	protected String maxPlayers;
	protected String mMap;
	protected String mGame;
	protected String mTags;
	protected String mDbLabel;

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
	
	public void setName(String name) {
		mName = name;
	}
	
	public String getmName() {
		return mName;
	}
	
	public String getGame () {
		return mGame;
	}
	
	public String getTags () {

		return mTags;
	}
	
	public void setTags (String tags) {
		mTags = tags;
	}
	
	public void setGame (String game) {
		mGame = game;
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
		mMap = mapN;
	}
	
	public String getMap () {
		return mMap;
	}

	public void setDbId (String id) {
		mDbLabel = id;
	}

	public String getDbId () {
		return mDbLabel;
	}

	@Override
	public String toString(){
		return "IpAddr " + ipAddr;
	}
}
